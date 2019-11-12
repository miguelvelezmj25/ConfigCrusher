package edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.export.idta;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler.export.BaseExportSnapshot;
import org.junit.Test;

import java.io.IOException;

public class IDTAExportSnapshotTest {

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseExportSnapshot exportSnapshot = new IDTAExportSnapshot(programName);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    exportSnapshot.analyze(args);
  }
}
