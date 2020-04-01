package edu.cmu.cs.mvelezce.eval.java.accuracy.partition;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
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
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.learning.builder.model.matlab.MatlabLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.BerkeleyIDTAPerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.ConvertIDTAPerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.LuceneIDTAPerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.RunBenchCIDTAPerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class AccuracyPartitionEvaluationTest {

  //  @Test
  //  public void berkeleyDB_GT0_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configs = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configs.add(config);
  //    }
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
  //    eval.saveConfigsToPerformanceExhaustive(
  //        Evaluation.GT, BaseExecutor.REAL, configs, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_GT1_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configs = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configs.add(config);
  //    }
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
  //    eval.saveConfigsToPerformanceExhaustive(
  //        Evaluation.GT, BaseExecutor.REAL, configs, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_GT2_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configs = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configs.add(config);
  //    }
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
  //    eval.saveConfigsToPerformanceExhaustive(
  //        Evaluation.GT, BaseExecutor.REAL, configs, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_GT3_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configs = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configs.add(config);
  //    }
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
  //    eval.saveConfigsToPerformanceExhaustive(
  //        Evaluation.GT, BaseExecutor.REAL, configs, model);
  //  }

  @Test
  public void berkeleyDB_GT_Data_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, BaseExecutor.REAL, configs, model);
  }

  @Test
  public void berkeleyDB_BF_Data_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new BruteForceExhaustiveModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, BaseExecutor.REAL, configs, model);
  }

  @Test
  public void berkeleyDB_IDTA_Data_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = BerkeleyIDTAPerformanceModel.toBerkeleyIDTAPerformanceModel(model);

    BaseCompression gtCompression = new GTCompression(programName);
    args = new String[0];
    Set<Set<String>> configsToPredict = gtCompression.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.IDTA, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  //  @Test
  //  public void berkeleyDB_IDTA0_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    BaseCompression idtaCompression = new
  // IDTASuboptimalGreedyConjunctionsCompression(programName);
  //    String[] args = new String[0];
  //    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 0, BaseExecutor.REAL);
  //    args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder = new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 0,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.IDTA, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_IDTA1_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    BaseCompression idtaCompression = new
  // IDTASuboptimalGreedyConjunctionsCompression(programName);
  //    String[] args = new String[0];
  //    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 1, BaseExecutor.REAL);
  //    args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder = new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 1,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.IDTA, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_IDTA2_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    BaseCompression idtaCompression = new
  // IDTASuboptimalGreedyConjunctionsCompression(programName);
  //    String[] args = new String[0];
  //    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 2, BaseExecutor.REAL);
  //    args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder = new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 2,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.IDTA, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_IDTA3_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    BaseCompression idtaCompression = new
  // IDTASuboptimalGreedyConjunctionsCompression(programName);
  //    String[] args = new String[0];
  //    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 3, BaseExecutor.REAL);
  //    args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder = new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 3,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.IDTA, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_FW0_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 0, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 0,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.FW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_FW1_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 1, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 1,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.FW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_FW2_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 2, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 2,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.FW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_FW3_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 3, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 3,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.FW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }

  @Test
  public void berkeleyDB_FW_Data_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.FW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  @Test
  public void berkeleyDB_PW_Data_real() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.PW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  //  @Test
  //  public void berkeleyDB_PW0_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 0, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 0,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.PW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_PW1_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 1, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 1,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.PW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_PW2_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 2, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 2,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.PW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_PW3_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
  //    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
  //    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
  //    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName + 3, BaseExecutor.REAL);
  //    String[] args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    Set<Set<String>> configsToPredict = new HashSet<>();
  //
  //    for (Partition entry : model.getLocalModels().iterator().next().getModel().keySet()) {
  //      Set<String> config = ConstraintUtils.toConfig(entry.getFeatureExpr(), options);
  //      configsToPredict.add(config);
  //    }
  //
  //    builder =
  //        new MatlabLinearLearnedModelBuilder(
  //            programName, samplingApproach, BaseExecutor.REAL);
  //    args = new String[0];
  //    model = builder.analyze(args);
  //
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName + 3,
  // options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.PW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }

  @Test
  public void berkeleyDB_Compare_IDTA_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.REAL);
  }

  //  @Test
  //  public void berkeleyDB_Compare_IDTA_GT0() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_IDTA_GT1() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_IDTA_GT2() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_IDTA_GT3() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.REAL);
  //  }

  @Test
  public void berkeleyDB_Compare_BF_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void berkeleyDB_Compare_FW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.REAL);
  }

  //  @Test
  //  public void berkeleyDB_Compare_FW_GT0() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_FW_GT1() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_FW_GT2() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_FW_GT3() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.REAL);
  //  }

  @Test
  public void berkeleyDB_Compare_PW_GT() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.REAL);
  }

  //  @Test
  //  public void berkeleyDB_Compare_PW_GT0() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 0;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_PW_GT1() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 1;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_PW_GT2() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 2;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.REAL);
  //  }
  //
  //  @Test
  //  public void berkeleyDB_Compare_PW_GT3() throws IOException {
  //    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME + 3;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.REAL);
  //  }

  @Test
  public void lucene_GT_Data_real() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, BaseExecutor.REAL, configs, model);
  }

  @Test
  public void lucene_BF_Data_real() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new BruteForceExhaustiveModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, BaseExecutor.REAL, configs, model);
  }

  @Test
  public void lucene_FW_Data_real() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.FW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  @Test
  public void lucene_PW_Data_real() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.PW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  @Test
  public void lucene_IDTA_Data_real() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = LuceneIDTAPerformanceModel.toLuceneIDTAPerformanceModel(model);

    BaseCompression gtCompression = new GTCompression(programName);
    args = new String[0];
    Set<Set<String>> configsToPredict = gtCompression.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.IDTA, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  @Test
  public void lucene_Compare_BF_GT_real() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void lucene_Compare_IDTA_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void lucene_Compare_FW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void lucene_Compare_PW_GT() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.REAL);
  }

  //  @Test
  //  public void multithread_GT_Data_real() throws IOException, InterruptedException {
  //    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
  //    BaseCompression compression = new GTCompression(programName);
  //    String[] args = new String[0];
  //    Set<Set<String>> configs = compression.analyze(args);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
  //    args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //
  //    List<String> options = BaseMultithreadAdapter.getListOfOptions();
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
  //    eval.saveConfigsToPerformanceExhaustive(
  //        Evaluation.GT, BaseExecutor.REAL, configs, model);
  //  }
  //
  //  @Test
  //  public void multithread_IDTA_Data() throws IOException, InterruptedException {
  //    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
  //    BaseCompression idtaCompression = new
  // IDTASuboptimalGreedyConjunctionsCompression(programName);
  //    String[] args = new String[0];
  //    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);
  //
  //    BaseAnalysis<PerformanceModel<Partition>> builder =
  //        new IDTAPerformanceModelBuilder(programName);
  //    args = new String[0];
  //    PerformanceModel<Partition> model = builder.analyze(args);
  //    model = LuceneIDTAPerformanceModel.toLuceneIDTAPerformanceModel(model);
  //
  //    BaseCompression gtCompression = new GTCompression(programName);
  //    args = new String[0];
  //    Set<Set<String>> configsToPredict = gtCompression.analyze(args);
  //
  //    List<String> options = BaseMultithreadAdapter.getListOfOptions();
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
  //    eval.saveConfigsToPerformance(
  //        Evaluation.IDTA, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  //  }
  //
  //  @Test
  //  public void multithread_Compare_IDTA_GT() throws IOException {
  //    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
  //    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
  //    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.REAL);
  //  }

  @Test
  public void convert_GT_Data_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, BaseExecutor.USER, configs, model);
  }

  @Test
  public void convert_BF_Data_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new BruteForceExhaustiveModelBuilder(programName, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, BaseExecutor.USER, configs, model);
  }

  @Test
  public void convert_FW_Data_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.FW, BaseExecutor.USER, executedConfigs, configsToPredict, model);
  }

  @Test
  public void convert_PW_Data_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.PW, BaseExecutor.USER, executedConfigs, configsToPredict, model);
  }

  @Test
  public void convert_IDTA_Data_user() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = ConvertIDTAPerformanceModel.toConvertIDTAPerformanceModel(model);

    BaseCompression gtCompression = new GTCompression(programName);
    args = new String[0];
    Set<Set<String>> configsToPredict = gtCompression.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.IDTA, BaseExecutor.USER, executedConfigs, configsToPredict, model);
  }

  @Test
  public void convert_Compare_BF_GT_user() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT, BaseExecutor.USER);
  }

  @Test
  public void convert_Compare_FW_GT() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.USER);
  }

  @Test
  public void convert_Compare_PW_GT() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.USER);
  }

  @Test
  public void convert_Compare_IDTA_GT() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, Evaluation.GT, BaseExecutor.USER);
  }

  @Test
  public void runBenchC_GT_Data_real() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, BaseExecutor.REAL, configs, model);
  }

  @Test
  public void runBenchC_BF_Data_real() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new BruteForceExhaustiveModelBuilder(programName, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, BaseExecutor.REAL, configs, model);
  }

  @Test
  public void runBenchC_FW_Data_real() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    SamplingApproach samplingApproach = FeatureWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.FW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  @Test
  public void runBenchC_PW_Data_real() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    SamplingApproach samplingApproach = PairWiseSampling.getInstance();
    Set<Set<String>> executedConfigs = samplingApproach.getConfigs(options);

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configsToPredict = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new MatlabLinearLearnedModelBuilder(programName, samplingApproach, BaseExecutor.REAL);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.PW, BaseExecutor.REAL, executedConfigs, configsToPredict, model);
  }

  @Test
  public void runBenchC_GT_Data_user() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new GroundTruthExhaustiveModelBuilder(programName, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.GT, BaseExecutor.USER, configs, model);
  }

  @Test
  public void runBenchC_BF_Data_user() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new BruteForceExhaustiveModelBuilder(programName, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformanceExhaustive(Evaluation.BF, BaseExecutor.USER, configs, model);
  }

  @Test
  public void runBenchC_IDTA_Data_user() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression idtaCompression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> executedConfigs = idtaCompression.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.USER);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = RunBenchCIDTAPerformanceModel.toRunBenchCIDTAPerformanceModel(model);

    BaseCompression gtCompression = new GTCompression(programName);
    args = new String[0];
    Set<Set<String>> configsToPredict = gtCompression.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName, options);
    eval.saveConfigsToPerformance(
        Evaluation.IDTA, BaseExecutor.USER, executedConfigs, configsToPredict, model);
  }

  @Test
  public void runBenchC_Compare_BF_GT_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void runBenchC_Compare_FW_GT_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.FW, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void runBenchC_Compare_PW_GT_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.PW, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void runBenchC_Compare_IDTA_user_GT_real() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.IDTA, BaseExecutor.USER, Evaluation.GT, BaseExecutor.REAL);
  }

  @Test
  public void runBenchC_Compare_BF_GT_user() throws IOException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    AccuracyEvaluation<Partition> eval = new AccuracyPartitionEvaluation(programName);
    eval.compareApproaches(Evaluation.BF, Evaluation.GT, BaseExecutor.USER);
  }
}
