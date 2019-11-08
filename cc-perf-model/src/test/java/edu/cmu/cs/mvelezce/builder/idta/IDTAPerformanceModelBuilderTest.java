package edu.cmu.cs.mvelezce.builder.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.instrument.idta.IDTATimerInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.java.processor.aggregator.idta.IDTAPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IDTAPerformanceModelBuilderTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    BaseRegionInstrumenter<Set<FeatureExpr>> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints =
        instrumenter.getProcessedRegionsToData();

    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName);

    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    List<String> options = BaseTrivialAdapter.getListOfOptions();
    BasePerformanceModelBuilder<Set<FeatureExpr>, FeatureExpr> builder =
        new IDTAPerformanceModelBuilder(
            programName, options, regionsToConstraints, performanceEntries);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    builder.analyze(args);
  }
}
