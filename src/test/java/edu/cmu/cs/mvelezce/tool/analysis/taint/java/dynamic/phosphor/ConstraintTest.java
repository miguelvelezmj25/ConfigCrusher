package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class ConstraintTest {

  @Test(expected = IllegalArgumentException.class)
  public void toConfig_forEmptyConstraint() {
    Map<String, Boolean> emptyPartialConfig = new HashMap<>();

    Constraint.toConfig(emptyPartialConfig);
  }

  @Test
  public void toConfig_forConstraint_NotA_NotB() {
    Map<String, Boolean> partialConfig = ConstraintTest.buildPartialConfig_notA_notB();

    Set<String> config = Constraint.toConfig(partialConfig);
    Assert.assertTrue(config.isEmpty());
  }

  @Test
  public void toConfig_forConstraint_A_notB() {
    Map<String, Boolean> partialConfig = ConstraintTest.buildPartialConfig_A_notB();

    Set<String> expectedConfig = new HashSet<>();
    expectedConfig.add("A");

    Set<String> config = Constraint.toConfig(partialConfig);
    Assert.assertEquals(expectedConfig, config);
  }

  @Test
  public void toConstraint_forAllOptionsFalse() {
    Map<String, Boolean> expectedConstraint = ConstraintTest.buildPartialConfig_notA_notB();

    Set<String> config = new HashSet<>();

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Map<String, Boolean> constraint = Constraint.toConstraint(config, options);

    Assert.assertEquals(expectedConstraint, constraint);
  }

  @Test
  public void toConstraint_forAllOptionsTrue() {
    Map<String, Boolean> expectedConstraint = ConstraintTest.buildPartialConfig_A_B();

    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("B");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Map<String, Boolean> constraint = Constraint.toConstraint(config, options);

    Assert.assertEquals(expectedConstraint, constraint);
  }

  @Test
  public void isValid_forValidConstraint_ContextNotInPartialConfig() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_B();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forValidConstraint_ContextInPartialConfig() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A_notB();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_notB();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forValidConstraint_TrueContext() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A_notB();
    Constraint constraint = new Constraint(partialConfig);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forValidConstraint_SamePartialConfigAndContext() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A_notB();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_A_notB();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forInvalidConstraint() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_notA();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertFalse(constraint.isValid());
  }

  static Map<String, Boolean> buildPartialConfig_B() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("B", true);

    return partialConfig;
  }

  static Map<String, Boolean> buildPartialConfig_notB() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("B", false);

    return partialConfig;
  }

  static Map<String, Boolean> buildPartialConfig_notA() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("A", false);

    return partialConfig;
  }

  static Map<String, Boolean> buildPartialConfig_A() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("A", true);

    return partialConfig;
  }

  static Map<String, Boolean> buildPartialConfig_notA_notB() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("A", false);
    partialConfig.put("B", false);

    return partialConfig;
  }

  static Map<String, Boolean> buildPartialConfig_notA_B() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("A", false);
    partialConfig.put("B", true);

    return partialConfig;
  }

  static Map<String, Boolean> buildPartialConfig_A_notB() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("A", true);
    partialConfig.put("B", false);

    return partialConfig;
  }

  static Map<String, Boolean> buildPartialConfig_A_B() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("A", true);
    partialConfig.put("B", true);

    return partialConfig;
  }

}