package edu.cmu.cs.mvelezce.evaluation.dta.constraints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class idtaConstraintsAnalyzer implements Analysis<Set<FeatureExpr>> {

  private final String programName;
  private final Set<ConfigConstraint> configConstraints;
  private final Set<String> options;

  idtaConstraintsAnalyzer(
      String programName, Set<ConfigConstraint> configConstraints, Set<String> options) {
    this.programName = programName;
    this.configConstraints = configConstraints;
    this.options = options;
  }

  idtaConstraintsAnalyzer(String programName) {
    this.programName = programName;
    this.configConstraints = new HashSet<>();
    this.options = new HashSet<>();
  }

  @Override
  public Set<FeatureExpr> analyze() {
    List<String> stringConstraints = this.getStringConstraints();

    return new HashSet<>(MinConfigsGenerator.getFeatureExprs(stringConstraints));
  }

  private List<String> getStringConstraints() {
    System.err.println("This code was copied and pasted");
    List<String> stringConstraints = new ArrayList<>();

    for (ConfigConstraint configConstraint : this.configConstraints) {
      String constraint = this.getConstraint(configConstraint);
      stringConstraints.add(constraint);
    }

    return stringConstraints;
  }

  private String getConstraint(ConfigConstraint configConstraint) {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, Boolean> partialConfig = configConstraint.getPartialConfig();
    stringBuilder.append("(");

    Iterator<Entry<String, Boolean>> partialConfigIter = partialConfig.entrySet().iterator();

    while (partialConfigIter.hasNext()) {
      Entry<String, Boolean> entry = partialConfigIter.next();

      if (!entry.getValue()) {
        stringBuilder.append("!");
      }

      stringBuilder.append(entry.getKey());

      if (partialConfigIter.hasNext()) {
        stringBuilder.append(" && ");
      }
    }

    stringBuilder.append(")");

    return stringBuilder.toString();
  }

  @Override
  public Set<FeatureExpr> analyze(String[] args) throws IOException {
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

    Set<FeatureExpr> interactions = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(interactions);
    }

    return interactions;
  }

  @Override
  public void writeToFile(Set<FeatureExpr> interactions) throws IOException {
    System.err.println("This code was copied and pasted");
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
  public Set<FeatureExpr> readFromFile(File file) throws IOException {
    System.err.println("This code was copied and pasted");
    ObjectMapper mapper = new ObjectMapper();
    List<String> stringConstraints = mapper.readValue(file, new TypeReference<List<String>>() {});

    return new HashSet<>(MinConfigsGenerator.getFeatureExprs(stringConstraints));
  }

  @Override
  public String outputDir() {
    System.err.println("Change phosphor to another name");
    return Options.DIRECTORY
        + "/evaluation/dta/constraints/java/programs/phosphor/"
        + this.programName;
  }
}
