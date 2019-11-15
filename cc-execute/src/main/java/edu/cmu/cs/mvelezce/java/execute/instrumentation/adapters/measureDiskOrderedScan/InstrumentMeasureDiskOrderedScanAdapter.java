package edu.cmu.cs.mvelezce.java.execute.instrumentation.adapters.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.idta.IDTAInstrumentExecutor;

import java.io.IOException;
import java.util.Set;

public class InstrumentMeasureDiskOrderedScanAdapter extends BaseMeasureDiskOrderedScanAdapter
    implements ExecutorAdapter {

  private final IDTAInstrumentExecutor executor;

  public InstrumentMeasureDiskOrderedScanAdapter(IDTAInstrumentExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseMeasureDiskOrderedScanAdapter.INSTRUMENTED_CLASS_PATH,
        ExecutorAdapter.EXECUTOR_MAIN_CLASS_PREFIX + BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME,
        configAsArgs);
  }
}
