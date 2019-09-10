package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public class InteractionAnalyzer implements Analysis<List<FeatureExpr>> {

  private final String programName;
  private final Set<SubtraceAnalysisInfo> subtraceAnalysisInfos;
  private final Set<String> options;

  InteractionAnalyzer(
      String programName, Set<SubtraceAnalysisInfo> subtraceAnalysisInfos, Set<String> options) {
    this.programName = programName;
    this.subtraceAnalysisInfos = subtraceAnalysisInfos;
    this.options = options;
  }

  InteractionAnalyzer(String programName) {
    this.programName = programName;
    this.subtraceAnalysisInfos = new HashSet<>();
    this.options = new HashSet<>();
  }

  @Override
  public List<FeatureExpr> analyze() {
    List<String> constraints = this.buildStringConstraints();

    return MinConfigsGenerator.getFeatureExprs(constraints);
  }

  private List<String> buildStringConstraints() {
    System.err.println("This is copied and pasted code");
    List<String> constraints = new ArrayList<>();

    Set<Set<Set<String>>> processedConfigs = new HashSet<>();

    for (SubtraceAnalysisInfo subtraceAnalysisInfo : this.subtraceAnalysisInfos) {
      Map<String, Set<Set<String>>> valuesToConfigs = subtraceAnalysisInfo.getValuesToConfigs();

      if (valuesToConfigs.size() == 1) {
        continue;
      }

      for (Map.Entry<String, Set<Set<String>>> entry : valuesToConfigs.entrySet()) {
        Set<Set<String>> configs = entry.getValue();

        if (processedConfigs.contains(configs)) {
          continue;
        }

        String stringConstraints = toStringConstraints(configs);

        if (stringConstraints.isEmpty()) {
          continue;
        }

        constraints.add(stringConstraints);
        processedConfigs.add(configs);
      }
    }

    return constraints;
  }

  private String toStringConstraints(Set<Set<String>> configs) {
    StringBuilder orConstraints = new StringBuilder();

    Iterator<Set<String>> configsIter = configs.iterator();

    while (configsIter.hasNext()) {
      Set<String> config = configsIter.next();
      String andConstraint = this.getAndConstraints(config);
      orConstraints.append(andConstraint);

      if (configsIter.hasNext()) {
        orConstraints.append(" || ");
      }
    }

    return orConstraints.toString();
  }

  private String getAndConstraints(Set<String> config) {
    StringBuilder stringBuilder = new StringBuilder("(");

    Iterator<String> optionsIter = this.options.iterator();

    while (optionsIter.hasNext()) {
      String option = optionsIter.next();

      if (!config.contains(option)) {
        stringBuilder.append("!");
      }

      stringBuilder.append(option);

      if (optionsIter.hasNext()) {
        stringBuilder.append(" && ");
      }
    }

    stringBuilder.append(")");

    return stringBuilder.toString();
  }

  @Override
  public List<FeatureExpr> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    String outputFile = this.outputDir();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      if (files.size() != 1) {
        throw new RuntimeException(
            "We expected to find 1 file in the directory, but that is not the case " + outputFile);
      }

      return this.readFromFile(files.iterator().next());
    }

    List<FeatureExpr> interactions = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(interactions);
    }

    return interactions;
  }

  @Override
  public void writeToFile(List<FeatureExpr> interactions) throws IOException {
    String outputFile = this.outputDir() + "/" + this.programName + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    List<String> stringInteractions = new ArrayList<>();

    for (FeatureExpr featureExpr : interactions) {
      String stringInteraction = featureExpr.toTextExpr().replaceAll("definedEx\\(", "");

      for (String option : this.options) {
        stringInteraction = stringInteraction.replaceAll(option + "\\)", option);
      }

      stringInteractions.add(stringInteraction);
    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, stringInteractions);
  }

  @Override
  public List<FeatureExpr> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<String> stringConstraints = mapper.readValue(file, new TypeReference<List<String>>() {});

    return MinConfigsGenerator.getFeatureExprs(stringConstraints);
  }

  @Override
  public String outputDir() {
    return Options.DIRECTORY + "/analysis/spec/interactions/java/programs/" + this.programName;
  }
}
