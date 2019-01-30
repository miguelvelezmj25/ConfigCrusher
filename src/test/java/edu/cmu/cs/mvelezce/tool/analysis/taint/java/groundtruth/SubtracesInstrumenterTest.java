package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import java.io.IOException;
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
}