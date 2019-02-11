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

    Set<Constraint> constraintsToSatisfy = new HashSet<>();
    Set<Constraint> satisfiedConstraints = new HashSet<>();

    for (Set<String> config : configs) {
      Constraint configAsConstraint = Constraint.fromConfig(config, this.getOptions());
      satisfiedConstraints.add(configAsConstraint);

      this.runPhosphorAnalysis(config);
      this.postProcessPhosphorAnalysis(config);

      Set<Constraint> analysisConstraints = BFPhosphorAnalysis
          .getProgramConstraints(this.getSinksToData().values());
      constraintsToSatisfy.addAll(analysisConstraints);

      Set<Constraint> satisfiedConstraintsByConfig = this
          .getSatisfiedConstraintsByConfig(constraintsToSatisfy, configAsConstraint);
      satisfiedConstraints.addAll(satisfiedConstraintsByConfig);
      constraintsToSatisfy.removeAll(satisfiedConstraints);
    }

    if (!constraintsToSatisfy.isEmpty()) {
      throw new RuntimeException("Not all constraints were satisfied");
    }

  }

  private Set<Constraint> getSatisfiedConstraintsByConfig(Set<Constraint> constraints,
      Constraint configAsConstraint) {
    Set<Constraint> satisfiedConstraints = new HashSet<>();

    for (Constraint constraint : constraints) {
      if (constraint.isSubConstraintOf(configAsConstraint)) {
        satisfiedConstraints.add(constraint);
      }
    }

    return satisfiedConstraints;
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
