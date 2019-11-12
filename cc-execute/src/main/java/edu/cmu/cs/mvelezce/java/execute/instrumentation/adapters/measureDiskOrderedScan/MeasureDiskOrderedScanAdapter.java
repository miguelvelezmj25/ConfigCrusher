package edu.cmu.cs.mvelezce.java.execute.instrumentation.adapters.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.Executor;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.adapters.ExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class MeasureDiskOrderedScanAdapter extends BaseMeasureDiskOrderedScanAdapter
    implements ExecutorAdapter {

  private final Executor executor;

  public MeasureDiskOrderedScanAdapter(Executor executor) {
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
