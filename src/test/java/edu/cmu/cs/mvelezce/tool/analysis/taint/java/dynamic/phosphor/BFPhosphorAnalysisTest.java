package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class BFPhosphorAnalysisTest {

  @Test
  public void dynamicRunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(DynamicRunningExampleAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void dynamicRunningExample_forRead() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    analysis.analyze(args);
  }

  @Test
  public void simpleExample1() throws IOException, InterruptedException {
    String programName = SimpleExample1Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleExample1Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void example1() throws IOException, InterruptedException {
    String programName = Example1Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Example1Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void example2() throws IOException, InterruptedException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(PhosphorExample2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void multiFacet() throws IOException, InterruptedException {
    String programName = MultiFacetsAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(MultiFacetsAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void example3() throws IOException, InterruptedException {
    String programName = PhosphorExample3Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(PhosphorExample3Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}