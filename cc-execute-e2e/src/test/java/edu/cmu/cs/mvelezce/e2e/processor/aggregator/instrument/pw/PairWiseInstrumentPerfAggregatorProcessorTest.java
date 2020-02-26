package edu.cmu.cs.mvelezce.e2e.processor.aggregator.instrument.pw;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.execute.instrument.pw.PairWiseInstrumentExecutor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class PairWiseInstrumentPerfAggregatorProcessorTest {

  @Test
  public void berkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseExecutor<PerfExecution> executor = new PairWiseInstrumentExecutor(programName);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new PairWiseInstrumentPerfAggregatorProcessor(programName, itersToResults);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseExecutor<PerfExecution> executor = new PairWiseInstrumentExecutor(programName);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new PairWiseInstrumentPerfAggregatorProcessor(programName, itersToResults);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }
}
