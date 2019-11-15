package edu.cmu.cs.mvelezce.eval.approach.bf;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.eval.adapters.blackbox.measureDiskOrderedScan.BlackBoxMeasureDiskOrderedScanExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.Set;

public class BruteForceExecutor<T> extends BaseExecutor<T> {

  public static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/idta/programs/bf";

  BruteForceExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, null);
  }

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new BlackBoxMeasureDiskOrderedScanExecutorAdapter();
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}
