package edu.cmu.cs.mvelezce.analysis.region.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.idta.IDTAAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.BaseCountRegionsPerMethodAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.idta.IDTATimerInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTACountRegionsPerMethodAnalysisTest {

  @Test
  public void vanillaTrivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis =
        new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    BaseCountRegionsPerMethodAnalysis<Set<FeatureExpr>> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToConstraints);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }

  @Test
  public void vanillaBerkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis =
        new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    BaseCountRegionsPerMethodAnalysis<Set<FeatureExpr>> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToConstraints);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }

  @Test
  public void expandedBerkeleyDB() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    BaseRegionInstrumenter<Set<FeatureExpr>> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints =
        instrumenter.getProcessedRegionsToData();

    BaseCountRegionsPerMethodAnalysis<Set<FeatureExpr>> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToConstraints);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }
}
