package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.region;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit.ImplicitAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.orContext.OrContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample6.SimpleForExample6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sound.SoundAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces6.Subtraces6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces7.Subtraces7Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class TaintPhosphorAnalysisTest {

  @Test
  public void Trivial() throws IOException, InterruptedException {
    String systemName = TrivialAdapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void SimpleForExample6() throws IOException, InterruptedException {
    String systemName = SimpleForExample6Adapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String systemName = SubtracesAdapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces2() throws IOException, InterruptedException {
    String systemName = Subtraces2Adapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces6() throws IOException, InterruptedException {
    String systemName = Subtraces6Adapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces7() throws IOException, InterruptedException {
    String systemName = Subtraces7Adapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Implicit() throws IOException, InterruptedException {
    String systemName = ImplicitAdapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void dynamicRunningExample() throws IOException, InterruptedException {
    String systemName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void orContext() throws IOException, InterruptedException {
    String systemName = OrContextAdapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void example2() throws IOException, InterruptedException {
    String systemName = PhosphorExample2Adapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void example3() throws IOException, InterruptedException {
    String systemName = PhosphorExample3Adapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Sound() throws IOException, InterruptedException {
    String systemName = SoundAdapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String systemName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> write = analysis.analyze(args);

    args = new String[0];

    analysis = new TaintPhosphorAnalysis(systemName);
    Map<JavaRegion, InfluencingTaints> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}