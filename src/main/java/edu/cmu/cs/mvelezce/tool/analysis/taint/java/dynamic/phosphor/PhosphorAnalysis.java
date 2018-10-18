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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

  private final Map<String, Set<Constraint>> sinksToConstraints = new HashMap<>();

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

//    Set<Constraint> exploredConstraints = new HashSet<>();
//    Set<Constraint> constraintsToExplore = new HashSet<>();
//    constraintsToExplore.add(Constraint.toConstraint(initialConfig, options));
//
//    int count = 0;
//
//    while (!constraintsToExplore.isEmpty()) {
//      // CTE, c := get_next_constraint(CE,O)
//      Pair<Map<String, Boolean>, Set<String>> nextConstraint = PhosphorAnalysis
//          .getNextConstraint(constraintsToExplore);
//      Map<String, Boolean> constraintToExplore = nextConstraint.getLeft();
//      Set<String> config = nextConstraint.getRight();
//
//      // CE.removeAll(CTE) // all sub constraints
//      Set<Map<String, Boolean>> exploringConstraints = PhosphorAnalysis
//          .getAllCombinationsOfConstraints(constraintToExplore);
//      constraintsToExplore.removeAll(exploringConstraints);
//      // EC.addAll(CTE) // all sub constraints
//      exploredConstraints.addAll(exploringConstraints);
//
//      // TS := run_taint_analysis(P’, c)
//      this.runPhosphorAnalysis(config);
//      Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults = this.analyzePhosphorResults();
//      this.calculateConstraintsPerSink(sinksToTaintsResults);
//
////      // CT := range(TS) // TS: S —> P(O)
////      Set<Set<String>> taintsAtSinks = new HashSet<>(sinksToTaintsResults.values());
////      // CC := calc_constraints(CT)
////      Set<Map<String, Boolean>> constraintsFromAnalysis = PhosphorAnalysis
////          .calculateConstraints(taintsAtSinks);
////
////      // CC.removeAll(EC) // all explored constraints
////      Set<Map<String, Boolean>> currentExploredConstraints = PhosphorAnalysis
////          .getExploredConstraints(
////              constraintsFromAnalysis, exploredConstraints);
////      constraintsFromAnalysis.removeAll(currentExploredConstraints);
////      // CE.addAll(CC)
////      constraintsToExplore.addAll(constraintsFromAnalysis);
//
//      count++;
//    }
//
//    System.out.println(count);
//    // TODO this might be done in the compression step, not in the analysis
//    this.getConfigsForCC();
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

