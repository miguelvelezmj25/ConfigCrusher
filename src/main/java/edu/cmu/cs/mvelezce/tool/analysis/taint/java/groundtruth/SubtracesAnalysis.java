package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

/**
 * Analyzes how many values each subtrace has and what configurations got that value
 */
public class SubtracesAnalysis implements Analysis<Set<SubtraceAnalysisInfo>> {

  private final String programName;
  private final Set<ConfigSubtraceValueInfo> configSubtraceValues;

  SubtracesAnalysis(String programName) {
    this.programName = programName;
    this.configSubtraceValues = new HashSet<>();
  }

  SubtracesAnalysis(String programName, Set<ConfigSubtraceValueInfo> configSubtraceValues) {
    this.programName = programName;
    this.configSubtraceValues = configSubtraceValues;
  }

  @Override
  public Set<SubtraceAnalysisInfo> analyze() {
    Set<String> uniqueSubtraces = this.getUniqueSubtraces();
    Map<String, Set<String>> subtracesToValues = this.getSubtracesToValues(uniqueSubtraces);
    return this.some(subtracesToValues);
  }

  // TODO rename
  private Set<SubtraceAnalysisInfo> some(Map<String, Set<String>> subtracesToAllValues) {
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = new HashSet<>();

    for (Map.Entry<String, Set<String>> entry : subtracesToAllValues.entrySet()) {
      String subtrace = entry.getKey();
      Set<String> values = entry.getValue();

      Map<String, Set<Set<String>>> valuesToConfigs = new HashMap<>();

//      for (String value : values) {
//        valuesToConfigs.put(value, new HashSet<>());
//      }

      for (String value : values) {
        Set<Set<String>> configsWithValue = this.getConfigsWithvalueInSubtrace(value, subtrace);
        valuesToConfigs.put(value, configsWithValue);
      }

      SubtraceAnalysisInfo subtraceAnalysisInfo = new SubtraceAnalysisInfo(subtrace,
          valuesToConfigs);
      subtraceAnalysisInfos.add(subtraceAnalysisInfo);
    }

    return subtraceAnalysisInfos;
  }

  private Set<Set<String>> getConfigsWithvalueInSubtrace(String value, String subtrace) {
    Set<Set<String>> configs = new HashSet<>();

    for (ConfigSubtraceValueInfo subtraceValueInfo : this.configSubtraceValues) {
      Map<String, String> subtracesToValues = subtraceValueInfo.getSubtracesToValues();

      String subtraceValue = subtracesToValues.get(subtrace);

      if (subtraceValue == null || !subtraceValue.equals(value)) {
        continue;
      }

      configs.add(subtraceValueInfo.getConfig());
    }

    return configs;
  }

  private Map<String, Set<String>> getSubtracesToValues(Set<String> uniqueSubtraces) {
    Map<String, Set<String>> subtracesToValues = new HashMap<>();

    for (String subtrace : uniqueSubtraces) {
      subtracesToValues.put(subtrace, new HashSet<>());
    }

    for (String subtrace : uniqueSubtraces) {
      Set<String> subtraceValues = this.getAllValues(subtrace);
      subtracesToValues.put(subtrace, subtraceValues);
    }

    return subtracesToValues;
  }

  private Set<String> getAllValues(String subtrace) {
    Set<String> values = new HashSet<>();

    for (ConfigSubtraceValueInfo configSubtraceValue : this.configSubtraceValues) {
      Map<String, String> subtracesToValues = configSubtraceValue.getSubtracesToValues();
      values.add(subtracesToValues.getOrDefault(subtrace, ""));
    }

    return values;
  }

  private Set<String> getUniqueSubtraces() {
    Set<String> uniqueSubtraces = new HashSet<>();

    for (ConfigSubtraceValueInfo configSubtraceValue : this.configSubtraceValues) {
      Map<String, String> subtracesToValues = configSubtraceValue.getSubtracesToValues();
      uniqueSubtraces.addAll(subtracesToValues.keySet());
    }

    return uniqueSubtraces;
  }

  @Override
  public Set<SubtraceAnalysisInfo> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case "
                + outputFile);
      }

      return this.readFromFile(files.iterator().next());
    }

    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(subtraceAnalysisInfos);
    }

    return subtraceAnalysisInfos;
  }

  @Override
  public void writeToFile(Set<SubtraceAnalysisInfo> subtraceAnalysisInfos) throws IOException {
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, subtraceAnalysisInfos);
  }

  @Override
  public Set<SubtraceAnalysisInfo> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<SubtraceAnalysisInfo>>() {
    });
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY + "/analysis/spec/analysisvalues/java/programs/" + this.programName;
  }

}
