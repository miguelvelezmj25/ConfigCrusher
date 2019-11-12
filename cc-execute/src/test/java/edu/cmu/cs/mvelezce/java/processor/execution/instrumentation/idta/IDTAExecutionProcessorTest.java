package edu.cmu.cs.mvelezce.java.processor.execution.instrumentation.idta;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.BaseInstrumentationExecutor;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.idta.IDTAInstrumentationExecutor;
import edu.cmu.cs.mvelezce.java.results.instrumentation.raw.RawPerfExecution;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAExecutionProcessorTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    BaseInstrumentationExecutor executor = new IDTAInstrumentationExecutor(programName);
    Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions =
        executor.getRawExecutionParser().readResults();

    Analysis processor = new IDTAExecutionProcessor(programName, itersToRawPerfExecutions);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }

  @Test
  public void berkeley() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseInstrumentationExecutor executor = new IDTAInstrumentationExecutor(programName);
    Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions =
        executor.getRawExecutionParser().readResults();

    Analysis processor = new IDTAExecutionProcessor(programName, itersToRawPerfExecutions);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }
}
