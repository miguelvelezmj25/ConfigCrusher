package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.cc.TaintLabel;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleMain;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

public class PhosphorAnalysis extends BaseDynamicAnalysis {

  private static final String PHOSPHOR_OUTPUT_DIR =
      BaseAdapter.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";
  private static final String PHOSPHOR_SCRIPTS_DIR = BaseAdapter.USER_HOME
      + "/Documents/Programming/Java/Projects/phosphor/Phosphor/scripts/run-instrumented/implicit-optimized";

  PhosphorAnalysis(String programName) {
    super(programName);
  }

  void dynamicAnalysis(Set<String> initialConfig, Set<String> options) throws IOException {
    Set<Map<String, Boolean>> exploredConstraints = new HashSet<>();
    Set<Map<String, Boolean>> constraintsToExplore = new HashSet<>();
    constraintsToExplore.add(this.toConstraint(initialConfig, options));

    int count = 0;
    while (!constraintsToExplore.isEmpty()) {
      Pair<Map<String, Boolean>, Set<String>> nextConstraint = PhosphorAnalysis.getNextConstraint(
          constraintsToExplore);
      Map<String, Boolean> constraintToExplore = nextConstraint.getLeft();
      Set<String> config = nextConstraint.getRight();

      Set<Map<String, Boolean>> exploringConstraints = PhosphorAnalysis
          .getExploringConstraints(constraintToExplore);
      constraintsToExplore.removeAll(exploringConstraints);
      exploredConstraints.addAll(exploringConstraints);

//      // TODO run the analysis
      Map<String, Set<String>> results = this.analyzePhosphorResults();
      Set<Set<String>> taintsAtSinks = new HashSet<>(results.values());
      Set<Map<String, Boolean>> currentConstraints = PhosphorAnalysis
          .calculateConstraints(taintsAtSinks);

      Set<Map<String, Boolean>> currentExploredConstraints = PhosphorAnalysis
          .getExploredConstraints(
              currentConstraints, exploredConstraints);
      currentConstraints.removeAll(currentExploredConstraints);
      constraintsToExplore.addAll(currentConstraints);

      count++;
    }

    System.out.println(count);
  }

  void runPhosphorAnalysis(Set<String> config) throws IOException, InterruptedException {
    if (config == null) {
      throw new IllegalArgumentException("The configuration cannot be null");
    }

    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(this.getProgramName(), config);
    builder.command(commandList);
    builder.directory(new File(PHOSPHOR_SCRIPTS_DIR));
    Process process = builder.start();

    this.processOutput(process);
    System.out.println();
    this.processError(process);
    System.out.println();

    process.waitFor();
  }

  private void processError(Process process) throws IOException {
    System.out.println("Errors: ");
    BufferedReader errorReader =
        new BufferedReader(new InputStreamReader(process.getErrorStream()));
    String string;

    while ((string = errorReader.readLine()) != null) {
      if (!string.isEmpty()) {
        System.out.println(string);
      }
    }
  }

  private void processOutput(Process process) throws IOException {
    System.out.println("Output: ");
    BufferedReader inputReader =
        new BufferedReader(new InputStreamReader(process.getInputStream()));
    String string;

    while ((string = inputReader.readLine()) != null) {
      if (!string.isEmpty()) {
        System.out.println(string);
      }
    }
  }

  private List<String> buildCommandAsList(String programName, Set<String> config) {
    List<String> commandList = new ArrayList<>();

    Adapter adapter;

    if (programName.equals(DynamicRunningExampleMain.PROGRAM_NAME)) {
      commandList.add("./examples.sh");
      adapter = new DynamicRunningExampleAdapter.Builder().build();
    }
    else {
      throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);

    return commandList;
  }

  static Set<Map<String, Boolean>> getExploringConstraints(
      Map<String, Boolean> constraintToExplore) {
    Set<Map<String, Boolean>> exploringConstraints = new HashSet<>();
    Set<String> options = constraintToExplore.keySet();
    Set<Set<String>> optionsCombinations = Helper.getCombinations(options);

    for (Set<String> optionsCombo : optionsCombinations) {
      Map<String, Boolean> constraint = new HashMap<>();

      for (String option : optionsCombo) {
        constraint.put(option, constraintToExplore.get(option));
      }

      exploringConstraints.add(constraint);
    }

    return exploringConstraints;
  }

