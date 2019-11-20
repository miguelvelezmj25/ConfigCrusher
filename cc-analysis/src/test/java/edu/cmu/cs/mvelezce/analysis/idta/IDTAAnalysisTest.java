package edu.cmu.cs.mvelezce.analysis.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class IDTAAnalysisTest {

  @Test
  public void trivial() throws IOException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    IDTAAnalysis analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Set<FeatureExpr>> results = analysis.analyze();

    Assert.assertFalse(results.isEmpty());
  }
}
