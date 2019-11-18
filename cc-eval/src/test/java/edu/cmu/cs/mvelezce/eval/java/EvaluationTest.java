package edu.cmu.cs.mvelezce.eval.java;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.eval.java.blackbox.perfmodel.bf.BruteForcePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;

public class EvaluationTest {

  @Test
  public void berkeleyDB_BF_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel> builder = new BruteForcePerformanceModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel model = builder.analyze(args);

    System.out.println();
  }
}
