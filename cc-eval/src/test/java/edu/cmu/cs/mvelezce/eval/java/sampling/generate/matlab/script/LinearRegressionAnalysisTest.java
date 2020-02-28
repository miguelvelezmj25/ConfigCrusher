package edu.cmu.cs.mvelezce.eval.java.sampling.generate.matlab.script;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions.IDTASuboptimalGreedyConjunctionsCompression;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class LinearRegressionAnalysisTest {

  @Test
  public void berkeleyDB_instrument() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    LinearRegressionAnalysis analysis =
        new LinearRegressionAnalysis(programName, executedConfigs.size(), BaseExecutor.REAL);
    analysis.generateLinearRegressionScript();
  }

  @Test
  public void lucene_instrument() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    LinearRegressionAnalysis analysis =
        new LinearRegressionAnalysis(programName, executedConfigs.size(), BaseExecutor.REAL);
    analysis.generateLinearRegressionScript();
  }

  @Test
  public void convert_time_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    LinearRegressionAnalysis analysis =
        new LinearRegressionAnalysis(programName, executedConfigs.size(), BaseExecutor.USER);
    analysis.generateLinearRegressionScript();
  }
}
