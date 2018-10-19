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
  public void isValid_forTrueValidConstraint_ContextNotInPartialConfig() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_B();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forTrueValidConstraint_ContextInPartialConfig() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A_notB();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_notB();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forTrueValidConstraint_TrueContext() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A_notB();
    Constraint constraint = new Constraint(partialConfig);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forTrueValidConstraint_SamePartialConfigAndContext() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A_notB();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_A_notB();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertTrue(constraint.isValid());
  }

  @Test
  public void isValid_forFalseInvalidConstraint0() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_notA();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertFalse(constraint.isValid());
  }

  @Test
  public void isValid_forFalseInvalidConstraint1() {
    Map<String, Boolean> partialConfig = buildPartialConfig_A_notB();
    Map<String, Boolean> context = ConstraintTest.buildPartialConfig_notA_notB();
    Constraint constraint = new Constraint(partialConfig, context);

    Assert.assertFalse(constraint.isValid());
  }

  @Test
  public void isSubsetOf_forTrueEqualConstraints() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A());
    Constraint constraint1 = new Constraint(buildPartialConfig_A());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
    Assert.assertTrue(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forTrueEqualPartialConfigsEqualCtxs() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A(), buildPartialConfig_A());
    Constraint constraint1 = new Constraint(buildPartialConfig_A(), buildPartialConfig_A());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
    Assert.assertTrue(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forTrueDiffPartialConfigsEqualCtxs() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A(), buildPartialConfig_A());
    Constraint constraint1 = new Constraint(buildPartialConfig_A_B(), buildPartialConfig_A());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
  }

  @Test
  public void isSubsetOf_forTrueDiffCtx0() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A(), buildPartialConfig_A());
    Constraint constraint1 = new Constraint(buildPartialConfig_A_B(), buildPartialConfig_A_B());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
  }

  @Test
  public void isSubsetOf_forTrueDiffCtx1() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A(), buildPartialConfig_A());
    Constraint constraint1 = new Constraint(buildPartialConfig_A_B(), buildPartialConfig_B());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
  }

  @Test
  public void isSubsetOf_forTrueDiffPartialConfigsAndCtxs() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A(), buildPartialConfig_B());
    Constraint constraint1 = new Constraint(buildPartialConfig_A_B());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
    Assert.assertTrue(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forTrueDiffConstraints() {
    Constraint constraint0 = new Constraint(buildPartialConfig_B());
    Constraint constraint1 = new Constraint(buildPartialConfig_A(), buildPartialConfig_B());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
  }

  @Test
  public void isSubsetOf_forFalseDiffPartialConfigs0() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A());
    Constraint constraint1 = new Constraint(buildPartialConfig_notA());

    Assert.assertFalse(constraint0.isSubsetOf(constraint1));
    Assert.assertFalse(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forFalseEqualPartialConfigsDiffCtxs0() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A());
    Constraint constraint1 = new Constraint(buildPartialConfig_A(), buildPartialConfig_B());

    Assert.assertFalse(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forFalseDiffCtxs() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A(), buildPartialConfig_notC());
    Constraint constraint1 = new Constraint(buildPartialConfig_A_notB(), buildPartialConfig_notB());

    Assert.assertFalse(constraint0.isSubsetOf(constraint1));
    Assert.assertFalse(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forFalseEqualPartialConfigsDiffCtxs1() {
    Constraint constraint0 = new Constraint(buildPartialConfig_A(), buildPartialConfig_notB());
    Constraint constraint1 = new Constraint(buildPartialConfig_A(), buildPartialConfig_notC());

    Assert.assertFalse(constraint0.isSubsetOf(constraint1));
    Assert.assertFalse(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forFalseDiffPartialConfigs1() {
    Constraint constraint0 = new Constraint(buildPartialConfig_notB(), buildPartialConfig_notA());
    Constraint constraint1 = new Constraint(buildPartialConfig_notC(), buildPartialConfig_notA());

    Assert.assertFalse(constraint0.isSubsetOf(constraint1));
    Assert.assertFalse(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void toPartialConfig_forAllOptionsFalse() {
    Map<String, Boolean> expectedPartialConfig = ConstraintTest.buildPartialConfig_notA_notB();

    Set<String> config = new HashSet<>();

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Map<String, Boolean> partialConfig = Constraint.toPartialConfig(config, options);

    Assert.assertEquals(expectedPartialConfig, partialConfig);
  }

  @Test
  public void toPartialConfig_forAllOptionsTrue() {
    Map<String, Boolean> expectedPartialConfig = ConstraintTest.buildPartialConfig_A_B();

    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("B");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Map<String, Boolean> partialConfig = Constraint.toPartialConfig(config, options);

    Assert.assertEquals(expectedPartialConfig, partialConfig);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toPartialConfig_forInvalidArgs() {
    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("C");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Constraint.toPartialConfig(config, options);
  }

  @Test
  public void buildContext_forNullContextEmptyConfig() {
    Set<String> taintsFromContext = null;
    Set<String> config = new HashSet<>();

    Map<String, Boolean> context = Constraint.buildContext(taintsFromContext, config);

    Assert.assertTrue(context.isEmpty());
  }

  @Test
  public void buildContext_forNullContextNonEmptyConfig() {
    Set<String> taintsFromContext = null;

    Set<String> config = new HashSet<>();
    config.add("A");

    Map<String, Boolean> context = Constraint.buildContext(taintsFromContext, config);

    Assert.assertTrue(context.isEmpty());
  }

  @Test
  public void buildContext_forEmptyContextEmptyConfig() {
    Set<String> taintsFromContext = new HashSet<>();
    Set<String> config = new HashSet<>();

    Map<String, Boolean> context = Constraint.buildContext(taintsFromContext, config);

    Assert.assertTrue(context.isEmpty());
  }

  @Test
  public void buildContext_forEmptyContextNonEmptyConfig() {
    Set<String> taintsFromContext = new HashSet<>();

    Set<String> config = new HashSet<>();
    config.add("A");

    Map<String, Boolean> context = Constraint.buildContext(taintsFromContext, config);

    Assert.assertTrue(context.isEmpty());
  }

  @Test
  public void buildContext_forNonEmptyContextEmptyConfig() {
    String A = "A";

    Set<String> taintsFromContext = new HashSet<>();
    taintsFromContext.add(A);

    Set<String> config = new HashSet<>();

    Map<String, Boolean> context = Constraint.buildContext(taintsFromContext, config);

    Assert.assertTrue(context.containsKey(A));
    Assert.assertEquals(false, context.get(A));
  }

  @Test
  public void buildContext_forNonEmptyContextNonEmptyConfig() {
    String A = "A";

    Set<String> taintsFromContext = new HashSet<>();
    taintsFromContext.add(A);

    Set<String> config = new HashSet<>();
    config.add(A);

    Map<String, Boolean> context = Constraint.buildContext(taintsFromContext, config);

    Assert.assertTrue(context.containsKey(A));
    Assert.assertEquals(true, context.get(A));
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

  static Map<String, Boolean> buildPartialConfig_notC() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("C", false);

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