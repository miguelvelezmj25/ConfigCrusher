package edu.cmu.cs.mvelezce.eval.java;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.builder.idta.IDTAPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.naive.IDTANaiveCompression;
import edu.cmu.cs.mvelezce.e2e.perfmodel.bf.BruteForcePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.e2e.perfmodel.gt.GroundTruthPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.eval.java.constraint.ConstraintEvaluation;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class EvaluationTest {

  @Test
  public void berkeleyDB_GT_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = ConfigHelper.getConfigurations(options);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthPerformanceModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_BF_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = ConfigHelper.getConfigurations(options);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new BruteForcePerformanceModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, configs, model);
  }

  @Test
  public void berkeleyDB_IDTA_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTANaiveCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new IDTAPerformanceModelBuilder(programName);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configsToPredict = ConfigHelper.getConfigurations(options);

    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.IDTA, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_Compare_IDTA_BF() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, Evaluation.BF);
  }
}
