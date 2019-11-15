package edu.cmu.cs.mvelezce.eval.approach.blackbox;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.eval.adapters.blackbox.measureDiskOrderedScan.BlackBoxMeasureDiskOrderedScanExecutorAdapter;
import edu.cmu.cs.mvelezce.eval.results.blackbox.BlackBoxResult;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.BaseInstrumentExecutor;

import java.util.Set;

public abstract class BlackBoxExecutor extends BaseInstrumentExecutor<BlackBoxResult> {

  protected BlackBoxExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, null);
  }

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new BlackBoxMeasureDiskOrderedScanExecutorAdapter(this);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }
}
