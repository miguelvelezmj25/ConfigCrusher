package edu.cmu.cs.mvelezce.eval.java.config;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.builder.idta.IDTAPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions.IDTASuboptimalGreedyConjunctionsCompression;
import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta.IDTAPerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigAnalysisTest {

  @Test
  public void measured_idta_0() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    Set<String> config = new HashSet<>();
    config.add("RAM_BUFFER_SIZE_MB");

    ConfigAnalysis configAnalysis = new ConfigAnalysis(programName);
    configAnalysis.some(Evaluation.IDTA, performanceEntries, config);
  }

  @Test
  public void compare_idta_model_lucene_0() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    ConfigAnalysis configAnalysis = new ConfigAnalysis(programName, options);

    Set<String> config = new HashSet<>();
    config.add("RAM_BUFFER_SIZE_MB");
    configAnalysis.compareMeasurementAndPrediction(
        Evaluation.IDTA, performanceEntries, model, config);
  }

  @Test
  public void compare_idta_model_lucene_all() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    ConfigAnalysis configAnalysis = new ConfigAnalysis(programName, options);

    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    args = new String[0];
    Set<Set<String>> executedConfigs = compression.analyze(args);

    for (Set<String> config : executedConfigs) {
      configAnalysis.compareMeasurementAndPrediction(
          Evaluation.IDTA, performanceEntries, model, config);
    }
  }

  @Test
  public void compare_idta_model_berkeley_all() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Analysis<Set<PerformanceEntry>> perfAggregatorProcessor =
        new IDTAPerfAggregatorProcessor(programName);
    String[] args = new String[0];
    Set<PerformanceEntry> performanceEntries = perfAggregatorProcessor.analyze(args);

    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName);
    args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    ConfigAnalysis configAnalysis = new ConfigAnalysis(programName, options);

    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    args = new String[0];
    Set<Set<String>> executedConfigs = compression.analyze(args);

    for (Set<String> config : executedConfigs) {
      configAnalysis.compareMeasurementAndPrediction(
          Evaluation.IDTA, performanceEntries, model, config);
    }
  }
}
