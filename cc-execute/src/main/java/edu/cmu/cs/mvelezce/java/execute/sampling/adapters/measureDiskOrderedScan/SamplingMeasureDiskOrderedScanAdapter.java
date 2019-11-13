package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.IDTAExecutor;

import java.util.Set;

public class SamplingMeasureDiskOrderedScanAdapter extends BaseMeasureDiskOrderedScanAdapter
    implements ExecutorAdapter {

  private final IDTAExecutor executor;

  public SamplingMeasureDiskOrderedScanAdapter(IDTAExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) {
    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(configAsArgs);
  }
}
