package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
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

public class DynamicAnalysisSpecificationTest {

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

    DynamicAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = DynamicRunningExampleAdapter.getListOfOptions();

    // Traces and Contexts
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = getRunningExampleDecisionsToTracesAndContexts(
        programOptions);
    Map<String, Map<List<String>, Context>> decisionsToTracesAndContexts = getDecisionsToTracesAndContexts(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndContexts, decisionsToTracesAndContexts);

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

  private Map<String, Map<List<String>, Context>> getRunningExampleDecisionsToTracesAndContexts(
      List<String> programOptions) {
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = new HashMap<>();

    Map<List<String>, Context> tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_1,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_2,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:17"), getContext(setA(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, FOO_Z_DECISION_1, tracesToContexts);

    return expectedDecisionsToTracesAndContexts;
  }

  @Test
  public void simpleExample1() throws IOException, InterruptedException {
    String programName = SimpleExample1Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = SimpleExample1Adapter.getListOfOptions();

    // Traces and Contexts
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = getSimpleExample1DecisionsToTracesAndContexts(
        programOptions);
    Map<String, Map<List<String>, Context>> decisionsToTracesAndContexts = getDecisionsToTracesAndContexts(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndContexts, decisionsToTracesAndContexts);

    // Traces to set of options
    Map<String, Map<List<String>, Set<String>>> expectedDecisionsToTracesAndOptions = getSimpleExample1DecisionsToTracesAndOptions();
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = getDecisionsToTracesAndTables(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndOptions, decisionsToTracesAndOptions);
  }

