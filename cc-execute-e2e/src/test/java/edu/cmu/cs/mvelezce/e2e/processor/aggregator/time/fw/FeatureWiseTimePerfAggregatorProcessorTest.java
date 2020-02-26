package edu.cmu.cs.mvelezce.e2e.processor.aggregator.time.fw;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.e2e.execute.time.fw.FeatureWiseTimeExecutor;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class FeatureWiseTimePerfAggregatorProcessorTest {

  @Test
  public void convert() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseExecutor<PerfExecution> executor = new FeatureWiseTimeExecutor(programName);
    Map<Integer, Set<PerfExecution>> itersToResults =
        executor.getRawExecutionParser().readResults();
    Analysis perfAggregatorProcessor =
        new FeatureWiseTimePerfAggregatorProcessor(programName, itersToResults);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    perfAggregatorProcessor.analyze(args);
  }
}
