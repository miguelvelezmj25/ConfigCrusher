package edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.export.idta;

import edu.cmu.cs.mvelezce.java.execute.sampling.idta.IDTAExecutor;
import edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.export.BaseExportSnapshot;

public class IDTAExportSnapshot extends BaseExportSnapshot {

  private static final String OUTPUT_DIR = IDTAExecutor.OUTPUT_DIR;

  public IDTAExportSnapshot(String programName) {
    super(programName);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}
