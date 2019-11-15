package edu.cmu.cs.mvelezce.eval.adapters.blackbox.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.eval.approach.blackbox.BlackBoxExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.measureDiskOrderedScan.MeasureDiskOrderedScanExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class BlackBoxMeasureDiskOrderedScanExecutorAdapter
    extends MeasureDiskOrderedScanExecutorAdapter {

  private final BlackBoxExecutor executor;

  public <T> BlackBoxMeasureDiskOrderedScanExecutorAdapter(BlackBoxExecutor executor) {
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
