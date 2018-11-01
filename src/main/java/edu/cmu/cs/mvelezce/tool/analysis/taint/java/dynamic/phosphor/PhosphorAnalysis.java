package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.cc.TaintLabel;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;

public class PhosphorAnalysis extends BaseDynamicAnalysis<Set<Constraint>> {

  private static final String PHOSPHOR_OUTPUT_DIR =
      BaseAdapter.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";
  private static final String PHOSPHOR_SCRIPTS_DIR = BaseAdapter.USER_HOME
      + "/Documents/Programming/Java/Projects/phosphor/Phosphor/scripts/run-instrumented/implicit-optimized";

  private final Map<String, Set<Constraint>> sinksToConstraints = new HashMap<>();

  public PhosphorAnalysis(String programName) {
    this(programName, new HashSet<>(), new HashSet<>());
  }

  PhosphorAnalysis(String programName, Set<String> options, Set<String> initialConfig) {
    super(programName, options, initialConfig);
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> analyze() throws IOException, InterruptedException {
    this.runDynamicAnalysis();

    Map<JavaRegion, Set<Constraint>> regionsToConstraints = new HashMap<>();

    for (Map.Entry<String, Set<Constraint>> entry : this.sinksToConstraints.entrySet()) {
      String sink = entry.getKey();
      JavaRegion region = new JavaRegion.Builder(this.getPackageName(sink), this.getClassName(sink),
          this.getMethodSignature(sink)).startBytecodeIndex(this.getDecisionOrder(sink)).build();

      regionsToConstraints.put(region, entry.getValue());
    }

    return regionsToConstraints;
  }

  @Override
  public Map<JavaRegion, Set<Constraint>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo<Set<Constraint>>> results = mapper
        .readValue(file, new TypeReference<List<RegionToInfo<Set<Constraint>>>>() {
        });

    Map<JavaRegion, Set<Constraint>> regionsToConstraints = new HashMap<>();

    for (RegionToInfo<Set<Constraint>> result : results) {
      regionsToConstraints.put(result.getRegion(), new HashSet<>(result.getInfo()));
    }

    return regionsToConstraints;
  }

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/cc/";
  }

  /**
   * Input: P, c in C, O
   *
   * Output: SC: S —> P(CT)
   *
   * Input: The program is provided elsewhere. Therefore, there is no need to pass the program to
   * this method.
   */
  protected void runDynamicAnalysis() throws IOException, InterruptedException {
    // TODO add check to be sure that we are not sampling a constraint that we already sample

    Set<Constraint> exploredConstraints = new HashSet<>();
    Set<Constraint> constraintsToExplore = new HashSet<>();
    // CE := to_constraint(c)
    Set<String> options = this.getOptions();
    Map<String, Boolean> initialConfigAsConfigWithValues = Constraint
        .toConfigWithValues(this.getInitialConfig(), options);
    constraintsToExplore.add(new Constraint(initialConfigAsConfigWithValues));

    int count = 0;

    while (!constraintsToExplore.isEmpty()) {
      // CTE := get_next_constraint(CE,O)
      Constraint currentConstraint = PhosphorAnalysis.getNextConstraint(constraintsToExplore);
      // c:= to_config(CTE)
      Set<String> config = currentConstraint.getConstraintAsPartialConfig();
      Map<String, Boolean> configWithValues = Constraint.toConfigWithValues(config, options);
      currentConstraint = new Constraint(configWithValues);

      // CE.removeAll(CTE)
      PhosphorAnalysis.removeAllSubConstraints(constraintsToExplore, currentConstraint);
      // EC.addAll(CTE)
      exploredConstraints.add(currentConstraint);

      // ST := run_taint_analysis(P’, c)
      this.runPhosphorAnalysis(config);
      Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults = this
          .analyzePhosphorResults();

      // CFA := get_constraints_from_analysis(ST)
      Set<Constraint> constraintsFromAnalysis = this
          .getConstraintsFromAnalysis(sinksToTaintsResults, config);

      // CFA.removeAll(EC)
      PhosphorAnalysis.removeAllSubConstraints(constraintsFromAnalysis, exploredConstraints);
      // CE.addAll(CC)
      constraintsToExplore.addAll(constraintsFromAnalysis);

      count++;
    }

    System.out.println(count);
//    // TODO this might be done in the compression step, not in the analysis
//    this.getConfigsForCC();
  }

  static void removeAllSubConstraints(Set<Constraint> constraintsFromAnalysis,
      Set<Constraint> exploredConstraints) {
    for (Constraint explored : exploredConstraints) {
      removeAllSubConstraints(constraintsFromAnalysis, explored);
    }
  }

  /**
   * Removes the subconstraints of the passed constraint from passes constraints set.
   */
  static void removeAllSubConstraints(Set<Constraint> constraints, Constraint constraint) {
    constraints.removeIf(currentConstraint -> currentConstraint.isSubsetOf(constraint));
  }

  private int getDecisionOrder(String sink) {
    int indexOfLastDot = sink.lastIndexOf(".");

    return Integer.valueOf(sink.substring(indexOfLastDot + 1));
  }

  private String getMethodSignature(String sink) {
    int indexOfFirstDot = sink.indexOf(".");
    int indexOfLastDot = sink.lastIndexOf(".");

    return sink.substring(indexOfFirstDot + 1, indexOfLastDot);
  }

  private String getClassName(String sink) {
    int indexOfDot = sink.indexOf(".");
    String packageAndClass = sink.substring(0, indexOfDot);
    int indexOfLastSlash = packageAndClass.lastIndexOf("/");

    return packageAndClass.substring(indexOfLastSlash + 1);
  }

  private String getPackageName(String sink) {
    int indexOfDot = sink.indexOf(".");
    String packageAndClass = sink.substring(0, indexOfDot);
    int indexOfLastSlash = packageAndClass.lastIndexOf("/");
    String packageName = packageAndClass.substring(0, indexOfLastSlash);

    return packageName.replace("/", ".");
  }

  /**
   * Input: P', c in C
   *
   * Output: ST: S --> (P(O), P(O))
   *
   * Input: There is a script that this method calls to executed the instrumented program.
   * Therefore, we do not not pass the instrumented program to this method.
   *
   * Output: This method only runs the phosphor analysis. There is another method that processes the
   * results from the analysis.
   */
  void runPhosphorAnalysis(Set<String> config) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();

    List<String> commandList = this.buildCommandAsList(config);
    builder.command(commandList);
    builder.directory(new File(PHOSPHOR_SCRIPTS_DIR));
    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }

  private List<String> buildCommandAsList(Set<String> config) {
    List<String> commandList = new ArrayList<>();

    String programName = this.getProgramName();
    Adapter adapter;

    switch (programName) {
      case DynamicRunningExampleAdapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new DynamicRunningExampleAdapter();
        break;
      case PhosphorExample2Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample2Adapter();
        break;
      case PhosphorExample3Adapter.PROGRAM_NAME:
        commandList.add("./examples.sh");
        adapter = new PhosphorExample3Adapter();
        break;
      default:
        throw new RuntimeException("Could not find a phosphor script to run " + programName);
    }

    String[] configArgs = adapter.configurationAsMainArguments(config);
    List<String> configList = Arrays.asList(configArgs);
    commandList.addAll(configList);
    commandList.add(adapter.getMainClass());

    return commandList;
  }

  /**
   * Input: P', c in C
   *
   * Output: ST: S --> (P(O), P(O))
   *
   * Helper method for running the phosphor analysis. This method processes the results of the
   * analysis and returns the output specified in the algorithm.
   */
  protected Pair<Map<String, Set<String>>, Map<String, Set<String>>> analyzePhosphorResults()
      throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + this.getProgramName();
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 2) {
      throw new RuntimeException("The directory " + dir + " does not have 2 files.");
    }

    return this.readPhosphorTaintResults(serializedFiles);
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

  // TODO check catching and throwing
  private Map<String, Set<TaintLabel>> deserialize(File file) throws IOException {
    FileInputStream fis = new FileInputStream(file);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Map<String, Set<TaintLabel>> sinksToTaints;

    try {
      sinksToTaints = (Map<String, Set<TaintLabel>>) ois.readObject();
    }
    catch (ClassNotFoundException e) {
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
   * Input: ST: S --> (P(O), P(O)), c ∈ C
   *
   * Output: CFA: P(CT)
   *
   * Calculate the constraints from running the dynamic analysis
   */
  Set<Constraint> getConstraintsFromAnalysis(
      Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults,
      Set<String> config) {
    Map<String, Set<String>> sinksToTaintsFromTaints = sinksToTaintsResults.getLeft();
    Map<String, Set<String>> sinksToTaintsFromContext = sinksToTaintsResults.getRight();

    if (sinksToTaintsFromTaints == null || sinksToTaintsFromContext == null) {
      throw new IllegalArgumentException("The sinks to taints result cannot be empty");
    }

    Set<Constraint> constraintsFromAnalysis = new HashSet<>();

    Set<String> executedSinks = new HashSet<>(sinksToTaintsFromTaints.keySet());
    executedSinks.addAll(sinksToTaintsFromContext.keySet());
    this.addNewSinks(executedSinks);

    for (String sink : executedSinks) {
      Set<Constraint> constraintsAtSink = this
          .getConstraintsAtSink(sinksToTaintsFromTaints.get(sink),
              sinksToTaintsFromContext.get(sink),
              config);

      constraintsFromAnalysis.addAll(constraintsAtSink);

      Set<Constraint> oldConstraints = this.sinksToConstraints.get(sink);
      oldConstraints.addAll(constraintsAtSink);
      this.sinksToConstraints.put(sink, oldConstraints);
    }

    return constraintsFromAnalysis;
  }

  /**
   * Input: stv ∈ ST.values, c ∈ C
   *
   * Output: CS: P(CFA) // CFA: P(CT)
   *
   * Calculate the constraints at a sink
   */
  private Set<Constraint> getConstraintsAtSink(@Nullable Set<String> taintsFromTaint,
      @Nullable Set<String> taintsFromContext, Set<String> config) {
    Set<Constraint> constraints = new HashSet<>();

    Set<Map<String, Boolean>> partialConfigs = Constraint.buildPartialConfigs(taintsFromTaint);
    Map<String, Boolean> context = Constraint.buildContext(taintsFromContext, config);

    if (partialConfigs.isEmpty()) {
      partialConfigs.add(new HashMap<>());
    }

    for (Map<String, Boolean> partialConfig : partialConfigs) {
      constraints.add(new Constraint(partialConfig, context));
    }

    PhosphorAnalysis.removeInvalidConstraints(constraints);

    return constraints;
  }

  static void removeInvalidConstraints(Set<Constraint> constraints) {
    constraints.removeIf(constraint -> !constraint.isValid());
  }

  private void addNewSinks(Set<String> sinks) {
    for (String sink : sinks) {
      if (!this.sinksToConstraints.containsKey(sink)) {
        this.sinksToConstraints.put(sink, new HashSet<>());
      }
    }
  }

  /**
   * Input: CE, O
   *
   * Output: CTE: P(CE)
   *
   * Input: since we represent options set to false by not including them in the set that represents
   * configurations, there is no need to pass them in the method.
   *
   * Example: config = {A, C} means that the configurations is A=T, B=F, C=T.
   */
  static Constraint getNextConstraint(Set<Constraint> constraintsToEvaluate) {
    if (constraintsToEvaluate.isEmpty()) {
      throw new IllegalArgumentException("The constraints to evaluate cannot be empty");
    }

    if (constraintsToEvaluate.size() == 1) {
      return constraintsToEvaluate.iterator().next();
    }

    // TODO check results if picking the longest constraint first
    Set<Map<String, Boolean>> completeConstraints = getCompleteConstraints(constraintsToEvaluate);
    Iterator<Map<String, Boolean>> iter = completeConstraints.iterator();
    Map<String, Boolean> finalConstraintAsConfigWithValues = new HashMap<>(iter.next());

    while (iter.hasNext()) {
      Map<String, Boolean> currentConstraintAsConfigWithValues = iter.next();
      Set<String> pivotOptions = getPivotOptions(finalConstraintAsConfigWithValues,
          currentConstraintAsConfigWithValues);

      if (pivotOptions.isEmpty()) {
        finalConstraintAsConfigWithValues.putAll(currentConstraintAsConfigWithValues);
      }
      else {
        Map<String, Boolean> finalConstraintPivotValues = getPivotValues(
            finalConstraintAsConfigWithValues, pivotOptions);
        Map<String, Boolean> currentConstraintPivotValues = getPivotValues(
            currentConstraintAsConfigWithValues, pivotOptions);

        if (!finalConstraintPivotValues.equals(currentConstraintPivotValues)) {
          // Could not merge the constraints
          continue;
        }

        finalConstraintAsConfigWithValues.putAll(currentConstraintAsConfigWithValues);
      }
    }

    // TODO check if the constraint we picked is NOT a proper subset of a set left in the constraints set
    return new Constraint(finalConstraintAsConfigWithValues);
  }

  private static Map<String, Boolean> getPivotValues(
      Map<String, Boolean> constraintAsConfigWithValues, Set<String> pivotOptions) {
    Map<String, Boolean> pivotValues = new HashMap<>();

    for (String option : pivotOptions) {
      pivotValues.put(option, constraintAsConfigWithValues.get(option));
    }

    return pivotValues;
  }

  private static Set<String> getPivotOptions(Map<String, Boolean> configWaithValues1,
      Map<String, Boolean> configWaithValues2) {
    Set<String> pivotOptions = new HashSet<>(configWaithValues1.keySet());
    pivotOptions.retainAll(configWaithValues2.keySet());

    return pivotOptions;
  }

  private static Set<Map<String, Boolean>> getCompleteConstraints(Set<Constraint> constraints) {
    Set<Map<String, Boolean>> completeConstraints = new HashSet<>();

    for (Constraint constraint : constraints) {
      completeConstraints.add(constraint.getCompleteConstraint());
    }

    return completeConstraints;
  }

//  Set<Set<String>> getConfigsForCC() {
//    Set<Constraint> ccConstraints = this.getAllConstraints();
//    Set<Set<String>> configs = new HashSet<>();
//
//    for (Constraint ccConstraint : ccConstraints) {
//      Set<String> config = Constraint.toPartialCCConfig(ccConstraint);
//      configs.add(config);
//    }
//
//    System.out.println(configs);
//    return configs;
//  }

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


}
