package edu.cmu.cs.mvelezce.analysis.idta;

import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class IDTAAnalysisTest {

  @Test
  public void trivial() throws IOException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    IDTAAnalysis analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> results = analysis.analyze();

    Assert.assertFalse(results.isEmpty());
  }
}
