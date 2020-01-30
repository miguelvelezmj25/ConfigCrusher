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
import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.eval.java.accuracy.AccuracyEvaluation;
import edu.cmu.cs.mvelezce.exhaustive.builder.bf.BruteForceExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.exhaustive.builder.gt.GroundTruthExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.learning.builder.model.matlab.MatlabLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccuracyConstraintEvaluationTest {

  @Test
  public void berkeleyDB_GT0_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configs.add(config);
    }

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_GT1_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configs.add(config);
    }

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_GT2_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configs.add(config);
    }

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_GT3_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configs.add(config);
    }

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

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
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
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
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.IDTA, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW0_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 0);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<FeatureExpr> eval =
        new AccuracyConstraintEvaluation(programName + 0, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW1_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 1);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<FeatureExpr> eval =
        new AccuracyConstraintEvaluation(programName + 1, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW2_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 2);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<FeatureExpr> eval =
        new AccuracyConstraintEvaluation(programName + 2, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW3_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 3);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (FeatureExpr entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry, options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<FeatureExpr> eval =
        new AccuracyConstraintEvaluation(programName + 3, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
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

    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_Compare_IDTA_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_BF_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT0() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT1() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT2() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT3() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
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
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
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
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, configs, model);
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
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
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
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void lucene_Compare_BF_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT);
  }

  @Test
  public void lucene_Compare_FW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void lucene_Compare_PW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<FeatureExpr> eval = new AccuracyConstraintEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }
}
