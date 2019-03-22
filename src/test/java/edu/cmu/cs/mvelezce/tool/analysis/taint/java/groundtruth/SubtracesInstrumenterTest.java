package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

public class SubtracesInstrumenterTest {

  @Test
  public void compileTraces() throws IOException, InterruptedException {
    String programName = "Traces";
    String srcDir = "../performance-mapper-evaluation/instrumented/phosphor-examples";
    String classDir = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

    Instrumenter instrumenter = new SubtracesInstrumenter(programName, srcDir, classDir);
    instrumenter.compile();
  }

  @Test
  public void instrumentTraces()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    String programName = "phosphorExamples";
    String srcDir = "../performance-mapper-evaluation/instrumented/phosphor-examples";
    String classDir = "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Instrumenter instrumenter = new SubtracesInstrumenter(programName, srcDir, classDir);
    instrumenter.instrument(args);
  }

  @Test
  public void instrumentPrevayler()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    String programName = "measureDiskOrderedScan";
    String srcDir = "../performance-mapper-evaluation/instrumented/berkeley-db";
    String classDir = "../performance-mapper-evaluation/instrumented/berkeley-db/target/classes";

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Instrumenter instrumenter = new SubtracesInstrumenter(programName, srcDir, classDir);
    instrumenter.instrument(args);
  }
}