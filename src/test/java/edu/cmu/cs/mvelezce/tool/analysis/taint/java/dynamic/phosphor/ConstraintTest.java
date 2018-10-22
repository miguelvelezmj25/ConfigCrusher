package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class ConstraintTest {

  @Test(expected = IllegalArgumentException.class)
  public void toPartialCCConfig_forEmptyConstraint() {
    Map<String, Boolean> emptyPartialConfig = new HashMap<>();

    Constraint.toPartialCCConfig(emptyPartialConfig);
  }

  @Test
  public void toPartialCCConfig_forConstraint_NotA_NotB() {
    Map<String, Boolean> partialConfig = ConstraintTest.buildPartialConfig_notA_notB();

    Set<String> partialCCConfig = Constraint.toPartialCCConfig(partialConfig);
    Assert.assertTrue(partialCCConfig.isEmpty());
  }

  @Test
  public void toPartialCCConfig_forConstraint_A_notB() {
    Map<String, Boolean> partialConfig = ConstraintTest.buildPartialConfig_A_notB();

    Set<String> expectedConfig = new HashSet<>();
    expectedConfig.add("A");

    Set<String> partialCCConfig = Constraint.toPartialCCConfig(partialConfig);
    Assert.assertEquals(expectedConfig, partialCCConfig);
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
    Constraint constraint = buildConstraint_A_notB();

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
    Constraint constraint0 = buildConstraint_A();
    Constraint constraint1 = buildConstraint_A();

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
    Constraint constraint1 = buildConstraint_A_B();

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
    Assert.assertTrue(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forTrueDiffConstraints() {
    Constraint constraint0 = buildConstraint_B();
    Constraint constraint1 = new Constraint(buildPartialConfig_A(), buildPartialConfig_B());

    Assert.assertTrue(constraint0.isSubsetOf(constraint1));
  }

  @Test
  public void isSubsetOf_forFalseDiffPartialConfigs0() {
    Constraint constraint0 = buildConstraint_A();
    Constraint constraint1 = buildConstraint_notA();

    Assert.assertFalse(constraint0.isSubsetOf(constraint1));
    Assert.assertFalse(constraint1.isSubsetOf(constraint0));
  }

  @Test
  public void isSubsetOf_forFalseEqualPartialConfigsDiffCtxs0() {
    Constraint constraint0 = buildConstraint_A();
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
  public void toConfigWithValues_forAllOptionsFalse() {
    Map<String, Boolean> expectedConfigWithValues = ConstraintTest.buildPartialConfig_notA_notB();

    Set<String> config = new HashSet<>();

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Map<String, Boolean> configWithValues = Constraint.toConfigWithValues(config, options);

    Assert.assertEquals(expectedConfigWithValues, configWithValues);
  }

  @Test
  public void toConfigWithValues_forAllOptionsTrue() {
    Map<String, Boolean> expectedWithValues = ConstraintTest.buildPartialConfig_A_B();

    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("B");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Map<String, Boolean> configWithValues = Constraint.toConfigWithValues(config, options);

    Assert.assertEquals(expectedWithValues, configWithValues);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toConfigWithValues_forInvalidArgs() {
    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("C");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    Constraint.toConfigWithValues(config, options);
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

  @Test(expected = IllegalArgumentException.class)
  public void buildPartialConfigs_forEmptyTaintsAtSink() {
    Set<String> emptyTaintsAtSink = new HashSet<>();

    Constraint.buildPartialConfigs(emptyTaintsAtSink);
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

    Set<Map<String, Boolean>> constraints = Constraint.buildPartialConfigs(taintsAtSink);

    Assert.assertEquals(expectedConstraints, constraints);
  }

  @Test
  public void isEqualTo_forFalse() {
    Constraint constraint_A = ConstraintTest.buildConstraint_A();
    Constraint constraint_A_True = new Constraint(buildPartialConfig_A(),
        buildPartialConfig_notB());

    Assert.assertFalse(constraint_A.isEqualTo(constraint_A_True));
    Assert.assertFalse(constraint_A_True.isEqualTo(constraint_A));
  }

  @Test
  public void isEqualTo_forTrue() {
    Constraint constraint_A = ConstraintTest.buildConstraint_A();
    Constraint constraint_A_True = new Constraint(buildPartialConfig_A(), buildPartialConfig_A());

    Assert.assertTrue(constraint_A.isEqualTo(constraint_A_True));
    Assert.assertTrue(constraint_A_True.isEqualTo(constraint_A));
  }

  static Constraint buildConstraint_notB_C() {
    Map<String, Boolean> partialConfig_notB_C = ConstraintTest.buildPartialConfig_notB_C();

    return new Constraint(partialConfig_notB_C);
  }

  static Constraint buildConstraint_notB() {
    Map<String, Boolean> partialConfig_notB = ConstraintTest.buildPartialConfig_notB();

    return new Constraint(partialConfig_notB);
  }

  static Constraint buildConstraint_B() {
    Map<String, Boolean> partialConfig_B = ConstraintTest.buildPartialConfig_B();

    return new Constraint(partialConfig_B);
  }

  static Constraint buildConstraint_A() {
    Map<String, Boolean> partialConfig_A = ConstraintTest.buildPartialConfig_A();

    return new Constraint(partialConfig_A);
  }

  static Constraint buildConstraint_notA() {
    Map<String, Boolean> partialConfig_notA = ConstraintTest.buildPartialConfig_notA();

    return new Constraint(partialConfig_notA);
  }

  static Constraint buildConstraint_A_B() {
    Map<String, Boolean> partialConfig_A_B = ConstraintTest.buildPartialConfig_A_B();

    return new Constraint(partialConfig_A_B);
  }

  static Constraint buildConstraint_notA_B() {
    Map<String, Boolean> partialConfig_notA_B = ConstraintTest.buildPartialConfig_notA_B();

    return new Constraint(partialConfig_notA_B);
  }

  static Constraint buildConstraint_A_notB() {
    Map<String, Boolean> partialConfig_A_notB = ConstraintTest.buildPartialConfig_A_notB();

    return new Constraint(partialConfig_A_notB);
  }

  static Constraint buildConstraint_notA_notB() {
    Map<String, Boolean> partialConfig_notA_notB = ConstraintTest.buildPartialConfig_notA_notB();

    return new Constraint(partialConfig_notA_notB);
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

  static Map<String, Boolean> buildPartialConfig_notB_C() {
    Map<String, Boolean> partialConfig = new HashMap<>();
    partialConfig.put("B", false);
    partialConfig.put("C", true);

    return partialConfig;
  }

}