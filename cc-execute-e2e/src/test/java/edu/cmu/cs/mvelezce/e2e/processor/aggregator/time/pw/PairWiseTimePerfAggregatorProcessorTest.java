package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.pw;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.E2ETimeExecutionParser;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.E2EUserTimeExecutionParser;
import edu.cmu.cs.mvelezce.e2e.execute.time.pw.PairWiseTimeExecutor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class PairWiseTimePerfAggregatorProcessorTest {

  @Test
  public void convert_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    E2ETimeExecutionParser e2eUserTimeExecutionParser =
        new E2EUserTimeExecutionParser(programName, PairWiseTimeExecutor.OUTPUT_DIR);
    BaseExecutor<PerfExecution> executor =
        new PairWiseTimeExecutor(programName, e2eUserTimeExecutionParser);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new PairWiseTimePerfAggregatorProcessor(
            programName, itersToResults, BaseExecutor.USER, PerfAggregatorProcessor.ADDED_TIME);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void convert_real() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    E2ETimeExecutionParser e2eUserTimeExecutionParser =
        new E2EUserTimeExecutionParser(programName, PairWiseTimeExecutor.OUTPUT_DIR);
    BaseExecutor<PerfExecution> executor =
        new PairWiseTimeExecutor(programName, e2eUserTimeExecutionParser);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new PairWiseTimePerfAggregatorProcessor(
            programName, itersToResults, BaseExecutor.REAL, PerfAggregatorProcessor.ADDED_TIME);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }

  @Test
  public void runBenchC_real() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    E2ETimeExecutionParser e2eUserTimeExecutionParser =
        new E2EUserTimeExecutionParser(programName, PairWiseTimeExecutor.OUTPUT_DIR);
    BaseExecutor<PerfExecution> executor =
        new PairWiseTimeExecutor(programName, e2eUserTimeExecutionParser);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new PairWiseTimePerfAggregatorProcessor(
            programName, itersToResults, BaseExecutor.REAL, PerfAggregatorProcessor.ADDED_TIME);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }
}
