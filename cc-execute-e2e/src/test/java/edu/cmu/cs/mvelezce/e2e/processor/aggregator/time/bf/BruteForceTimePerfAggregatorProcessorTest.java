package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.bf;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.execute.time.bf.BruteForceTimeExecutor;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.E2ETimeExecutionParser;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.E2EUserTimeExecutionParser;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class BruteForceTimePerfAggregatorProcessorTest {

  @Test
  public void convert_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    E2ETimeExecutionParser e2eUserTimeExecutionParser =
        new E2EUserTimeExecutionParser(programName, BruteForceTimeExecutor.OUTPUT_DIR);
    BaseExecutor<PerfExecution> executor =
        new BruteForceTimeExecutor(programName, e2eUserTimeExecutionParser);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new BruteForceTimePerfAggregatorProcessor(programName, itersToResults, BaseExecutor.USER);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }
}
