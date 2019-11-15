package edu.cmu.cs.mvelezce.eval.adapters.blackbox.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.java.execute.adapters.measureDiskOrderedScan.MeasureDiskOrderedScanExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class BlackBoxMeasureDiskOrderedScanExecutorAdapter
    extends MeasureDiskOrderedScanExecutorAdapter {

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    throw new UnsupportedOperationException("Implement");
  }
}