  private Map<String, Map<List<String>, Context>> getSimpleExample1DecisionsToTracesAndContexts(
      List<String> programOptions) {
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = new HashMap<>();

    Map<List<String>, Context> tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_1,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(setA(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_2,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_3,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_4,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_5,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_6,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_7,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_8,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_9,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_10,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_11,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_12,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_13,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_14,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_15,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_16,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:51"), getContext(setA(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, FOO_DECISION_1, tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:55"), getContext(setB(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, FOO_DECISION_1, tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:59"), getContext(setA(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MOO_Z_DECISION_1, tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:63"), getContext(setB(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MOO_Z_DECISION_1, tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:67"), getContext(setA(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, BAR_Z_DECISION_1, tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:71"), getContext(setB(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, BAR_Z_DECISION_1, tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:75"), getContext(setA(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, COW_Z_DECISION_1, tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace("main:79"), getContext(setB(), setAB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, COW_Z_DECISION_1, tracesToContexts);

    return expectedDecisionsToTracesAndContexts;
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

    DynamicAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = Example1Adapter.getListOfOptions();

    // Traces and Contexts
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = getExample1DecisionsToTracesAndContexts(
        programOptions);
    Map<String, Map<List<String>, Context>> decisionsToTracesAndContexts = getDecisionsToTracesAndContexts(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndContexts, decisionsToTracesAndContexts);

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

  private Map<String, Map<List<String>, Context>> getExample1DecisionsToTracesAndContexts(
      List<String> programOptions) {
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = new HashMap<>();

    Map<List<String>, Context> tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_1,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_2,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(emptySet(), setB()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_3,
        tracesToContexts);

    return expectedDecisionsToTracesAndContexts;
  }

  @Test
  public void phosphorExample2() throws IOException, InterruptedException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = PhosphorExample2Adapter.getListOfOptions();

    // Traces and Contexts
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = getExample2DecisionsToTracesAndContexts(
        programOptions);
    Map<String, Map<List<String>, Context>> decisionsToTracesAndContexts = getDecisionsToTracesAndContexts(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndContexts, decisionsToTracesAndContexts);

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

  private Map<String, Map<List<String>, Context>> getExample2DecisionsToTracesAndContexts(
      List<String> programOptions) {
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = new HashMap<>();

    Map<List<String>, Context> tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_1,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_2,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(emptySet(), setA()));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_3,
        tracesToContexts);

    return expectedDecisionsToTracesAndContexts;
  }

  @Test
  public void MultiFacets() throws IOException, InterruptedException {
    String programName = MultiFacetsAdapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(programName);
    Map<JavaRegion, DecisionInfo> decisionInfo = analysis.analyze(args);

    List<String> programOptions = MultiFacetsAdapter.getListOfOptions();

    // Traces and Contexts
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = getMultiFacetsDecisionsToTracesAndContexts(
        programOptions);
    Map<String, Map<List<String>, Context>> decisionsToTracesAndContexts = getDecisionsToTracesAndContexts(
        decisionInfo);

    Assert.assertEquals(expectedDecisionsToTracesAndContexts, decisionsToTracesAndContexts);

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

  private Map<String, Map<List<String>, Context>> getMultiFacetsDecisionsToTracesAndContexts(
      List<String> programOptions) {
    Map<String, Map<List<String>, Context>> expectedDecisionsToTracesAndContexts = new HashMap<>();

    Map<List<String>, Context> tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_1,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_2,
        tracesToContexts);

    tracesToContexts = new HashMap<>();
    tracesToContexts.put(getTrace(), getContext(programOptions));
    putOrAddContext(expectedDecisionsToTracesAndContexts, MAIN_STRINGS_DECISION_3,
        tracesToContexts);

    return expectedDecisionsToTracesAndContexts;
  }

  private static Map<String, Map<List<String>, Context>> getDecisionsToTracesAndContexts(
      Map<JavaRegion, DecisionInfo> regionsToDecisions) {
    Map<String, Map<List<String>, Context>> decisionsToTracesAndContexts = new HashMap<>();

    for (Map.Entry<JavaRegion, DecisionInfo> entry : regionsToDecisions.entrySet()) {
      DecisionInfo decisionInfo = entry.getValue();
      Map<List<String>, Context> tracesToContexts = decisionInfo.getStackTracesToContexts();

      JavaRegion region = entry.getKey();
      decisionsToTracesAndContexts
          .put(getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
              tracesToContexts);
    }

    return decisionsToTracesAndContexts;
  }

  private static Map<String, Map<List<String>, Set<String>>> getDecisionsToTracesAndTables(
      Map<JavaRegion, DecisionInfo> regionsToDecisions) {
    Map<String, Map<List<String>, Set<String>>> decisionsToTracesAndOptions = new HashMap<>();

    for (Map.Entry<JavaRegion, DecisionInfo> entry : regionsToDecisions.entrySet()) {
      DecisionInfo decisionInfo = entry.getValue();
      Map<List<String>, DecisionBranchCountTable> tracesToTables = decisionInfo
          .getStackTracesToDecisionBranchTables();
      Map<List<String>, Set<String>> tracesToOptions = new HashMap<>();

      for (Map.Entry<List<String>, DecisionBranchCountTable> traceToTable : tracesToTables
          .entrySet()) {
        Set<String> options = DynamicAnalysisSpecification
            .getMinimalSetOfOptions(traceToTable.getValue());
        tracesToOptions.put(traceToTable.getKey(), options);
      }

      JavaRegion region = entry.getKey();
      decisionsToTracesAndOptions
          .put(getRegionInfo(region.getRegionMethod(), region.getStartRegionIndex()),
              tracesToOptions);
    }

    return decisionsToTracesAndOptions;
  }

  private static void putOrAddContext(
      Map<String, Map<List<String>, Context>> decisionsToTracesAndContexts,
      String decision, Map<List<String>, Context> tracesToContexts) {
    Map<List<String>, Context> entry = decisionsToTracesAndContexts.get(decision);

    if (entry == null) {
      decisionsToTracesAndContexts.put(decision, tracesToContexts);
    }
    else {
      entry.putAll(tracesToContexts);
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
  private static Context getContext(Set<String>... configs) {
    Context context = new Context();

    for (Set<String> config : configs) {
      context.addConfig(config);
    }

    return context;
  }

  private static Context getContext(List<String> options) {
    Context context = new Context();
    Set<Set<String>> configs = Helper.getConfigurations(options);

    for (Set<String> config : configs) {
      context.addConfig(config);
    }

    return context;
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