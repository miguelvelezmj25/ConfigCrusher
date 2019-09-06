package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

// TODO MIGUEL maybe have a separate dir for projects instrumented for this evaluation?
public class SubtracesInstrumenterTest {

  @Test
  public void instrumentTraces()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = "phosphorExamples";
    String srcDir = TrivialAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = TrivialAdapter.INSTRUMENTED_CLASS_PATH;
    Instrumenter instrumenter = new SubtracesInstrumenter(programName, srcDir, classDir);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void instrumentPrevayler()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = "prevayler";
    String srcDir = "../performance-mapper-evaluation/instrumented/prevayler";
    String classDir = "../performance-mapper-evaluation/instrumented/prevayler/target/classes";

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Instrumenter instrumenter = new SubtracesInstrumenter(programName, srcDir, classDir);
    instrumenter.instrument(args);
  }

  @Test
  public void instrumentMeasureDiskOrderedScan()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String srcDir = MeasureDiskOrderedScanAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = MeasureDiskOrderedScanAdapter.INSTRUMENTED_CLASS_PATH;
    Instrumenter instrumenter = new SubtracesInstrumenter(programName, srcDir, classDir);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    instrumenter.instrument(args);
  }
}
