package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

@Deprecated
public class DynamicRegionAnalysisSpecificationTest {

  private static final String MAIN_STRINGS_DECISION_1 = "main([Ljava/lang/String;)V.1";
  private static final String MAIN_STRINGS_DECISION_2 = "main([Ljava/lang/String;)V.2";
  private static final String MAIN_STRINGS_DECISION_3 = "main([Ljava/lang/String;)V.3";
  private static final String MAIN_STRINGS_DECISION_4 = "main([Ljava/lang/String;)V.4";
  private static final String MAIN_STRINGS_DECISION_5 = "main([Ljava/lang/String;)V.5";
  private static final String MAIN_STRINGS_DECISION_6 = "main([Ljava/lang/String;)V.6";
  private static final String MAIN_STRINGS_DECISION_7 = "main([Ljava/lang/String;)V.7";
  private static final String MAIN_STRINGS_DECISION_8 = "main([Ljava/lang/String;)V.8";
  private static final String MAIN_STRINGS_DECISION_9 = "main([Ljava/lang/String;)V.9";
  private static final String MAIN_STRINGS_DECISION_10 = "main([Ljava/lang/String;)V.10";
  private static final String MAIN_STRINGS_DECISION_11 = "main([Ljava/lang/String;)V.11";
  private static final String MAIN_STRINGS_DECISION_12 = "main([Ljava/lang/String;)V.12";
  private static final String MAIN_STRINGS_DECISION_13 = "main([Ljava/lang/String;)V.13";
  private static final String MAIN_STRINGS_DECISION_14 = "main([Ljava/lang/String;)V.14";
  private static final String MAIN_STRINGS_DECISION_15 = "main([Ljava/lang/String;)V.15";
  private static final String MAIN_STRINGS_DECISION_16 = "main([Ljava/lang/String;)V.16";
  private static final String FOO_Z_DECISION_1 = "foo(Z)V.1";
  private static final String FOO_DECISION_1 = "foo()V.1";
  private static final String MOO_Z_DECISION_1 = "moo(Z)V.1";
  private static final String BAR_Z_DECISION_1 = "bar(Z)V.1";
  private static final String COW_Z_DECISION_1 = "cow(Z)V.1";

  @Test
  public void RunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;
    // Program arguments
    String[] args = new String[0];

    DynamicRegionAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = DynamicRunningExampleAdapter.getListOfOptions();

    // Traces and Ctxs
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = getRunningExampleDecisionsToTracesAndCtxs(
        programOptions);
    Map<String, Map<List<String>, VariabilityCtx>> decisionsToTracesAndVariabilityCtxs = getDecisionsToTracesAndCtxs(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndVariabilityCtxs,
        decisionsToTracesAndVariabilityCtxs);

