package edu.cmu.cs.mvelezce.tool.analysis.taint.java.phosphor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class TaintAnalysisTest {

  @Test(expected = RuntimeException.class)
  public void completeConfig_forEmptyConstraint() {
    Map<String, Boolean> emptyConstraintToEvaluate = new HashMap<>();

    TaintAnalysis.completeConfig(emptyConstraintToEvaluate);
  }

  @Test
  public void completeConfig_forConstraintsWithFalse() {
    Map<String, Boolean> constraintToEvaluate = new HashMap<>();
    constraintToEvaluate.put("A", false);
    constraintToEvaluate.put("B", false);

    Set<String> config = TaintAnalysis.completeConfig(constraintToEvaluate);
    Assert.assertTrue(config.isEmpty());
  }

  @Test
  public void completeConfig_forConstraintsWithTrue() {
    Map<String, Boolean> constraintToEvaluate = new HashMap<>();
    constraintToEvaluate.put("A", true);
    constraintToEvaluate.put("B", false);

    Set<String> expectedConfig = new HashSet<>();
    expectedConfig.add("A");

    Set<String> config = TaintAnalysis.completeConfig(constraintToEvaluate);
    Assert.assertEquals(expectedConfig, config);
  }
}