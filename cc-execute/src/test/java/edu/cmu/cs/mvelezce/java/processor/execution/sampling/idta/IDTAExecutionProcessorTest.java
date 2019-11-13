package edu.cmu.cs.mvelezce.java.processor.execution.sampling.idta;

import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAExecutor;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawPerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAExecutionProcessorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    BaseExecutor executor = new IDTAExecutor(programName);
    Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecs =
        executor.getRawExecutionParser().readResults();

    throw new UnsupportedOperationException("Implement");
    //    Analysis processor = new IDTAExecutionProcessor(programName, itersToRawPerfExecs);
    //
    //    String[] args = new String[2];
    //    args[0] = "-delres";
    //    args[1] = "-saveres";
    //
    //    processor.analyze(args);

  }
}
