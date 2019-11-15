package edu.cmu.cs.mvelezce.java.execute.instrumentation.idta;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.BaseInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.adapters.measureDiskOrderedScan.InstrumentMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.adapters.trivial.InstrumentTrivialExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.parser.RawInstrumentExecutionParser;
import edu.cmu.cs.mvelezce.java.results.instrumentation.raw.RawInstrumentPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.Set;

public class IDTAInstrumentExecutor extends BaseInstrumentExecutor<RawInstrumentPerfExecution> {

  public static final String OUTPUT_DIR =
      "../cc-execute/" + Options.DIRECTORY + "/executor/java/idta/programs/instrumentation";

  public IDTAInstrumentExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  IDTAInstrumentExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, new RawInstrumentExecutionParser(programName, OUTPUT_DIR));
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case InstrumentTrivialExecutorAdapter.PROGRAM_NAME:
        adapter = new InstrumentTrivialExecutorAdapter(this);
        break;
      case InstrumentMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new InstrumentMeasureDiskOrderedScanAdapter(this);
        ((BaseMeasureDiskOrderedScanAdapter) adapter)
            .preProcess("../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }
}
