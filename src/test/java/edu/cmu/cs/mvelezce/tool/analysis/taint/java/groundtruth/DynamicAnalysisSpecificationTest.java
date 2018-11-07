package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
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

public class DynamicAnalysisSpecificationTest {

  @Test
  public void RunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;
    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new BranchCoverageAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    // Context
    List<String> programOptions = DynamicRunningExampleAdapter.getListOfOptions();

    Map<String, Set<Set<String>>> expectedDecisionToContexts = new HashMap<>();
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.1",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.2",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("foo(Z)V.1",
        DynamicAnalysisSpecificationTest
            .getContext(DynamicAnalysisSpecificationTest.setA(),
                DynamicAnalysisSpecificationTest.setAB()));

    Map<String, Set<Set<String>>> contextResults = DynamicAnalysisSpecificationTest
        .getDecisionsToContexts(decisionInfo);

    Assert.assertEquals(expectedDecisionToContexts, contextResults);

    // Set of options
    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.1", DynamicAnalysisSpecificationTest.setA());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.2", DynamicAnalysisSpecificationTest.setA());
    expectedDecisionsToOptions.put("foo(Z)V.1", DynamicAnalysisSpecificationTest.setAB());

    Map<String, Set<String>> results = DynamicAnalysisSpecificationTest
        .getDecisionsToOptions(decisionInfo);

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
    List<String> programOptions = SimpleExample1Adapter.getListOfOptions();

    Map<String, Set<Set<String>>> expectedDecisionToContexts = new HashMap<>();
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.1",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.2",
        DynamicAnalysisSpecificationTest.getContext(DynamicAnalysisSpecificationTest.setA(),
            DynamicAnalysisSpecificationTest.setAB()));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.3",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.4",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.5",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.6",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.7",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.8",
        DynamicAnalysisSpecificationTest.getContext(programOptions));

    Map<String, Set<Set<String>>> contextResults = DynamicAnalysisSpecificationTest
        .getDecisionsToContexts(decisionInfo);

    Assert.assertEquals(expectedDecisionToContexts, contextResults);

    // Set of options
    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.1", DynamicAnalysisSpecificationTest.setA());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.2", DynamicAnalysisSpecificationTest.setA());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.3", DynamicAnalysisSpecificationTest.emptySet());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.4", DynamicAnalysisSpecificationTest.emptySet());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.5", DynamicAnalysisSpecificationTest.setB());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.6", DynamicAnalysisSpecificationTest.emptySet());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.7", DynamicAnalysisSpecificationTest.setB());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.8", DynamicAnalysisSpecificationTest.setB());

    Map<String, Set<String>> results = DynamicAnalysisSpecificationTest
        .getDecisionsToOptions(decisionInfo);

    Assert.assertEquals(expectedDecisionsToOptions, results);
  }

  @Test
  public void example1() throws IOException, InterruptedException {
    String programName = Example1Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new BranchCoverageAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    // Context
    List<String> programOptions = Example1Adapter.getListOfOptions();

    Map<String, Set<Set<String>>> expectedDecisionToContexts = new HashMap<>();
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.1",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.2",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.3",
        DynamicAnalysisSpecificationTest.getContext(DynamicAnalysisSpecificationTest.emptySet(),
            DynamicAnalysisSpecificationTest
                .setB()));

    Map<String, Set<Set<String>>> contextResults = DynamicAnalysisSpecificationTest
        .getDecisionsToContexts(decisionInfo);

    Assert.assertEquals(expectedDecisionToContexts, contextResults);

    // Set of options
    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.1", DynamicAnalysisSpecificationTest.setA());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.2", DynamicAnalysisSpecificationTest.setA());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.3", DynamicAnalysisSpecificationTest.setAB());

    Map<String, Set<String>> results = DynamicAnalysisSpecificationTest
        .getDecisionsToOptions(decisionInfo);

    Assert.assertEquals(expectedDecisionsToOptions, results);
  }

  @Test
  public void phosphorExample2() throws IOException, InterruptedException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new BranchCoverageAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    // Context
    List<String> programOptions = PhosphorExample2Adapter.getListOfOptions();

    Map<String, Set<Set<String>>> expectedDecisionToContexts = new HashMap<>();
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.1",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.2",
        DynamicAnalysisSpecificationTest.getContext(programOptions));
    expectedDecisionToContexts.put("main([Ljava/lang/String;)V.3",
        DynamicAnalysisSpecificationTest.getContext(DynamicAnalysisSpecificationTest.setA(), DynamicAnalysisSpecificationTest.emptySet()));

    Map<String, Set<Set<String>>> contextResults = DynamicAnalysisSpecificationTest
        .getDecisionsToContexts(decisionInfo);

    Assert.assertEquals(expectedDecisionToContexts, contextResults);

    // Set of options
    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.1", DynamicAnalysisSpecificationTest.setA());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.2", DynamicAnalysisSpecificationTest.setB());
    expectedDecisionsToOptions
        .put("main([Ljava/lang/String;)V.3", DynamicAnalysisSpecificationTest.setAB());

    Map<String, Set<String>> results = DynamicAnalysisSpecificationTest
        .getDecisionsToOptions(decisionInfo);

    Assert.assertEquals(expectedDecisionsToOptions, results);
  }

  private static Map<String, Set<Set<String>>> getDecisionsToContexts(
      Map<JavaRegion, DecisionInfo> decisionInfo) {
    Map<String, Set<Set<String>>> decisionsToContexts = new HashMap<>();

    for (Map.Entry<JavaRegion, DecisionInfo> entry : decisionInfo.entrySet()) {
      JavaRegion region = entry.getKey();

      Set<Set<String>> context = DynamicAnalysisSpecification.getContext(entry.getValue());
      decisionsToContexts.put(
          DynamicAnalysisSpecificationTest
              .getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
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

      Set<String> options = DynamicAnalysisSpecification
          .getMinimalSetOfOptions(entry.getValue().getDecisionBranchTable());
      decisionsToOptions.put(
          DynamicAnalysisSpecificationTest
              .getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
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