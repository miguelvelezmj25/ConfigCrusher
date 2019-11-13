package edu.cmu.cs.mvelezce.java.execute.sampling.idta;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.adapters.measureDiskOrderedScan.SamplingMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.adapters.trivial.SamplingTrivialExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.parser.profiler.jprofiler.RawExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.HashSet;
import java.util.Set;

public class IDTAExecutor extends BaseExecutor {

  public static final String OUTPUT_DIR =
      "../cc-execute/" + Options.DIRECTORY + "/executor/java/idta/programs/sampling";

  public IDTAExecutor(String programName) {
    this(programName, new HashSet<>());
  }

  IDTAExecutor(String programName, Set<Set<String>> configurations) {
    super(programName, configurations, new RawExecutionParser(programName, OUTPUT_DIR));
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }

  @Override
  protected ExecutorAdapter getExecutorAdapter() {
    ExecutorAdapter adapter;

    switch (this.getProgramName()) {
      case SamplingTrivialExecutorAdapter.PROGRAM_NAME:
        adapter = new SamplingTrivialExecutorAdapter(this);
        break;
      case SamplingMeasureDiskOrderedScanAdapter.PROGRAM_NAME:
        adapter = new SamplingMeasureDiskOrderedScanAdapter(this);
        ((BaseMeasureDiskOrderedScanAdapter) adapter)
            .preProcess("../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR);
        break;
      default:
        throw new RuntimeException("Could not find an adapter for " + this.getProgramName());
    }

    return adapter;
  }
}
