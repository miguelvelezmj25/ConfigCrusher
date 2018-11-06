package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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

    DynamicAnalysis<DecisionBranchCountTable> analysis = new BranchCoverageAnalysis(programName);
    Map<JavaRegion, DecisionBranchCountTable> table = analysis.analyze(args);

    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.1", SpecificationTest.setA());
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.2", SpecificationTest.setA());
    expectedDecisionsToOptions.put("foo(Z)V.1", SpecificationTest.setAB());

    Map<String, Set<String>> results = SpecificationTest.getDecisionToOptions(table);

    Assert.assertEquals(expectedDecisionsToOptions, results);
  }

  @Test
  public void simpleExample1() throws IOException, InterruptedException {
    String programName = SimpleExample1Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionBranchCountTable> analysis = new BranchCoverageAnalysis(programName);
    Map<JavaRegion, DecisionBranchCountTable> table = analysis.analyze(args);

    Map<String, Set<String>> expectedDecisionsToOptions = new HashMap<>();
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.1", SpecificationTest.setA());
    expectedDecisionsToOptions.put("main([Ljava/lang/String;)V.2", SpecificationTest.setA());

    Map<String, Set<String>> results = SpecificationTest.getDecisionToOptions(table);

    Assert.assertEquals(expectedDecisionsToOptions, results);
  }

  private static Map<String, Set<String>> getDecisionToOptions(
      Map<JavaRegion, DecisionBranchCountTable> table) {
    Map<String, Set<String>> results = new HashMap<>();

    Specification spec = new Specification();
    for (Map.Entry<JavaRegion, DecisionBranchCountTable> entry : table.entrySet()) {
      JavaRegion region = entry.getKey();

      Set<String> options = spec.getMinimalSetOfOptions(entry.getValue());
      results.put(region.getRegionMethod() + "." + region.getStartRegionIndex(), options);
    }

    return results;
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