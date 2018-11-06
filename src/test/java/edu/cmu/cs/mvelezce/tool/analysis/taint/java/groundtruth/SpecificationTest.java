package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class SpecificationTest {

  @Test
  public void RunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;
    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new BranchCoverageAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    // Context
    Map<String, Set<Set<String>>> expectedDecisionToContexts = new HashMap<>();
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.1",
        SpecificationTest.getContext(DynamicRunningExampleAdapter.getListOfOptions()));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.2",
        SpecificationTest.getContext(DynamicRunningExampleAdapter.getListOfOptions()));
    expectedDecisionToContexts.put("foo(Z)V.1",
        SpecificationTest.getContext(SpecificationTest.setA(), SpecificationTest.setAB()));

    Map<String, Set<Set<String>>> contextResults = SpecificationTest
        .getDecisionsToContexts(decisionInfo);

    Assert.assertEquals(expectedDecisionToContexts, contextResults);

    // Set of options
    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.1", SpecificationTest.setA());
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.2", SpecificationTest.setA());
    expectedDecisionsToOptions.put("foo(Z)V.1", SpecificationTest.setAB());

    Map<String, Set<String>> results = SpecificationTest.getDecisionsToOptions(decisionInfo);

    Assert.assertEquals(expectedDecisionsToOptions, results);
  }

  @Test
  public void simpleExample1() throws IOException, InterruptedException {
    String programName = SimpleExample1Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new BranchCoverageAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    // Context
    Map<String, Set<Set<String>>> expectedDecisionToContexts = new HashMap<>();
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.1",
        SpecificationTest.getContext(SimpleExample1Adapter.getListOfOptions()));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.2",
        SpecificationTest.getContext(SpecificationTest.setA()));

    Map<String, Set<Set<String>>> contextResults = SpecificationTest
        .getDecisionsToContexts(decisionInfo);

    Assert.assertEquals(expectedDecisionToContexts, contextResults);

    // Set of options
    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.1", SpecificationTest.setA());
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.2", SpecificationTest.setA());

    Map<String, Set<String>> results = SpecificationTest.getDecisionsToOptions(decisionInfo);

    Assert.assertEquals(expectedDecisionsToOptions, results);
  }

  private static Map<String, Set<Set<String>>> getDecisionsToContexts(
      Map<JavaRegion, DecisionInfo> decisionInfo) {
    Map<String, Set<Set<String>>> decisionsToContexts = new HashMap<>();

    for (Map.Entry<JavaRegion, DecisionInfo> entry : decisionInfo.entrySet()) {
      JavaRegion region = entry.getKey();

      Set<Set<String>> context = Specification.getContext(entry.getValue());
      decisionsToContexts.put(
          SpecificationTest.getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
          context);
    }

    return decisionsToContexts;
  }

  private static String getRegionInfo(String regionMethod, int startRegionIndex) {
    return regionMethod + "." + startRegionIndex;
  }

  @SafeVarargs
  private static Set<Set<String>> getContext(Set<String>... configs) {
    return new HashSet<>(Arrays.asList(configs));
  }

  private static Set<Set<String>> getContext(List<String> options) {
    return Helper.getConfigurations(options);
  }

  private static Map<String, Set<String>> getDecisionsToOptions(
      Map<JavaRegion, DecisionInfo> decisionInfo) {
    Map<String, Set<String>> decisionsToOptions = new HashMap<>();

    for (Map.Entry<JavaRegion, DecisionInfo> entry : decisionInfo.entrySet()) {
      JavaRegion region = entry.getKey();

      Set<String> options = Specification
          .getMinimalSetOfOptions(entry.getValue().getDecisionBranchTable());
      decisionsToOptions.put(
          SpecificationTest.getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
          options);
    }

    return decisionsToOptions;
  }

  private static Set<String> emptySet() {
    return new HashSet<>();
  }

  private static Set<String> setA() {
    Set<String> options = new HashSet<>();
    options.add("A");

    return options;
  }

  private static Set<String> setB() {
    Set<String> options = new HashSet<>();
    options.add("B");

    return options;
  }

  private static Set<String> setAB() {
    Set<String> options = new HashSet<>();
    options.add("A");
    options.add("B");

    return options;
  }

}