    // Traces to set of options
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = getRunningExampleDecisionsToTracesAndOptions();
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = getDecisionsToTracesAndTables(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndOptions, decisionsToTracesAndOptions);
  }

  private Map<String, Map<List<String>, Set<String>>> getRunningExampleDecisionsToTracesAndOptions() {
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = new HashMap<>();

    Map<List<String>, Set<String>> tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_2, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:17"), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, FOO_Z_DECISION_1, tracesToOptions);

    return expectedDecisionsToTracesAndOptions;
  }

  private Map<String, Map<List<String>, VariabilityCtx>> getRunningExampleDecisionsToTracesAndCtxs(
      List<String> programOptions) {
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = new HashMap<>();

    Map<List<String>, VariabilityCtx> tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_2,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:17"), getCtx(setA(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, FOO_Z_DECISION_1,
        tracesToCtxs);

    return expectedDecisionsToTracesAndVariabilityCtxs;
  }

  @Test
  public void simpleExample1() throws IOException, InterruptedException {
    String programName = SimpleExample1Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicRegionAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = SimpleExample1Adapter.getListOfOptions();

    // Traces and Ctxs
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = getSimpleExample1DecisionsToTracesAndCtxs(
        programOptions);
    Map<String, Map<List<String>, VariabilityCtx>> decisionsToTracesAndVariabilityCtxs = getDecisionsToTracesAndCtxs(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndVariabilityCtxs,
        decisionsToTracesAndVariabilityCtxs);

    // Traces to set of options
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = getSimpleExample1DecisionsToTracesAndOptions();
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = getDecisionsToTracesAndTables(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndOptions, decisionsToTracesAndOptions);
  }

  private Map<String, Map<List<String>, VariabilityCtx>> getSimpleExample1DecisionsToTracesAndCtxs(
      List<String> programOptions) {
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = new HashMap<>();

    Map<List<String>, VariabilityCtx> tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(setA(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_2,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_3,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_4,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_5,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_6,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_7,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_8,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_9,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_10,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_11,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_12,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_13,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_14,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_15,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_16,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:51"), getCtx(setA(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, FOO_DECISION_1, tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:55"), getCtx(setB(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, FOO_DECISION_1, tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:59"), getCtx(setA(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MOO_Z_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:63"), getCtx(setB(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MOO_Z_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:67"), getCtx(setA(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, BAR_Z_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:71"), getCtx(setB(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, BAR_Z_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:75"), getCtx(setA(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, COW_Z_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace("main:79"), getCtx(setB(), setAB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, COW_Z_DECISION_1,
        tracesToCtxs);

    return expectedDecisionsToTracesAndVariabilityCtxs;
  }

  private Map<String, Map<List<String>, Set<String>>> getSimpleExample1DecisionsToTracesAndOptions() {
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = new HashMap<>();

    Map<List<String>, Set<String>> tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_2, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_3, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_4, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_5, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_6, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_7, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_8, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_9, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_10, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_11, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_12, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_13, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_14, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_15, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_16, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:51"), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, FOO_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:55"), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, FOO_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:59"), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MOO_Z_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:63"), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MOO_Z_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:67"), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, BAR_Z_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:71"), emptySet());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, BAR_Z_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:75"), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, COW_Z_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace("main:79"), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, COW_Z_DECISION_1, tracesToOptions);

    return expectedDecisionsToTracesAndOptions;
  }

  @Test
  public void example1() throws IOException, InterruptedException {
    String programName = Example1Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicRegionAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = Example1Adapter.getListOfOptions();

    // Traces and Ctxs
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = getExample1DecisionsToTracesAndCtxs(
        programOptions);
    Map<String, Map<List<String>, VariabilityCtx>> decisionsToTracesAndVariabilityCtxs = getDecisionsToTracesAndCtxs(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndVariabilityCtxs,
        decisionsToTracesAndVariabilityCtxs);

    // Traces to set of options
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = getExample1DecisionsToTracesAndOptions();
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = getDecisionsToTracesAndTables(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndOptions, decisionsToTracesAndOptions);
  }

  private Map<String, Map<List<String>, Set<String>>> getExample1DecisionsToTracesAndOptions() {
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = new HashMap<>();

    Map<List<String>, Set<String>> tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_2, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_3, tracesToOptions);

    return expectedDecisionsToTracesAndOptions;
  }

  private Map<String, Map<List<String>, VariabilityCtx>> getExample1DecisionsToTracesAndCtxs(
      List<String> programOptions) {
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = new HashMap<>();

    Map<List<String>, VariabilityCtx> tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_2,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(emptySet(), setB()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_3,
        tracesToCtxs);

    return expectedDecisionsToTracesAndVariabilityCtxs;
  }

  @Test
  public void phosphorExample2() throws IOException, InterruptedException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicRegionAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = PhosphorExample2Adapter.getListOfOptions();

    // Traces and Ctxs
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = getExample2DecisionsToTracesAndCtxs(
        programOptions);
    Map<String, Map<List<String>, VariabilityCtx>> decisionsToTracesAndVariabilityCtxs = getDecisionsToTracesAndCtxs(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndVariabilityCtxs,
        decisionsToTracesAndVariabilityCtxs);

    // Traces to set of options
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = getExample2DecisionsToTracesAndOptions();
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = getDecisionsToTracesAndTables(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndOptions, decisionsToTracesAndOptions);
  }

  private Map<String, Map<List<String>, Set<String>>> getExample2DecisionsToTracesAndOptions() {
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = new HashMap<>();

    Map<List<String>, Set<String>> tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setB());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_2, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_3, tracesToOptions);

    return expectedDecisionsToTracesAndOptions;
  }

  private Map<String, Map<List<String>, VariabilityCtx>> getExample2DecisionsToTracesAndCtxs(
      List<String> programOptions) {
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = new HashMap<>();

    Map<List<String>, VariabilityCtx> tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_2,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(emptySet(), setA()));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_3,
        tracesToCtxs);

    return expectedDecisionsToTracesAndVariabilityCtxs;
  }

  @Test
  public void MultiFacets() throws IOException, InterruptedException {
    String programName = MultiFacetsAdapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicRegionAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = MultiFacetsAdapter.getListOfOptions();

    // Traces and Ctxs
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = getMultiFacetsDecisionsToTracesAndCtxs(
        programOptions);
    Map<String, Map<List<String>, VariabilityCtx>> decisionsToTracesAndVariabilityCtxs = getDecisionsToTracesAndCtxs(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndVariabilityCtxs,
        decisionsToTracesAndVariabilityCtxs);

    // Traces to set of options
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = getMultiFacetsDecisionsToTracesAndOptions();
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = getDecisionsToTracesAndTables(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndOptions, decisionsToTracesAndOptions);
  }

  private Map<String, Map<List<String>, Set<String>>> getMultiFacetsDecisionsToTracesAndOptions() {
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = new HashMap<>();

    Map<List<String>, Set<String>> tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_1, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_2, tracesToOptions);

    tracesToOptions = new HashMap<>();
    tracesToOptions.put(getTrace(), setA());
    putOrAddOptions(expectedDecisionsToTracesAndOptions, MAIN_STRINGS_DECISION_3, tracesToOptions);

    return expectedDecisionsToTracesAndOptions;
  }

  private Map<String, Map<List<String>, VariabilityCtx>> getMultiFacetsDecisionsToTracesAndCtxs(
      List<String> programOptions) {
    Map<String, Map<List<String>, VariabilityCtx>> expectedDecisionsToTracesAndVariabilityCtxs = new HashMap<>();

    Map<List<String>, VariabilityCtx> tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_1,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_2,
        tracesToCtxs);

    tracesToCtxs = new HashMap<>();
    tracesToCtxs.put(getTrace(), getCtx(programOptions));
    putOrAddCtx(expectedDecisionsToTracesAndVariabilityCtxs, MAIN_STRINGS_DECISION_3,
        tracesToCtxs);

    return expectedDecisionsToTracesAndVariabilityCtxs;
  }

  private static Map<String, Map<List<String>, VariabilityCtx>> getDecisionsToTracesAndCtxs(
      Map<JavaRegion, DecisionInfo> regionsToDecisions) {
    Map<String, Map<List<String>, VariabilityCtx>> decisionsToTracesAndVariabilityCtxs = new HashMap<>();

    for (Map.Entry<JavaRegion, DecisionInfo> entry : regionsToDecisions.entrySet()) {
      DecisionInfo decisionInfo = entry.getValue();
      Map<List<String>, VariabilityCtx> tracesToCtxs = decisionInfo
          .getCallingCtxsToVariabilityCtxs();

      JavaRegion region = entry.getKey();
      decisionsToTracesAndVariabilityCtxs
          .put(getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
              tracesToCtxs);
    }

    return decisionsToTracesAndVariabilityCtxs;
  }

  private static Map<String, Map<List<String>, Set<String>>> getDecisionsToTracesAndTables(
      Map<JavaRegion, DecisionInfo> regionsToDecisions) {
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = new HashMap<>();

    for (Map.Entry<JavaRegion, DecisionInfo> entry : regionsToDecisions.entrySet()) {
      DecisionInfo decisionInfo = entry.getValue();
      Map<List<String>, DecisionBranchCountTable> tracesToTables = decisionInfo
          .getCallingCtxsToDecisionBranchTables();
      Map<List<String>, Set<String>> tracesToOptions = new HashMap<>();

      for (Map.Entry<List<String>, DecisionBranchCountTable> traceToTable : tracesToTables
          .entrySet()) {
        Set<String> options = SpecificationAnalysis.getMinimalSetOfOptions(traceToTable.getValue());
        tracesToOptions.put(traceToTable.getKey(), options);
      }

      JavaRegion region = entry.getKey();
      decisionsToTracesAndOptions
          .put(getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
              tracesToOptions);
    }

    return decisionsToTracesAndOptions;
  }

  private static void putOrAddCtx(
      Map<String, Map<List<String>, VariabilityCtx>> decisionsToTracesAndVariabilityCtxs,
      String decision, Map<List<String>, VariabilityCtx> tracesToCtxs) {
    Map<List<String>, VariabilityCtx> entry = decisionsToTracesAndVariabilityCtxs.get(decision);

    if (entry == null) {
      decisionsToTracesAndVariabilityCtxs.put(decision, tracesToCtxs);
    }
    else {
      entry.putAll(tracesToCtxs);
    }
  }

  private static void putOrAddOptions(
      Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions,
      String decision, Map<List<String>, Set<String>> tracesToOptions) {
    Map<List<String>, Set<String>> entry = decisionsToTracesAndOptions.get(decision);

    if (entry == null) {
      decisionsToTracesAndOptions.put(decision, tracesToOptions);
    }
    else {
      entry.putAll(tracesToOptions);
    }
  }

  private static String getRegionInfo(String regionMethod, int startRegionIndex) {
    return regionMethod + "." + startRegionIndex;
  }

  private static List<String> getTrace(String... elements) {
    return new ArrayList<>(Arrays.asList(elements));
  }

  @SafeVarargs
  private static VariabilityCtx getCtx(Set<String>... configs) {
    VariabilityCtx variabilityCtx = new VariabilityCtx();

    for (Set<String> config : configs) {
      variabilityCtx.addConfig(config);
    }

    return variabilityCtx;
  }

  private static VariabilityCtx getCtx(List<String> options) {
    VariabilityCtx variabilityCtx = new VariabilityCtx();
    Set<Set<String>> configs = Helper.getConfigurations(options);

    for (Set<String> config : configs) {
      variabilityCtx.addConfig(config);
    }

    return variabilityCtx;
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