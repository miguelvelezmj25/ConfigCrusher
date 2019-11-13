package edu.cmu.cs.mvelezce.java.processor.execution.instrumentation.idta;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.idta.IDTAExecutor;
import org.junit.Test;

import java.io.IOException;

public class IDTAExecutionProcessorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    BaseExecutor executor = new IDTAExecutor(programName);

    throw new UnsupportedOperationException("Implement");
    //    Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions =
    //        executor.getRawExecutionParser().readResults();
    //
    //    Analysis processor = new IDTAExecutionProcessor(programName, itersToRawPerfExecutions);
    //
    //    String[] args = new String[2];
    //    args[0] = "-delres";
    //    args[1] = "-saveres";
    //
    //    processor.analyze(args);
  }

  @Test
  public void berkeley() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseExecutor executor = new IDTAExecutor(programName);

    throw new UnsupportedOperationException("Implement");
    //    Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions =
    //        executor.getRawExecutionParser().readResults();
    //
    //    Analysis processor = new IDTAExecutionProcessor(programName, itersToRawPerfExecutions);
    //
    //    String[] args = new String[2];
    //    args[0] = "-delres";
    //    args[1] = "-saveres";
    //
    //    processor.analyze(args);
  }
}
