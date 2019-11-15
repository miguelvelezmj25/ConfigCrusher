package edu.cmu.cs.mvelezce.builder.idta;

import org.junit.Test;

import java.io.IOException;

public class IDTAPerformanceModelBuilderTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    throw new UnsupportedOperationException("implement");
    //    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    //    BaseRegionInstrumenter<Set<FeatureExpr>> instrumenter = new
    // IDTATimerInstrumenter(programName);
    //    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints =
    //        instrumenter.getProcessedRegionsToData();
    //
    //    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
    //        new IDTAPerfAggregatorProcessor(programName);
    //
    //    String[] args = new String[0];
    //    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);
    //
    //    List<String> options = BaseTrivialAdapter.getListOfOptions();
    //    BasePerformanceModelBuilder<Set<FeatureExpr>, FeatureExpr> builder =
    //        new IDTAPerformanceModelBuilder(
    //            programName, options, regionsToConstraints, performanceEntries);
    //
    //    args = new String[2];
    //    args[0] = "-delres";
    //    args[1] = "-saveres";
    //    builder.analyze(args);
  }
}
