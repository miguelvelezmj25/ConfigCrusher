package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.region.TaintPhosphorAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample6.SimpleForExample6Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.timer.DynamicConfigCrusherTimerRegionInstrumenter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.junit.Test;

public class DynamicTimerInstrumenterTest {

  private void compile(String srcDir, String classDir) throws IOException, InterruptedException {
    Instrumenter compiler = new CompileInstrumenter(srcDir, classDir);
    compiler.compile();
  }

  @Test
  public void trivial()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    String entry = TrivialAdapter.MAIN_CLASS;
    String srcDir = TrivialAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = TrivialAdapter.INSTRUMENTED_CLASS_PATH;

    this.compile(srcDir, classDir);

    String[] args = new String[0];

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(programName);
    Map<JavaRegion, InfluencingTaints> decisionsToInfluencingTaints = analysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Instrumenter instrumenter = new DynamicConfigCrusherTimerRegionInstrumenter(programName, entry,
        classDir, decisionsToInfluencingTaints);
    instrumenter.instrument(args);
  }

  @Test
  public void simpleForExample6()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
    String programName = SimpleForExample6Adapter.PROGRAM_NAME;
    String entry = SimpleForExample6Adapter.MAIN_CLASS;
    String srcDir = SimpleForExample6Adapter.INSTRUMENTED_DIR_PATH;
    String classDir = SimpleForExample6Adapter.INSTRUMENTED_CLASS_PATH;

    this.compile(srcDir, classDir);

    String[] args = new String[0];

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(programName);
    Map<JavaRegion, InfluencingTaints> decisionsToInfluencingTaints = analysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Instrumenter instrumenter = new DynamicConfigCrusherTimerRegionInstrumenter(programName, entry,
        classDir, decisionsToInfluencingTaints);
    instrumenter.instrument(args);
  }

  @Test
  public void measureDiskOrderedScan()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String entry = MeasureDiskOrderedScanAdapter.MAIN_CLASS;
    String srcDir = MeasureDiskOrderedScanAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = MeasureDiskOrderedScanAdapter.INSTRUMENTED_CLASS_PATH;

//    this.compile(srcDir, classDir);

    String[] args = new String[0];

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(programName);
    Map<JavaRegion, InfluencingTaints> decisionsToInfluencingTaints = analysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Instrumenter instrumenter = new DynamicConfigCrusherTimerRegionInstrumenter(programName, entry,
        classDir, decisionsToInfluencingTaints);
    instrumenter.instrument(args);
  }

  @Test
  public void subtraces()
      throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String entry = SubtracesAdapter.MAIN_CLASS;
    String srcDir = SubtracesAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = SubtracesAdapter.INSTRUMENTED_CLASS_PATH;

//    this.compile(srcDir, classDir);

    String[] args = new String[0];

    TaintPhosphorAnalysis analysis = new TaintPhosphorAnalysis(programName);
    Map<JavaRegion, InfluencingTaints> decisionsToInfluencingTaints = analysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Instrumenter instrumenter = new DynamicConfigCrusherTimerRegionInstrumenter(programName, entry,
        classDir, decisionsToInfluencingTaints);
    instrumenter.instrument(args);
  }

}
