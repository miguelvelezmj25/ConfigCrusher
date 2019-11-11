package edu.cmu.cs.mvelezce.analysis.region.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.idta.IDTAAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.BaseCountRegionsPerMethodAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTACountRegionsPerMethodAnalysisTest {

  @Test
  public void vanillaTrivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    BaseCountRegionsPerMethodAnalysis<Set<FeatureExpr>> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToConstraints);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }
}
