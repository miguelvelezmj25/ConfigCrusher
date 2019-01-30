package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Compiler;
import java.io.IOException;

public class SubtracesInstrumenter extends BaseInstrumenter {

  public SubtracesInstrumenter(String programName, String srcDir, String classDir) {
    super(programName, srcDir, classDir);
  }

  @Override
  public void instrument() {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  public void compile() throws IOException, InterruptedException {
    Compiler.compile(this.getSrcDir());
  }
}
