package edu.cmu.cs.mvelezce.eval.java.accuracy.constraint;

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
import edu.cmu.cs.mvelezce.eval.java.accuracy.AccuracyEvaluation;
import edu.cmu.cs.mvelezce.exhaustive.builder.bf.BruteForceExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.exhaustive.builder.gt.GroundTruthExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.learning.builder.model.matlab.MatlabLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class AccuracyConstraintEvaluationTest {

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
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(AccuracyEvaluation.GT, configs, model);
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
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(AccuracyEvaluation.BF, configs, model);
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
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        AccuracyEvaluation.IDTA, executedConfigs, configsToPredict, model);
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

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(AccuracyEvaluation.FW, executedConfigs, configsToPredict, model);
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

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(AccuracyEvaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_Compare_IDTA_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(AccuracyEvaluation.IDTA, AccuracyEvaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_BF_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(AccuracyEvaluation.BF, AccuracyEvaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(AccuracyEvaluation.FW, AccuracyEvaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(AccuracyEvaluation.PW, AccuracyEvaluation.GT);
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
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(AccuracyEvaluation.GT, configs, model);
  }

  @Test
  public void lucene_BF_Data() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new BruteForceExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(AccuracyEvaluation.BF, configs, model);
  }

  @Test
  public void lucene_FW_Data() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(AccuracyEvaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void lucene_PW_Data() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(AccuracyEvaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void lucene_Compare_BF_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(AccuracyEvaluation.BF, AccuracyEvaluation.GT);
  }

  @Test
  public void lucene_Compare_FW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(AccuracyEvaluation.FW, AccuracyEvaluation.GT);
  }

  @Test
  public void lucene_Compare_PW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(AccuracyEvaluation.PW, AccuracyEvaluation.GT);
  }
}
