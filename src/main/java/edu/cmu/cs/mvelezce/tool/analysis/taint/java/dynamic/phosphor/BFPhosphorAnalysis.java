package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.alldynamic.AllDynamicAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO analyze different of picking first configuration to run?
public class BFPhosphorAnalysis extends PhosphorAnalysis {

  private String mainClass;

  BFPhosphorAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public BFPhosphorAnalysis(String programName) {
    super(programName);
  }

  BFPhosphorAnalysis(String programName, String mainClass, Set<String> options) {
    this(programName, options);

    this.mainClass = mainClass;
  }

  @Override
  void runDynamicAnalysis() throws IOException, InterruptedException {
    Set<String> options = this.getOptions();
    Set<Set<String>> configs = Helper.getConfigurations(options);

    Set<ConfigConstraint> configConstraintsToSatisfy = new HashSet<>();
    Set<ConfigConstraint> satisfiedConfigConstraints = new HashSet<>();

    for (Set<String> config : configs) {
      ConfigConstraint configConstraint = ConfigConstraint.fromConfig(config, this.getOptions());
      satisfiedConfigConstraints.add(configConstraint);

      this.runPhosphorAnalysis(config);
      this.postProcessPhosphorAnalysis(config);

      Set<ConfigConstraint> analysisConfigConstraints = BFPhosphorAnalysis
          .getAnalysisConfigConstraints(this.getSinksToData().values());
      configConstraintsToSatisfy.addAll(analysisConfigConstraints);

      Set<ConfigConstraint> satisfiedConstraintsByConfig = this
          .getSatisfiedConfigConstraintsByConfig(configConstraintsToSatisfy, configConstraint);
      satisfiedConfigConstraints.addAll(satisfiedConstraintsByConfig);
      configConstraintsToSatisfy.removeAll(satisfiedConfigConstraints);
    }

    if (!configConstraintsToSatisfy.isEmpty()) {
      throw new RuntimeException("Not all constraints were satisfied");
    }

  }

  private Set<ConfigConstraint> getSatisfiedConfigConstraintsByConfig(
      Set<ConfigConstraint> configConstraints, ConfigConstraint executedConfigConstraint) {
    Set<ConfigConstraint> satisfiedConfigConstraints = new HashSet<>();

    for (ConfigConstraint configConstraint : configConstraints) {
      if (configConstraint.isSubConstraintOf(executedConfigConstraint)) {
        satisfiedConfigConstraints.add(configConstraint);
      }
    }

    return satisfiedConfigConstraints;
  }

  @Override
  public String outputDir() {
    return BaseDynamicRegionAnalysis.DIRECTORY + "/" + this.getProgramName() + "/bf";
  }

  // TODO this method is hardcoded to run all dynamic examples
  @Override
  List<String> buildCommandAsList(Set<String> config) {
    if (this.mainClass == null) {
      return super.buildCommandAsList(config);
    }

    String programName = this.getProgramName();
    Adapter adapter = new AllDynamicAdapter(programName, this.mainClass);

    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);

    List<String> commandList = new ArrayList<>();
    commandList.add("./examples.sh");
    commandList.add(adapter.getMainClass());
    commandList.addAll(configList);

    return commandList;
  }
}
