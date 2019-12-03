package edu.cmu.cs.mvelezce.e2e.execute;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.measureDiskOrderedScan.E2EMeasureDiskOrderedScanExecutorAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.parser.E2EExecutionParser;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.BaseInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.Set;

public abstract class E2EExecutor extends BaseInstrumentExecutor<ProcessedPerfExecution> {

  protected E2EExecutor(
      String programName, Set<Set<String>> configurations, E2EExecutionParser e2EExecutionParser) {
    super(programName, configurations, e2EExecutionParser);
  }

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new E2EMeasureDiskOrderedScanExecutorAdapter(this);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }
}
