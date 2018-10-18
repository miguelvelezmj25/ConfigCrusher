package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import com.google.common.annotations.VisibleForTesting;
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

  private final Map<String, Set<Map<String, Boolean>>> sinksToConstraints = new HashMap<>();

  PhosphorAnalysis(String programName) {
    super(programName);
  }

  /**
   * Input: P, c in C, O
   *
   * Output: TODO
   *
   * Input: The program is provided elsewhere. Therefore, there is no need to pass the program to
   * this method.
   */
  void dynamicAnalysis(Set<String> initialConfig, Set<String> options)
      throws IOException, InterruptedException {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("The options cannot be empty");
    }

    Set<Map<String, Boolean>> exploredConstraints = new HashSet<>();
    Set<Map<String, Boolean>> constraintsToExplore = new HashSet<>();
    constraintsToExplore.add(PhosphorAnalysis.toConstraint(initialConfig, options));

    int count = 0;

    while (!constraintsToExplore.isEmpty()) {
      // CTE, c := get_next_constraint(CE,O)
      Pair<Map<String, Boolean>, Set<String>> nextConstraint = PhosphorAnalysis
          .getNextConstraint(constraintsToExplore);
      Map<String, Boolean> constraintToExplore = nextConstraint.getLeft();
      Set<String> config = nextConstraint.getRight();

      // CE.removeAll(CTE) // all sub constraints
      Set<Map<String, Boolean>> exploringConstraints = PhosphorAnalysis
          .getAllCombinationsOfConstraints(constraintToExplore);
      constraintsToExplore.removeAll(exploringConstraints);
      // EC.addAll(CTE) // all sub constraints
      exploredConstraints.addAll(exploringConstraints);

      // TS := run_taint_analysis(P’, c)
      this.runPhosphorAnalysis(config);
      Map<String, Set<String>> sinksToTaints = this.analyzePhosphorResults();
      this.calculateConstraintsPerSink(sinksToTaints);

      // CT := range(TS) // TS: S —> P(O)
      Set<Set<String>> taintsAtSinks = new HashSet<>(sinksToTaints.values());
      // CC := calc_constraints(CT)
      Set<Map<String, Boolean>> constraintsFromAnalysis = PhosphorAnalysis
          .calculateConstraints(taintsAtSinks);

      // CC.removeAll(EC) // all explored constraints
      Set<Map<String, Boolean>> currentExploredConstraints = PhosphorAnalysis
          .getExploredConstraints(
              constraintsFromAnalysis, exploredConstraints);
      constraintsFromAnalysis.removeAll(currentExploredConstraints);
      // CE.addAll(CC)
      constraintsToExplore.addAll(constraintsFromAnalysis);

      count++;
    }

    System.out.println(count);
    // TODO this might be done in the compression step, not in the analysis
    this.getConfigsForCC();
  }

  @Override
  public Map<JavaRegion, Set<Map<String, Boolean>>> analyze() throws IOException {
    throw new UnsupportedOperationException("Implement");
//    String dir = PHOSPHOR_OUTPUT_DIR + "/" + this.getProgramName();
//    Collection<File> serializedFiles = this.getSerializedFiles(dir);
//
//    if (serializedFiles.size() != 2) {
//      throw new RuntimeException("The directory " + dir + " does not have 2 files.");
//    }
//
//    this.readPhosphorTaintResults(serializedFiles);
//
//    // TODO
//    return null;
  }

  Set<Set<String>> getConfigsForCC() {
    Set<Map<String, Boolean>> ccConstraints = this.getAllConstraints();
    Set<Set<String>> configs = new HashSet<>();

    for (Map<String, Boolean> ccConstraint : ccConstraints) {
      Set<String> config = toConfig(ccConstraint);
      configs.add(config);
    }

    System.out.println(configs);
    return configs;
  }

  private Set<Map<String, Boolean>> getAllConstraints() {
    Set<Map<String, Boolean>> ccConstraints = new HashSet<>();

    for (Set<Map<String, Boolean>> constraints : this.sinksToConstraints.values()) {
      ccConstraints.addAll(constraints);
    }

    return ccConstraints;
  }

  /**
   * Input: P', c in C
   *
   * Output: TS S --> P(O)
   *
   * Input: There is a script that this method calls to executed the instrumented program.
   * Therefore, we do not not pass the instrumented program to this method.
   *
   * Output: This method only runs the phosphor analysis. There is another method that processes the
   * results from the analysis.
   */
  void runPhosphorAnalysis(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(this.getProgramName(), config);
    builder.command(commandList);
    builder.directory(new File(PHOSPHOR_SCRIPTS_DIR));
    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

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

  /**
   * Calculate all constraints and subconstraints of a constraint.
   */
  static Set<Map<String, Boolean>> getAllCombinationsOfConstraints(
      Map<String, Boolean> constraintToExplore) {
    if (constraintToExplore.isEmpty()) {
      throw new IllegalArgumentException("The constraint to explore cannot be empty");
    }

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

  /**
   * Helper method for running the phosphor analysis. This method processes the results of the
   * analysis and returns the output specified in the algorithm.
   *
   * Output: TS: S --> P(O)
   */
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
    if (currentConstraints.isEmpty()) {
      throw new IllegalArgumentException("The current constraints cannot be empty");
    }

    if (exploredConstraints.isEmpty()) {
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

  static Map<String, Boolean> toConstraint(Set<String> config, Set<String> options) {
    if (options.isEmpty()) {
      throw new IllegalArgumentException("The options cannot be empty");
    }

    Map<String, Boolean> constraint = new HashMap<>();

    for (String option : options) {
      boolean value = config.contains(option);
      constraint.put(option, value);
    }

    return constraint;
  }

  // TODO separate the taints from the taints and from the stacks
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

  private Map<String, Set<TaintLabel>> deserialize(File file) throws IOException {
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

  /**
   * Input: CT: P(P(O))
   *
   * Output: CC: set of constraint (i.e., partial configs)
   */
  static Set<Map<String, Boolean>> calculateConstraints(Set<Set<String>> taintsAtSinks) {
    if (taintsAtSinks.isEmpty()) {
      throw new IllegalArgumentException("The taints at sinks cannot be empty");
    }

    Set<Map<String, Boolean>> constraints = new HashSet<>();

    for (Set<String> taintsAtSink : taintsAtSinks) {
      constraints.addAll(PhosphorAnalysis.buildConstraints(taintsAtSink));
    }

    return constraints;
  }

  void calculateConstraintsPerSink(Map<String, Set<String>> sinksToTaints) {
    if (sinksToTaints.isEmpty()) {
      throw new IllegalArgumentException("The taints to sinks map cannot be empty");
    }

    this.addNewSinks(sinksToTaints.keySet());

    for (Map.Entry<String, Set<String>> entry : sinksToTaints.entrySet()) {
      String sink = entry.getKey();
      Set<String> taintsAtSink = entry.getValue();
      Set<Map<String, Boolean>> constraintsAtSink = PhosphorAnalysis.buildConstraints(taintsAtSink);

      Set<Map<String, Boolean>> currentConstraints = this.sinksToConstraints.get(sink);
      currentConstraints.addAll(constraintsAtSink);
      this.sinksToConstraints.put(entry.getKey(), currentConstraints);
    }
  }

  private void addNewSinks(Set<String> sinks) {
    for (String sink : sinks) {
      if (!this.sinksToConstraints.containsKey(sink)) {
        this.sinksToConstraints.put(sink, new HashSet<>());
      }
    }
  }

  /**
   * Input: CTS in CT
   *
   * Output: CCS in CFA
   */
  static Set<Map<String, Boolean>> buildConstraints(Set<String> taintsAtSink) {
    if (taintsAtSink.isEmpty()) {
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
   * Input: CE, O
   *
   * Output: CTE: P(CE), c in C
   *
   * Input: since we represent options set to false by not including them in the set that represents
   * configurations, there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Pair<Map<String, Boolean>, Set<String>> getNextConstraint(
      Set<Map<String, Boolean>> constraintsToEvaluate) {
    if (constraintsToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
    }

    Map<String, Boolean> constraintToEvaluate = PhosphorAnalysis
        .pickNextConstraint(constraintsToEvaluate);
    Set<String> config = PhosphorAnalysis.toConfig(constraintToEvaluate);

    return Pair.of(constraintToEvaluate, config);
  }

  // TODO optimize how to pick the next constraint to evaluate, maybe pick the one with the most options? Merge constraints?
  static Map<String, Boolean> pickNextConstraint(Set<Map<String, Boolean>> constraintsToEvaluate) {
    if (constraintsToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
    }

    return constraintsToEvaluate.iterator().next();
  }

  /**
   * In theory, this method should also take the set of options O of the program. However, since we
   * represent options set to false by not including them in the set that represents configurations,
   * there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Set<String> toConfig(Map<String, Boolean> constraint) {
    if (constraint.isEmpty()) {
      throw new IllegalArgumentException("The constraint should not be empty");
    }

    Set<String> config = new HashSet<>();

    for (Map.Entry<String, Boolean> entry : constraint.entrySet()) {
      if (entry.getValue()) {
        config.add(entry.getKey());
      }
    }

    return config;
  }

  Map<String, Set<Map<String, Boolean>>> getSinksToConstraints() {
    return sinksToConstraints;
  }

  @VisibleForTesting
  void addSinksToConstraints(Map<String, Set<Map<String, Boolean>>> sinksToConstraints) {
    this.sinksToConstraints.putAll(sinksToConstraints);
  }
}
