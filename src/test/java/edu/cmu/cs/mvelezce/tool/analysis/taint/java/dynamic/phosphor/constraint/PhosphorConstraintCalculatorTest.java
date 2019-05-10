package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class PhosphorConstraintCalculatorTest {

  private static final List<String> OPTIONS = new ArrayList<>();

  @Mock private Taint execCtxTaints;

  @Mock private Taint conditionTaints;

  @Mock private DecisionTaints decisionTaints;

  @BeforeClass
  public static void init() {
    OPTIONS.add("A");
    OPTIONS.add("B");
    OPTIONS.add("C");
  }

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    Mockito.when(decisionTaints.getConditionTaints()).thenReturn(conditionTaints);
    Mockito.when(decisionTaints.getExecCtxTaints()).thenReturn(execCtxTaints);
  }

  @Test
  public void deriveConstraints_forEmpty() {
    Set<DecisionTaints> results = new HashSet<>();
    Set<String> config = new HashSet<>();

    PhosphorConstraintCalculator phosphorConstraintCalculator =
        new PhosphorConstraintCalculator(OPTIONS);

    Collection<Set<ConfigConstraint>> constraintsSet =
        phosphorConstraintCalculator.deriveConstraints(results, config).values();
    Set<ConfigConstraint> constraints = new HashSet<>();

    for(Set<ConfigConstraint> entry : constraintsSet) {
      constraints.addAll(entry);
    }

    Assert.assertTrue(constraints.isEmpty());
  }

  @Test
  public void deriveConstraints_forSimpleConditionNotContext() {
    Mockito.when(conditionTaints.getTags()).thenReturn(new int[] {1});
    Mockito.when(execCtxTaints.getTags()).thenReturn(new int[] {0});

    Set<DecisionTaints> results = new HashSet<>();
    results.add(decisionTaints);

    Set<String> config = new HashSet<>();

    PhosphorConstraintCalculator phosphorConstraintCalculator =
        new PhosphorConstraintCalculator(OPTIONS);
    Collection<Set<ConfigConstraint>> constraintsSet =
        phosphorConstraintCalculator.deriveConstraints(results, config).values();
    Set<ConfigConstraint> constraints = new HashSet<>();

    for(Set<ConfigConstraint> entry : constraintsSet) {
      constraints.addAll(entry);
    }

    Assert.assertEquals(2, constraints.size());
    Assert.assertTrue(constraints.contains(getNotAConstraint()));
    Assert.assertTrue(constraints.contains(getAConstraint()));
  }

  @Test
  public void deriveConstraints_forMultiConditionNotContext() {
    Mockito.when(conditionTaints.getTags()).thenReturn(new int[] {5});
    Mockito.when(execCtxTaints.getTags()).thenReturn(new int[] {0});

    Set<DecisionTaints> results = new HashSet<>();
    results.add(decisionTaints);

    Set<String> config = new HashSet<>();

    PhosphorConstraintCalculator phosphorConstraintCalculator =
        new PhosphorConstraintCalculator(OPTIONS);
    Collection<Set<ConfigConstraint>> constraintsSet =
        phosphorConstraintCalculator.deriveConstraints(results, config).values();
    Set<ConfigConstraint> constraints = new HashSet<>();

    for(Set<ConfigConstraint> entry : constraintsSet) {
      constraints.addAll(entry);
    }

    Assert.assertEquals(4, constraints.size());
    Assert.assertTrue(constraints.contains(getACConstraint()));
    Assert.assertTrue(constraints.contains(getANotCConstraint()));
    Assert.assertTrue(constraints.contains(getNotACConstraint()));
    Assert.assertTrue(constraints.contains(getNotANotCConstraint()));
  }

  @Test
  public void deriveConstraints_forMultiConditionSimpleContext() {
    Mockito.when(conditionTaints.getTags()).thenReturn(new int[] {5});
    Mockito.when(execCtxTaints.getTags()).thenReturn(new int[] {2});

    Set<DecisionTaints> results = new HashSet<>();
    results.add(decisionTaints);

    Set<String> config = new HashSet<>();
    config.add("B");

    PhosphorConstraintCalculator phosphorConstraintCalculator =
        new PhosphorConstraintCalculator(OPTIONS);
    Collection<Set<ConfigConstraint>> constraintsSet =
        phosphorConstraintCalculator.deriveConstraints(results, config).values();
    Set<ConfigConstraint> constraints = new HashSet<>();

    for(Set<ConfigConstraint> entry : constraintsSet) {
      constraints.addAll(entry);
    }

    Assert.assertEquals(4, constraints.size());
    Assert.assertTrue(constraints.contains(getABCConstraint()));
    Assert.assertTrue(constraints.contains(getABNotCConstraint()));
    Assert.assertTrue(constraints.contains(getNotABCConstraint()));
    Assert.assertTrue(constraints.contains(getNotABNotCConstraint()));
  }

  @Test
  public void deriveConstraints_forMultiConditionMultiContext() {
    Mockito.when(conditionTaints.getTags()).thenReturn(new int[] {3});
    Mockito.when(execCtxTaints.getTags()).thenReturn(new int[] {2});

    Set<DecisionTaints> results = new HashSet<>();
    results.add(decisionTaints);

    Set<String> config = new HashSet<>();
    config.add("B");

    PhosphorConstraintCalculator phosphorConstraintCalculator =
        new PhosphorConstraintCalculator(OPTIONS);
    Collection<Set<ConfigConstraint>> constraintsSet =
        phosphorConstraintCalculator.deriveConstraints(results, config).values();
    Set<ConfigConstraint> constraints = new HashSet<>();

    for(Set<ConfigConstraint> entry : constraintsSet) {
      constraints.addAll(entry);
    }

    Assert.assertEquals(2, constraints.size());
    Assert.assertTrue(constraints.contains(getABConstraint()));
    Assert.assertTrue(constraints.contains(getNotABConstraint()));
  }

  private static ConfigConstraint getNotAConstraint() {
    Set<String> config = new HashSet<>();

    Set<String> options = new HashSet<>();
    options.add("A");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getAConstraint() {
    Set<String> config = new HashSet<>();
    config.add("A");

    Set<String> options = new HashSet<>();
    options.add("A");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getACConstraint() {
    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("C");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getNotACConstraint() {
    Set<String> config = new HashSet<>();
    config.add("C");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getANotCConstraint() {
    Set<String> config = new HashSet<>();
    config.add("A");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getNotANotCConstraint() {
    Set<String> config = new HashSet<>();

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getABCConstraint() {
    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("B");
    config.add("C");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getNotABCConstraint() {
    Set<String> config = new HashSet<>();
    config.add("B");
    config.add("C");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getABNotCConstraint() {
    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("B");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getNotABNotCConstraint() {
    Set<String> config = new HashSet<>();
    config.add("B");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");
    options.add("C");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getABConstraint() {
    Set<String> config = new HashSet<>();
    config.add("A");
    config.add("B");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    return ConfigConstraint.fromConfig(config, options);
  }

  private static ConfigConstraint getNotABConstraint() {
    Set<String> config = new HashSet<>();
    config.add("B");

    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    return ConfigConstraint.fromConfig(config, options);
  }
}
