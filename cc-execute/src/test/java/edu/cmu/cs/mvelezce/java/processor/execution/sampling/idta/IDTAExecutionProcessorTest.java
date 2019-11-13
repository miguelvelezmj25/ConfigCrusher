package edu.cmu.cs.mvelezce.java.processor.execution.sampling.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.IDTATimerInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
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

    BaseRegionInstrumenter<Set<FeatureExpr>> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints =
        instrumenter.getProcessedRegionsToData();

    Analysis processor =
        new IDTAExecutionProcessor(programName, itersToRawPerfExecs, regionsToConstraints.keySet());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    processor.analyze(args);
  }
}
