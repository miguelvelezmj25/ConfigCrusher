package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.idta;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.execute.time.idta.IDTAE2ETimeExecutor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAE2ETimePerfAggregatorProcessorTest {

  @Test
  public void convert() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseExecutor<PerfExecution> executor = new IDTAE2ETimeExecutor(programName);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new IDTAE2ETimePerfAggregatorProcessor(programName, itersToResults);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }
}
