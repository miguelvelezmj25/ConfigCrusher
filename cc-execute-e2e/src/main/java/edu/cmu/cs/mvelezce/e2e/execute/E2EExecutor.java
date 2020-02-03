package edu.cmu.cs.mvelezce.e2e.execute;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.convert.E2EConvertAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.indexFiles.E2EIndexFilesAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.adapters.measureDiskOrderedScan.E2EMeasureDiskOrderedScanExecutorAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.parser.E2EExecutionParser;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.BaseInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.util.Set;

public abstract class E2EExecutor extends BaseInstrumentExecutor<ProcessedPerfExecution> {

  protected E2EExecutor(
      String programName,
      Set<Set<String>> configurations,
      E2EExecutionParser e2EExecutionParser,
      int waitAfterExecution) {
    super(programName, configurations, e2EExecutionParser, waitAfterExecution);
  }

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new E2EMeasureDiskOrderedScanExecutorAdapter(this);
        break;
      case BaseIndexFilesAdapter.PROGRAM_NAME:
        adapter = new E2EIndexFilesAdapter(this);
        break;
      case BaseConvertAdapter.PROGRAM_NAME:
        adapter = new E2EConvertAdapter(this);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }
}
