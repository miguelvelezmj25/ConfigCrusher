package edu.cmu.cs.mvelezce.eval.java.blackbox.execute;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.eval.java.blackbox.execute.adapters.measureDiskOrderedScan.BlackBoxMeasureDiskOrderedScanExecutorAdapter;
import edu.cmu.cs.mvelezce.eval.java.blackbox.execute.parser.BlackBoxExecutionParser;
import edu.cmu.cs.mvelezce.eval.java.blackbox.results.BlackBoxResult;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.BaseInstrumentExecutor;

import java.util.Set;

public abstract class BlackBoxExecutor extends BaseInstrumentExecutor<BlackBoxResult> {

  protected BlackBoxExecutor(
      String programName,
      Set<Set<String>> configurations,
      BlackBoxExecutionParser blackBoxExecutionParser) {
    super(programName, configurations, blackBoxExecutionParser);
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