  private Map<String, Set<String>> analyzePhosphorResults() throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + this.getProgramName();
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 2) {
      throw new RuntimeException("The directory " + dir + " does not have 2 files.");
    }

    return this.readPhosphorTaintResults(serializedFiles);
  }

  static Set<Map<String, Boolean>> getExploredConstraints(
      Set<Map<String, Boolean>> currentConstraints,
      Set<Map<String, Boolean>> exploredConstraints) {
    if (currentConstraints == null || currentConstraints.isEmpty()) {
      throw new IllegalArgumentException("The current constraints cannot be empty");
    }

    if (exploredConstraints == null || exploredConstraints.isEmpty()) {
      throw new IllegalArgumentException("The explored constraints cannot be empty");
    }

    Set<Map<String, Boolean>> currentConstraintsAlreadyExplored = new HashSet<>();

    for (Map<String, Boolean> currentConstraintsEntry : currentConstraints) {
      for (Map<String, Boolean> exploredConstraintsEntry : exploredConstraints) {
        if (exploredConstraintsEntry.entrySet().containsAll(currentConstraintsEntry.entrySet())) {
          currentConstraintsAlreadyExplored.add(currentConstraintsEntry);
        }
      }
    }

    return currentConstraintsAlreadyExplored;
  }

  private Map<String, Boolean> toConstraint(Set<String> initialConfig,
      Set<String> options) {
    Map<String, Boolean> constraint = new HashMap<>();

    for (String option : options) {
      boolean value = initialConfig.contains(option);
      constraint.put(option, value);
    }

    return constraint;
  }


  @Override
  public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + this.getProgramName();
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 2) {
      throw new RuntimeException("The directory " + dir + " does not have 2 files.");
    }

    this.readPhosphorTaintResults(serializedFiles);

    // TODO
    return null;
  }

  private Map<String, Set<String>> readPhosphorTaintResults(Collection<File> serializedFiles)
      throws IOException {
    Map<String, Set<TaintLabel>> sinksToTaintsFromTaints = new HashMap<>();
    Map<String, Set<TaintLabel>> sinksToTaintsFromStacks = new HashMap<>();

    for (File file : serializedFiles) {
      if (file.getName().contains("taints")) {
        sinksToTaintsFromTaints = this.deserialize(file);
      }
      else {
        sinksToTaintsFromStacks = this.deserialize(file);
      }
    }

    Map<String, Set<TaintLabel>> sinksToTaintLabels = this.merge(sinksToTaintsFromTaints,
        sinksToTaintsFromStacks);

    Map<String, Set<String>> sinksToTaints = this.changeTaintLabelsToTaints(sinksToTaintLabels);

//    for (Map.Entry<String, Set<String>> entry : sinksToTaints.entrySet()) {
//      System.out.println(entry.getKey() + " --> " + entry.getValue());
//    }

    return sinksToTaints;
  }

  private Map<String, Set<String>> changeTaintLabelsToTaints(
      Map<String, Set<TaintLabel>> sinksToTaintLabels) {
    Map<String, Set<String>> sinksToTaints = new HashMap<>();

    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaintLabels.entrySet()) {
      Set<String> taints = new HashSet<>();

      for (TaintLabel taintLabel : entry.getValue()) {
        taints.add(taintLabel.getSource());
      }

      sinksToTaints.put(entry.getKey(), taints);
    }

    return sinksToTaints;
  }

  private Map<String, Set<TaintLabel>> merge(Map<String, Set<TaintLabel>> sinksToTaints1,
      Map<String, Set<TaintLabel>> sinksToTaints2) {
    Map<String, Set<TaintLabel>> sinksToTaints = new HashMap<>(sinksToTaints1);

    for (String sink : sinksToTaints2.keySet()) {
      if (!sinksToTaints.containsKey(sink)) {
        sinksToTaints.put(sink, new HashSet<>());
      }
    }

    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaints2.entrySet()) {
      String sink = entry.getKey();
      Set<TaintLabel> taints = sinksToTaints.get(sink);
      taints.addAll(entry.getValue());
      sinksToTaints.put(sink, taints);
    }

    return sinksToTaints;
  }

  private Map<String, Set<TaintLabel>> deserialize(File file)
      throws IOException {
    FileInputStream fis = new FileInputStream(file);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Map<String, Set<TaintLabel>> sinksToTaints;

    try {
      sinksToTaints = (Map<String, Set<TaintLabel>>) ois.readObject();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    ois.close();
    fis.close();

    return sinksToTaints;
  }

  private Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }

  static Set<Map<String, Boolean>> calculateConstraints(Set<Set<String>> taintsAtSinks) {
    if (taintsAtSinks == null || taintsAtSinks.isEmpty()) {
      throw new IllegalArgumentException("The taints at sinks cannot be empty");
    }

    Set<Map<String, Boolean>> constraints = new HashSet<>();

    for (Set<String> taintsAtSink : taintsAtSinks) {
      constraints.addAll(PhosphorAnalysis.buildConstraints(taintsAtSink));
    }

    return constraints;
  }

  /**
   * Builds a set of partial configurations
   */
  static Set<Map<String, Boolean>> buildConstraints(Set<String> taintsAtSink) {
    if (taintsAtSink == null || taintsAtSink.isEmpty()) {
      throw new IllegalArgumentException("The taints at sink cannot be empty");
    }

    Set<Map<String, Boolean>> constraints = new HashSet<>();
    Set<Set<String>> configs = Helper.getConfigurations(taintsAtSink);

    for (Set<String> config : configs) {
      Map<String, Boolean> constraint = new HashMap<>();

      for (String taint : taintsAtSink) {
        boolean value = config.contains(taint);
        constraint.put(taint, value);
      }

      constraints.add(constraint);
    }

    return constraints;
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Pair<Map<String, Boolean>, Set<String>> getNextConstraint(
      Set<Map<String, Boolean>> constraintsToEvaluate) {
    if (constraintsToEvaluate == null || constraintsToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
    }

    Map<String, Boolean> constraintToEvaluate = PhosphorAnalysis
        .pickNextConstraint(constraintsToEvaluate);
    Set<String> config = PhosphorAnalysis.buildConfig(constraintToEvaluate);

    return Pair.of(constraintToEvaluate, config);
  }

  // TODO add test cases
  // TODO optimize how to pick the next constraint to evaluate, maybe pick the one with the most options? Merge constraints?
  static Map<String, Boolean> pickNextConstraint(
      Set<Map<String, Boolean>> constraintsToEvaluate) {
    return constraintsToEvaluate.iterator().next();
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Set<String> buildConfig(Map<String, Boolean> constraintToEvaluate) {
    if (constraintToEvaluate == null || constraintToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraint to evaluate should not be empty");
    }

    Set<String> config = new HashSet<>();

    for (Map.Entry<String, Boolean> entry : constraintToEvaluate.entrySet()) {
      if (entry.getValue()) {
        config.add(entry.getKey());
      }
    }

    return config;
  }

}
