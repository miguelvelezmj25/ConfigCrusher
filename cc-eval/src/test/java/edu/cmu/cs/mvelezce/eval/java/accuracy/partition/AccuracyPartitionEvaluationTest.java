package edu.cmu.cs.mvelezce.eval.java.accuracy.partition;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
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
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.learning.builder.model.matlab.MatlabLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccuracyPartitionEvaluationTest {

  @Test
  public void berkeleyDB_GT0_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configs.add(config);
    }

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_GT1_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configs.add(config);
    }

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_GT2_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configs.add(config);
    }

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_GT3_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configs.add(config);
    }

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_GT_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void berkeleyDB_BF_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new BruteForceExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, configs, model);
  }

  @Test
  public void berkeleyDB_IDTA_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    BaseCompression gtCompression = new GTCompression(programName);
    args = new String[0];
    Set<Set<String>> configsToPredict = gtCompression.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.IDTA, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW0_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 0);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 0, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW1_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 1);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 1, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW2_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 2);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 2, options);
    eval.saveConfigsToPerformance(Evaluation.FW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_FW3_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 3);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 3, options);
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

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
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

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_PW0_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 0);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 0, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_PW1_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 1);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 1, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_PW2_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 2);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 2, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_PW3_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName + 3);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    Set<Set<String>> configsToPredict = new HashSet<>();

    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
      configsToPredict.add(config);
    }

    builder = new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 3, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_Compare_IDTA_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_BF_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT0() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT1() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT2() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT3() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT0() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT1() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT2() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }

  @Test
  public void berkeleyDB_Compare_PW_GT3() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }

  @Test
  public void lucene_GT_Data() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }

  @Test
  public void lucene_BF_Data() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new BruteForceExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
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

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
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

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.PW, executedConfigs, configsToPredict, model);
  }

  @Test
  public void lucene_Compare_BF_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT);
  }

  @Test
  public void lucene_Compare_FW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT);
  }

  @Test
  public void lucene_Compare_PW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT);
  }

  @Test
  public void density_GT_Data() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, configs, model);
  }
}
