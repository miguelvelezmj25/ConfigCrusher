package edu.cmu.cs.mvelezce.tool.analysis.taint.java.phosphor;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

public class PhosphorAnalysisTest {

  @Test
  public void analyze_PhosphorRunningExample() throws IOException, ClassNotFoundException {
    String programName = "running-example";

    PhosphorAnalysis analysis = new PhosphorAnalysis(programName);
    analysis.analyze();
  }

  @Test(expected = IllegalArgumentException.class)
  public void completeConfig_forEmptyConstraint() {
    Map<String, Boolean> emptyConstraintToEvaluate = new HashMap<>();

    PhosphorAnalysis.completeConfig(emptyConstraintToEvaluate);
  }

  @Test
  public void completeConfig_forConstraint_NotA_NotB() {
    Map<String, Boolean> constraintToEvaluate = this.buildConstraint_notA_notB();

    Set<String> config = PhosphorAnalysis.completeConfig(constraintToEvaluate);
    Assert.assertTrue(config.isEmpty());
  }

  @Test
  public void completeConfig_forConstraint_A_notB() {
    Map<String, Boolean> constraintToEvaluate = this.buildConstraint_A_notB();

    Set<String> expectedConfig = new HashSet<>();
    expectedConfig.add("A");

    Set<String> config = PhosphorAnalysis.completeConfig(constraintToEvaluate);
    Assert.assertEquals(expectedConfig, config);
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildConfiguration_forEmptyConstraintToEvaluate() {
    Set<Map<String, Boolean>> emptyConstraintToEvaluate = new HashSet<>();

    PhosphorAnalysis.buildConfiguration(emptyConstraintToEvaluate);
  }

  @Test
  public void buildConfiguration_forConstraint_notA_B() {
    Map<String, Boolean> constraint_notA_b = this.buildConstraint_notA_B();

    Set<Map<String, Boolean>> constraintsToEvaluate = new HashSet<>();
    constraintsToEvaluate.add(constraint_notA_b);

    Set<String> expectedConfig = new HashSet<>();
    expectedConfig.add("B");

    Pair<Map<String, Boolean>, Set<String>> results = PhosphorAnalysis
        .buildConfiguration(constraintsToEvaluate);

    Assert.assertEquals(constraint_notA_b, results.getLeft());
    Assert.assertEquals(expectedConfig, results.getRight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void buildConstraints_forEmptyTaintsAtSink() {
    Set<String> emptyTaintsAtSink = new HashSet<>();

    PhosphorAnalysis.buildConstraints(emptyTaintsAtSink);
  }

  @Test
  public void buildConstraints_forTaints_A_B() {
    Map<String, Boolean> constraint_notA_notB = this.buildConstraint_notA_notB();
    Map<String, Boolean> constraint_notA_B = this.buildConstraint_notA_B();
    Map<String, Boolean> constraint_A_notB = this.buildConstraint_A_notB();
    Map<String, Boolean> constraint_A_B = this.buildConstraint_A_B();

    HashSet<Map<String, Boolean>> expectedConstraints = new HashSet<>();
    expectedConstraints.add(constraint_notA_notB);
    expectedConstraints.add(constraint_notA_B);
    expectedConstraints.add(constraint_A_notB);
    expectedConstraints.add(constraint_A_B);

    Set<String> taintsAtSink = new HashSet<>();
    taintsAtSink.add("A");
    taintsAtSink.add("B");

    Set<Map<String, Boolean>> constraints = PhosphorAnalysis.buildConstraints(taintsAtSink);

    Assert.assertEquals(expectedConstraints, constraints);
  }

  @Test(expected = IllegalArgumentException.class)
  public void calculateConstraints_forEmptyTaintsAtSinks() {
    Set<Set<String>> emptyTaintsAtSinks = new HashSet<>();

    PhosphorAnalysis.calculateConstraints(emptyTaintsAtSinks);
  }

  @Test
  public void calculateConstraints_forTaints_A_B() {
    Map<String, Boolean> constraint_notA_notB = this.buildConstraint_notA_notB();
    Map<String, Boolean> constraint_notA_B = this.buildConstraint_notA_B();
    Map<String, Boolean> constraint_A_notB = this.buildConstraint_A_notB();
    Map<String, Boolean> constraint_A_B = this.buildConstraint_A_B();

    HashSet<Map<String, Boolean>> expectedConstraints = new HashSet<>();
    expectedConstraints.add(constraint_notA_notB);
    expectedConstraints.add(constraint_notA_B);
    expectedConstraints.add(constraint_A_notB);
    expectedConstraints.add(constraint_A_B);

    Set<String> taintsAtSink = new HashSet<>();
    taintsAtSink.add("A");
    taintsAtSink.add("B");

    Set<Set<String>> taintsAtSinks = new HashSet<>();
    taintsAtSinks.add(taintsAtSink);

    Set<Map<String, Boolean>> constraints = PhosphorAnalysis.calculateConstraints(taintsAtSinks);

    Assert.assertEquals(expectedConstraints, constraints);
  }

  private Map<String, Boolean> buildConstraint_notA_notB() {
    Map<String, Boolean> constraint = new HashMap<>();
    constraint.put("A", false);
    constraint.put("B", false);

    return constraint;
  }

  private Map<String, Boolean> buildConstraint_notA_B() {
    Map<String, Boolean> constraint = new HashMap<>();
    constraint.put("A", false);
    constraint.put("B", true);

    return constraint;
  }

  private Map<String, Boolean> buildConstraint_A_notB() {
    Map<String, Boolean> constraint = new HashMap<>();
    constraint.put("A", true);
    constraint.put("B", false);

    return constraint;
  }

  private Map<String, Boolean> buildConstraint_A_B() {
    Map<String, Boolean> constraint = new HashMap<>();
    constraint.put("A", true);
    constraint.put("B", true);

    return constraint;
  }

}