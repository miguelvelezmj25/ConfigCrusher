package edu.cmu.cs.mvelezce.eval.java;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.approaches.sampling.fw.FeatureWiseSampling;
import edu.cmu.cs.mvelezce.approaches.sampling.pw.PairWiseSampling;
import edu.cmu.cs.mvelezce.builder.idta.IDTAPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.gt.GTCompression;
import edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions.IDTASuboptimalGreedyConjunctionsCompression;
import edu.cmu.cs.mvelezce.eval.java.constraint.ConstraintEvaluation;
import edu.cmu.cs.mvelezce.exhaustive.builder.bf.BruteForceExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.exhaustive.builder.gt.GroundTruthExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.learning.builder.model.matlab.MatlabLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class EvaluationTest {

  @Test
  public void berkeleyDB_GT_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_BF_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new BruteForceExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, configs, model);
  }

  @Test
  public void berkeleyDB_IDTA_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new IDTAPerformanceModelBuilder(programName);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    BaseCompression gtCompression = new GTCompression(programName);
    args = new String[0];
    Set<Set<String>> configsToPredict = gtCompression.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.IDTA, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_PW_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_Compare_IDTA_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_BF_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }

  @Test
  public void lucene_GT_Data() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }
}
