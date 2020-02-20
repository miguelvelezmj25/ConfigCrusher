package edu.cmu.cs.mvelezce.e2e.execute.adapters.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.executor.E2EExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.measureDiskOrderedScan.MeasureDiskOrderedScanExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class E2EMeasureDiskOrderedScanExecutorAdapter
    extends MeasureDiskOrderedScanExecutorAdapter {

  private final E2EExecutor executor;

  public E2EMeasureDiskOrderedScanExecutorAdapter(E2EExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.preProcess("../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR);

    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_CLASS_PATH,
        ExecutorAdapter.EXECUTOR_MAIN_CLASS_PREFIX + BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME,
        configAsArgs);
  }
}
