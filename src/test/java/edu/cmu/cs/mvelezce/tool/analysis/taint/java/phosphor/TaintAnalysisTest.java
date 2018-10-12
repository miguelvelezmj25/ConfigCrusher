package edu.cmu.cs.mvelezce.tool.analysis.taint.java.phosphor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

public class TaintAnalysisTest {

  @Test(expected = RuntimeException.class)
  public void completeConfig_forEmptyConstraint() {
    Map<String, Boolean> emptyConstraintToEvaluate = new HashMap<>();

    TaintAnalysis.completeConfig(emptyConstraintToEvaluate);
  }

  @Test
  public void completeConfig_forConstraint_NotA_NotB() {
    Map<String, Boolean> constraintToEvaluate = this.buildConstraint_notA_notB();

    Set<String> config = TaintAnalysis.completeConfig(constraintToEvaluate);
    Assert.assertTrue(config.isEmpty());
  }

  @Test
  public void completeConfig_forConstraint_A_notB() {
    Map<String, Boolean> constraintToEvaluate = this.buildConstraint_A_notB();

    Set<String> expectedConfig = new HashSet<>();
    expectedConfig.add("A");

    Set<String> config = TaintAnalysis.completeConfig(constraintToEvaluate);
    Assert.assertEquals(expectedConfig, config);
  }

  @Test
  public void buildConfiguration_forConstraint_notA_B() {
    Map<String, Boolean> constraint_notA_b = this.buildConstraint_notA_B();

    Set<Map<String, Boolean>> constraintsToEvaluate = new HashSet<>();
    constraintsToEvaluate.add(constraint_notA_b);

    Set<String> expectedConfig = new HashSet<>();
    expectedConfig.add("B");

    Pair<Map<String, Boolean>, Set<String>> results = TaintAnalysis
        .buildConfiguration(constraintsToEvaluate);

    Assert.assertEquals(constraint_notA_b, results.getLeft());
    Assert.assertEquals(expectedConfig, results.getRight());
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