//  Set<Set<String>> getConfigsForCC() {
//    Set<Constraint> ccConstraints = this.getAllConstraints();
//    Set<Set<String>> configs = new HashSet<>();
//
//    for (Constraint ccConstraint : ccConstraints) {
//      Set<String> config = Constraint.toConfig(ccConstraint);
//      configs.add(config);
//    }
//
//    System.out.println(configs);
//    return configs;
//  }

  private Set<Constraint> getAllConstraints() {
    Set<Constraint> allConstraints = new HashSet<>();

    for (Set<Constraint> constraints : this.sinksToConstraints.values()) {
      allConstraints.addAll(constraints);
    }

    return allConstraints;
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
   * Output: TS: S --> (P(O), P(O))
   */
  private Pair<Map<String, Set<String>>, Map<String, Set<String>>> analyzePhosphorResults()
      throws IOException {
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

  private Pair<Map<String, Set<String>>, Map<String, Set<String>>> readPhosphorTaintResults(
      Collection<File> serializedFiles)
      throws IOException {
    Map<String, Set<TaintLabel>> sinksToTaintLabelsFromTaints = new HashMap<>();
    Map<String, Set<TaintLabel>> sinksToTaintLabelsFromContext = new HashMap<>();

    for (File file : serializedFiles) {
      if (file.getName().contains("taints")) {
        sinksToTaintLabelsFromTaints = this.deserialize(file);
      }
      else {
        sinksToTaintLabelsFromContext = this.deserialize(file);
      }
    }

    Map<String, Set<String>> sinksToTaintsFromTaints = this
        .changeTaintLabelsToTaints(sinksToTaintLabelsFromTaints);
    Map<String, Set<String>> sinksToTaintsFromContext = this
        .changeTaintLabelsToTaints(sinksToTaintLabelsFromContext);

    return Pair.of(sinksToTaintsFromTaints, sinksToTaintsFromContext);
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

//  private Map<String, Set<TaintLabel>> merge(Map<String, Set<TaintLabel>> sinksToTaints1,
//      Map<String, Set<TaintLabel>> sinksToTaints2) {
//    Map<String, Set<TaintLabel>> sinksToTaints = new HashMap<>(sinksToTaints1);
//
//    for (String sink : sinksToTaints2.keySet()) {
//      if (!sinksToTaints.containsKey(sink)) {
//        sinksToTaints.put(sink, new HashSet<>());
//      }
//    }
//
//    for (Map.Entry<String, Set<TaintLabel>> entry : sinksToTaints2.entrySet()) {
//      String sink = entry.getKey();
//      Set<TaintLabel> taints = sinksToTaints.get(sink);
//      taints.addAll(entry.getValue());
//      sinksToTaints.put(sink, taints);
//    }
//
//    return sinksToTaints;
//  }

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
      constraints.addAll(PhosphorAnalysis.buildPartialConfigs(taintsAtSink));
    }

    return constraints;
  }

  void calculateConstraintsPerSink(
      Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults,
      Set<String> config) {
    Map<String, Set<String>> sinksToTaintsFromTaints = sinksToTaintsResults.getLeft();
    Map<String, Set<String>> sinksToTaintsFromContext = sinksToTaintsResults.getRight();

    if (sinksToTaintsFromTaints == null || sinksToTaintsFromContext == null) {
      throw new IllegalArgumentException("The sinks to taints result cannot be empty");
    }

    Set<String> executedSinks = sinksToTaintsFromContext.keySet();
    this.addNewSinks(executedSinks);

    for (String sink : executedSinks) {
      Set<Constraint> constraintsAtSink = this
          .buildConstraints(sinksToTaintsFromTaints.get(sink), sinksToTaintsFromContext.get(sink),
              config);

      this.sinksToConstraints.put(sink, constraintsAtSink);
    }

  }

  private Set<Constraint> buildConstraints(Set<String> taintsFromTaint,
      Set<String> taintsFromContext, Set<String> config) {
    Set<Constraint> constraints = new HashSet<>();

    Set<Map<String, Boolean>> partialConfigs = PhosphorAnalysis
        .buildPartialConfigs(taintsFromTaint);
    Map<String, Boolean> context = this.buildContext(taintsFromContext, config);

    for (Map<String, Boolean> partialConfig : partialConfigs) {
      constraints.add(new Constraint(partialConfig, context));
    }

    PhosphorAnalysis.removeInvalidConstraints(constraints);

    return constraints;
  }

  static void removeInvalidConstraints(Set<Constraint> constraints) {
    constraints.removeIf(constraint -> !constraint.isValid());
  }

  private Map<String, Boolean> buildContext(Set<String> taintsFromContext,
      Set<String> config) {
    Map<String, Boolean> context = new HashMap<>();

    for (String taint : taintsFromContext) {
      context.put(taint, config.contains(taint));
    }

    return context;
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
  static Set<Map<String, Boolean>> buildPartialConfigs(Set<String> taintsAtSink) {
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
    Set<String> config = Constraint.toConfig(constraintToEvaluate);

    return Pair.of(constraintToEvaluate, config);
  }

  // TODO optimize how to pick the next constraint to evaluate, maybe pick the one with the most options? Merge constraints?
  static Map<String, Boolean> pickNextConstraint(Set<Map<String, Boolean>> constraintsToEvaluate) {
    if (constraintsToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
    }

    return constraintsToEvaluate.iterator().next();
  }

  Map<String, Set<Constraint>> getSinksToConstraints() {
    return sinksToConstraints;
  }

  @VisibleForTesting
  void addSinksToConstraints(Map<String, Set<Constraint>> sinksToConstraints) {
    this.sinksToConstraints.putAll(sinksToConstraints);
  }
}
