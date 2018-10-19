package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleMain;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

public class PhosphorAnalysisTest {

//  @Test
//  public void analyze_PhosphorRunningExample() throws IOException {
//    String programName = DynamicRunningExampleMain.PROGRAM_NAME;
//
//    String[] args = new String[0];
//
//    DynamicAnalysis analysis = new PhosphorAnalysis(programName);
//    analysis.analyze(args);
//  }

  @Test(expected = IllegalArgumentException.class)
  public void getNextConstraint_forEmptyConstraint() {
    Set<Constraint> emptyConstraints = new HashSet<>();

    PhosphorAnalysis.getNextConstraint(emptyConstraints);
  }

  @Test
  public void getNextConstraint_forConstraint_notA_B() {
    Map<String, Boolean> partialConfig_notA_b = ConstraintTest.buildPartialConfig_notA_B();
    Constraint expectedConstraint = new Constraint(partialConfig_notA_b);

    Set<Constraint> constraints = new HashSet<>();
    constraints.add(expectedConstraint);

    Constraint nextConstraint = PhosphorAnalysis.getNextConstraint(constraints);

    Assert.assertEquals(expectedConstraint, nextConstraint);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildPartialConfigs_forEmptyTaintsAtSink() {
    Set<String> emptyTaintsAtSink = new HashSet<>();

    PhosphorAnalysis.buildPartialConfigs(emptyTaintsAtSink);
  }

  @Test
  public void buildPartialConfigs_forTaints_A_B() {
    Map<String, Boolean> partialConfig_notA_notB = ConstraintTest.buildPartialConfig_notA_notB();
    Map<String, Boolean> partialConfig_notA_B = ConstraintTest.buildPartialConfig_notA_B();
    Map<String, Boolean> partialConfig_A_notB = ConstraintTest.buildPartialConfig_A_notB();
    Map<String, Boolean> partialConfig_A_B = ConstraintTest.buildPartialConfig_A_B();

    HashSet<Map<String, Boolean>> expectedConstraints = new HashSet<>();
    expectedConstraints.add(partialConfig_notA_notB);
    expectedConstraints.add(partialConfig_notA_B);
    expectedConstraints.add(partialConfig_A_notB);
    expectedConstraints.add(partialConfig_A_B);

    Set<String> taintsAtSink = new HashSet<>();
    taintsAtSink.add("A");
    taintsAtSink.add("B");

    Set<Map<String, Boolean>> constraints = PhosphorAnalysis.buildPartialConfigs(taintsAtSink);

    Assert.assertEquals(expectedConstraints, constraints);
  }

//  @Test
//  public void getExploredConstraints_forExplored_notA_notB() {
//    Map<String, Boolean> constraint_notA_notB = ConstraintTest.buildPartialConfig_notA_notB();
//    Map<String, Boolean> constraint_notA_B = ConstraintTest.buildPartialConfig_notA_B();
//    Map<String, Boolean> constraint_A_notB = ConstraintTest.buildPartialConfig_A_notB();
//    Map<String, Boolean> constraint_A_B = ConstraintTest.buildPartialConfig_A_B();
//    Map<String, Boolean> constraint_notA = ConstraintTest.buildPartialConfig_notA();
//    Map<String, Boolean> constraint_A = ConstraintTest.buildPartialConfig_A();
//
//    Set<Map<String, Boolean>> currentConstraints = new HashSet<>();
//    currentConstraints.add(constraint_notA_notB);
//    currentConstraints.add(constraint_notA_B);
//    currentConstraints.add(constraint_A_notB);
//    currentConstraints.add(constraint_A_B);
//    currentConstraints.add(constraint_notA);
//    currentConstraints.add(constraint_A);
//
//    Set<Map<String, Boolean>> exploredConstraints = new HashSet<>();
//    exploredConstraints.add(constraint_notA_notB);
//
//    Set<Map<String, Boolean>> currentExploredConstraints = PhosphorAnalysis
//        .getExploredConstraints(currentConstraints, exploredConstraints);
//
//    Assert.assertEquals(2, currentExploredConstraints.size());
//    Assert.assertTrue(currentConstraints.contains(constraint_notA));
//    Assert.assertTrue(currentConstraints.contains(constraint_notA_notB));
//  }

  @Test
  public void dynamicRunningExample() throws IOException, InterruptedException {
    Set<String> initialConfig = new HashSet<>();

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    String programName = DynamicRunningExampleMain.PROGRAM_NAME;
    PhosphorAnalysis analysis = new PhosphorAnalysis(programName);
    analysis.dynamicAnalysis(initialConfig, options);
  }

//  @Test
//  public void getAllCombinationsOfPartialConfigs_for3PartialConfigs() {
//    Map<String, Boolean> partialConfig_A_notB = ConstraintTest.buildPartialConfig_A_notB();
//    Map<String, Boolean> partialConfig_A = ConstraintTest.buildPartialConfig_A();
//    Map<String, Boolean> partialConfig_notB = ConstraintTest.buildPartialConfig_notB();
//
//    Set<Map<String, Boolean>> expectedPartialConfigs = new HashSet<>();
//    expectedPartialConfigs.add(partialConfig_A_notB);
//    expectedPartialConfigs.add(partialConfig_A);
//    expectedPartialConfigs.add(partialConfig_notB);
//
//    Set<Map<String, Boolean>> exploringConstraints = PhosphorAnalysis
//        .getAllCombinationsOfPartialConfigs(partialConfig_A_notB);
//
//    Assert.assertEquals(3, exploringConstraints.size());
//    Assert.assertEquals(expectedPartialConfigs, exploringConstraints);
//  }

  @Test
  public void runPhosphorAnalysis_forDynamicRunningExample()
      throws IOException, InterruptedException {
    String programName = DynamicRunningExampleMain.PROGRAM_NAME;
    PhosphorAnalysis analysis = new PhosphorAnalysis(programName);
    Set<String> config = new HashSet<>();

    analysis.runPhosphorAnalysis(config);
  }

  @Test
  public void getConstraintsFromAnalysis_for2Sinks() {
    String sink0 = "0";
    String sink1 = "1";
    String A = "A";
    String B = "B";

    Set<String> config = new HashSet<>();
    config.add(A);

    Map<String, Set<String>> sinksToTaints = new HashMap<>();
    Map<String, Set<String>> sinksToContexts = new HashMap<>();

    Set<String> taints0 = new HashSet<>();
    taints0.add(A);
    sinksToTaints.put(sink0, taints0);

    Set<String> context0 = new HashSet<>();
    sinksToContexts.put(sink0, context0);

    Set<String> taints1 = new HashSet<>();
    taints1.add(A);
    taints1.add(B);
    sinksToTaints.put(sink1, taints1);

    Set<String> context1 = new HashSet<>();
    context1.add(A);
    sinksToContexts.put(sink1, context1);

    Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults = Pair
        .of(sinksToTaints, sinksToContexts);

    PhosphorAnalysis analysis = new PhosphorAnalysis(DynamicRunningExampleMain.PROGRAM_NAME);
    Set<Constraint> constraintsFromAnalysis = analysis
        .getConstraintsFromAnalysis(sinksToTaintsResults, config);

    Assert.assertEquals(4, constraintsFromAnalysis.size());
  }

//  @Test
//  public void getConfigsForCC_for2Sinks() {
//    String sink0 = "0";
//    String sink1 = "1";
//
//    Map<String, Set<Map<String, Boolean>>> sinksToConstraints = new HashMap<>();
//
//    Set<Map<String, Boolean>> constraintsSink0 = new HashSet<>();
//    Map<String, Boolean> constraint_notA = ConstraintTest.buildPartialConfig_notA();
//    Map<String, Boolean> constraint_A = ConstraintTest.buildPartialConfig_A();
//    constraintsSink0.add(constraint_notA);
//    constraintsSink0.add(constraint_A);
//    sinksToConstraints.put(sink0, constraintsSink0);
//
//    Set<Map<String, Boolean>> constraintsSink1 = new HashSet<>();
//    Map<String, Boolean> constraint_notA_notB = ConstraintTest.buildPartialConfig_notA_notB();
//    Map<String, Boolean> constraint_notA_B = ConstraintTest.buildPartialConfig_notA_B();
//    Map<String, Boolean> constraint_A_notB = ConstraintTest.buildPartialConfig_A_notB();
//    Map<String, Boolean> constraint_A_B = ConstraintTest.buildPartialConfig_A_B();
//    constraintsSink1.add(constraint_notA_notB);
//    constraintsSink1.add(constraint_notA_B);
//    constraintsSink1.add(constraint_A_notB);
//    constraintsSink1.add(constraint_A_B);
//    sinksToConstraints.put(sink1, constraintsSink1);
//
//    PhosphorAnalysis analysis = new PhosphorAnalysis(DynamicRunningExampleMain.PROGRAM_NAME);
//    analysis.addSinksToConstraints(sinksToConstraints);
//
//    Set<Set<String>> expectedConfigs = new HashSet<>();
//    expectedConfigs.add(buildConfig_notA_notB());
//    expectedConfigs.add(buildConfig_notA_B());
//    expectedConfigs.add(buildConfig_A_notB());
//    expectedConfigs.add(buildConfig_A_B());
//
//    Set<Set<String>> ccConfigs = analysis.getConfigsForCC();
//
//    Assert.assertEquals(expectedConfigs, ccConfigs);
//  }

  @Test
  public void removeInvalidConstraints_forNoInvalidConstraint() {
    Constraint constraint0 = new Constraint(ConstraintTest.buildPartialConfig_A());
    Constraint constraint1 = new Constraint(ConstraintTest.buildPartialConfig_A(),
        ConstraintTest.buildPartialConfig_A_notB());
    Constraint constraint2 = new Constraint(ConstraintTest.buildPartialConfig_notA(),
        ConstraintTest.buildPartialConfig_notA());

    Set<Constraint> constrains = new HashSet<>();
    constrains.add(constraint0);
    constrains.add(constraint1);
    constrains.add(constraint2);

    Set<Constraint> expectedConstraints = new HashSet<>(constrains);

    PhosphorAnalysis.removeInvalidConstraints(constrains);

    Assert.assertEquals(expectedConstraints, constrains);
  }

  @Test
  public void removeInvalidConstraints_forInvalidConstraint() {
    Constraint constraint0 = new Constraint(ConstraintTest.buildPartialConfig_A());
    Constraint constraint1 = new Constraint(ConstraintTest.buildPartialConfig_A(),
        ConstraintTest.buildPartialConfig_notA_B());
    Constraint constraint2 = new Constraint(ConstraintTest.buildPartialConfig_notA(),
        ConstraintTest.buildPartialConfig_A());

    Set<Constraint> constrains = new HashSet<>();
    constrains.add(constraint0);
    constrains.add(constraint1);
    constrains.add(constraint2);

    Set<Constraint> expectedConstraints = new HashSet<>();
    expectedConstraints.add(constraint0);

    PhosphorAnalysis.removeInvalidConstraints(constrains);

    Assert.assertEquals(expectedConstraints, constrains);
  }

  @Test
  public void rremoveAllSubConstraints_forRemovingSubConstraints() {
    Map<String, Boolean> partialConfig_notA_notB = ConstraintTest.buildPartialConfig_notA_notB();
    Map<String, Boolean> partialConfig_notA = ConstraintTest.buildPartialConfig_notA();
    Map<String, Boolean> partialConfig_notB = ConstraintTest.buildPartialConfig_notB();
    Map<String, Boolean> partialConfig_A_B = ConstraintTest.buildPartialConfig_A_B();

    Constraint constraint_notA_notB = new Constraint(partialConfig_notA_notB);
    Constraint constraint_notB = new Constraint(partialConfig_notB);
    Constraint constraint_notA = new Constraint(partialConfig_notA);
    Constraint constraint_A_B = new Constraint(partialConfig_A_B);

    Set<Constraint> constraints = new HashSet<>();
    constraints.add(constraint_notA_notB);
    constraints.add(constraint_notA);
    constraints.add(constraint_notB);
    constraints.add(constraint_A_B);

    PhosphorAnalysis.removeAllSubConstraints(constraint_notA_notB, constraints);

    Assert.assertEquals(1, constraints.size());
    Assert.assertTrue(constraints.contains(constraint_A_B));
  }

  @Test
  public void rremoveAllSubConstraints_forRemoveAll() {
    Map<String, Boolean> partialConfig_notA_notB = ConstraintTest.buildPartialConfig_notA_notB();
    Map<String, Boolean> partialConfig_notA = ConstraintTest.buildPartialConfig_notA();
    Map<String, Boolean> partialConfig_notB = ConstraintTest.buildPartialConfig_notB();

    Constraint constraint_notA_notB = new Constraint(partialConfig_notA_notB);
    Constraint constraint_notB = new Constraint(partialConfig_notB);
    Constraint constraint_notA = new Constraint(partialConfig_notA);

    Set<Constraint> constraints = new HashSet<>();
    constraints.add(constraint_notA_notB);
    constraints.add(constraint_notA);
    constraints.add(constraint_notB);

    PhosphorAnalysis.removeAllSubConstraints(constraint_notA_notB, constraints);

    Assert.assertTrue(constraints.isEmpty());
  }

//  private Set<String> buildConfig_A_B() {
//    Set<String> config = new HashSet<>();
//    config.add("A");
//    config.add("B");
//
//    return config;
//  }
//
//  private Set<String> buildConfig_A_notB() {
//    Set<String> config = new HashSet<>();
//    config.add("A");
//
//    return config;
//  }
//
//  private Set<String> buildConfig_notA_B() {
//    Set<String> config = new HashSet<>();
//    config.add("B");
//
//    return config;
//  }
//
//  private Set<String> buildConfig_notA_notB() {
//    return new HashSet<>();
//  }
}