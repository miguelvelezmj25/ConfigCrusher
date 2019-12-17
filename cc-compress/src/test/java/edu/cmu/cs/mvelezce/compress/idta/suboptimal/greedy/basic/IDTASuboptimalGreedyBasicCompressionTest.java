package edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.basic;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.idta.IDTAAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IDTASuboptimalGreedyBasicCompressionTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    List<String> options = BaseTrivialAdapter.getListOfOptions();

    String workloadSize = "small";
    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis =
        new IDTAAnalysis(programName, workloadSize);
    Collection<Set<FeatureExpr>> allConstraints = analysis.analyze().values();

    BaseCompression compression =
        new IDTASuboptimalGreedyBasicCompression(programName, options, allConstraints);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);

    System.out.println(configs.size() + " to sample");
  }

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();

    String workloadSize = "small";
    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis =
        new IDTAAnalysis(programName, workloadSize);
    Collection<Set<FeatureExpr>> allConstraints = analysis.analyze().values();

    BaseCompression compression =
        new IDTASuboptimalGreedyBasicCompression(programName, options, allConstraints);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);

    System.out.println(configs.size() + " to sample");
  }
}
