package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.alldynamic.AllDynamicAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.ifOr2.IfOr2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext.OrContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext2.OrContext2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext3.OrContext3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext6.OrContext6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample8.PhosphorExample8Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample2.SimpleForExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext1.VariabilityContext1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.variabilityContext2.VariabilityContext2Adapter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class BFPhosphorAnalysisTest {

  @Test
  public void dynamicAll() {
    Collection<File> files = FileUtils.listFiles(new File(
            "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/phosphor-examples/src/main/java/edu/cmu/cs/mvelezce/analysis"),
        new String[]{"java"}, true);

    Set<String> programsWithErrors = new HashSet<>();

    for (File file : files) {
      String programName = file.getName();
      programName = programName.replace(".java", "");
      System.out.println(programName);

      try {
        String mainClass = AllDynamicAdapter.MAIN_PACKAGE + "." + programName;
        Set<String> options = new HashSet<>(AllDynamicAdapter.getListOfOptions());

        String[] args = new String[1];
//        String[] args = new String[2];
        args[0] = "-saveres";
//        args[1] = "-delres";

        DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, mainClass,
            options);
        Map<JavaRegion, SinkData> write = analysis.analyze(args);

        args = new String[0];
        analysis = new BFPhosphorAnalysis(programName);
        Map<JavaRegion, SinkData> read = analysis.analyze(args);

        Assert.assertEquals(write, read);
      }
      catch (Exception e) {
        programsWithErrors.add(programName);
      }
    }

    System.out.println();
    System.out.println("####################################");
    System.out.println("Programs with errors");
    System.out.println(programsWithErrors);
  }

  @Test
  public void dynamicRunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(DynamicRunningExampleAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void readDynamicRunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    String[] args = new String[0];
    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);
    PhosphorAnalysis.printProgramConstraints(read);
  }

  @Test
  public void simpleExample1() throws IOException, InterruptedException {
    String programName = SimpleExample1Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleExample1Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
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

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
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

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
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

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void readExample3() throws IOException, InterruptedException {
    String programName = PhosphorExample3Adapter.PROGRAM_NAME;

    String[] args = new String[0];
    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);
    PhosphorAnalysis.printProgramConstraints(read);
  }

  @Test
  public void ifOr2() throws IOException, InterruptedException {
    String programName = IfOr2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(IfOr2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void readIfOr2() throws IOException, InterruptedException {
    String programName = IfOr2Adapter.PROGRAM_NAME;

    String[] args = new String[0];
    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);
    PhosphorAnalysis.printProgramConstraints(read);
  }

  @Test
  public void orContext() throws IOException, InterruptedException {
    String programName = OrContextAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(OrContextAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void readOrContext() throws IOException, InterruptedException {
    String programName = OrContextAdapter.PROGRAM_NAME;

    String[] args = new String[0];
    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);
    PhosphorAnalysis.printProgramConstraints(read);
  }

  @Test
  public void orContext2() throws IOException, InterruptedException {
    String programName = OrContext2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(OrContext2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void readOrContext2() throws IOException, InterruptedException {
    String programName = OrContext2Adapter.PROGRAM_NAME;

    String[] args = new String[0];
    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);
    PhosphorAnalysis.printProgramConstraints(read);
  }

  @Test
  public void orContext3() throws IOException, InterruptedException {
    String programName = OrContext3Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(OrContext3Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void readOrContext3() throws IOException, InterruptedException {
    String programName = OrContext3Adapter.PROGRAM_NAME;

    String[] args = new String[0];
    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);
    PhosphorAnalysis.printProgramConstraints(read);
  }

  @Test
  public void multiFacet() throws IOException, InterruptedException {
    String programName = MultiFacetsAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(MultiFacetsAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void simpleForExample2() throws IOException, InterruptedException {
    String programName = SimpleForExample2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleForExample2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void variabilityContext1() throws IOException, InterruptedException {
    String programName = VariabilityContext1Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(VariabilityContext1Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void variabilityContext2() throws IOException, InterruptedException {
    String programName = VariabilityContext2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(VariabilityContext2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void example8() throws IOException, InterruptedException {
    String programName = PhosphorExample8Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(PhosphorExample8Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void readExample8() throws IOException, InterruptedException {
    String programName = PhosphorExample8Adapter.PROGRAM_NAME;

    String[] args = new String[0];
    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);
    PhosphorAnalysis.printProgramConstraints(read);
  }

  @Test
  public void orContext6() throws IOException, InterruptedException {
    String programName = OrContext6Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(OrContext6Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(programName, options);
    Map<JavaRegion, SinkData> write = analysis.analyze(args);

    args = new String[0];
    analysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, SinkData> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

}