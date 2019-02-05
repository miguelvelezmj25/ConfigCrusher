package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

/**
 * Analyses what value each subtrace has.
 */
public class SubtracesValueAnalysis implements Analysis<Set<ConfigSubtraceValueInfo>> {

  private final String programName;
  private final Map<Set<String>, List<String>> configsToTraces;
  private final List<String> alignedTrace;

  public SubtracesValueAnalysis(String programName) {
    this.programName = programName;
    this.configsToTraces = new HashMap<>();
    this.alignedTrace = new ArrayList<>();
  }

  public SubtracesValueAnalysis(String programName, Map<Set<String>, List<String>> configsToTraces,
      List<String> alignedTrace) {
    this.programName = programName;
    this.configsToTraces = configsToTraces;
    this.alignedTrace = alignedTrace;
  }

  @Override
  public Set<ConfigSubtraceValueInfo> analyze() {
    Set<ConfigSubtraceValueInfo> configSubtraceValues = new HashSet<>();

    for (Map.Entry<Set<String>, List<String>> entry : this.configsToTraces.entrySet()) {
      Set<String> config = entry.getKey();
      List<String> trace = entry.getValue();

      Map<String, String> subtracesToValues = this.getSubtracesToValues(trace);

      ConfigSubtraceValueInfo configSubtraceValue = new ConfigSubtraceValueInfo(config, subtracesToValues);
      configSubtraceValues.add(configSubtraceValue);
    }

    return configSubtraceValues;
  }

  private Map<String, String> getSubtracesToValues(List<String> trace) {
    Map<String, String> subtracesToValues = new HashMap<>();

    for (String subtrace : this.alignedTrace) {
      subtracesToValues.put(subtrace, "");
    }

    Map<String, Integer> subtracesToIndexes = this.getSubtracesToIndexes(trace);

    for (String subtrace : this.alignedTrace) {
      int index = subtracesToIndexes.getOrDefault(subtrace, -1);

      if (index < 0 || index == (trace.size() - 1)) {
        continue;
      }

      String subtraces = trace.get(index + 1);

      if (!subtraces.startsWith(SubtraceLabel.LABEL)) {
        subtracesToValues.put(subtrace, subtraces);
      }

    }

    return subtracesToValues;

  }

  private Map<String, Integer> getSubtracesToIndexes(List<String> trace) {
    Map<String, Integer> subtracesToIndexes = new HashMap<>();

    for (int i = 0; i < trace.size(); i++) {
      subtracesToIndexes.put(trace.get(i), i);
    }

    return subtracesToIndexes;
  }

  @Override
  public Set<ConfigSubtraceValueInfo> analyze(String[] args) throws IOException {
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

    Set<ConfigSubtraceValueInfo> configSubtraceValues = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(configSubtraceValues);
    }

    return configSubtraceValues;
  }

  @Override
  public Set<ConfigSubtraceValueInfo> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<Set<ConfigSubtraceValueInfo>>() {
    });
  }

  @Override
  public void writeToFile(Set<ConfigSubtraceValueInfo> configTraceValues) throws IOException {
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, configTraceValues);
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY + "/analysis/spec/subtracesvalues/java/programs/" + this.programName;
  }

}
