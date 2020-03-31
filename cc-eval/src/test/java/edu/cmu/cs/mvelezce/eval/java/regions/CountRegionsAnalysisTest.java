package edu.cmu.cs.mvelezce.eval.java.regions;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.builder.idta.IDTAPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.BerkeleyIDTAPerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.ConvertIDTAPerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.LuceneIDTAPerformanceModel;
import edu.cmu.cs.mvelezce.models.idta.RunBenchCIDTAPerformanceModel;
import org.junit.Test;

import java.io.IOException;

public class CountRegionsAnalysisTest {

  @Test
  public void berkeleyDB_real_total() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = BerkeleyIDTAPerformanceModel.toBerkeleyIDTAPerformanceModel(model);

    CountRegionsAnalysis.countRegions(model.getLocalModels());
  }

  @Test
  public void berkeleyDB_real_perf() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = BerkeleyIDTAPerformanceModel.toBerkeleyIDTAPerformanceModel(model);

    CountRegionsAnalysis.countPerfRelevantRegions(model.getLocalModels());
  }

  @Test
  public void lucene_real_total() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = LuceneIDTAPerformanceModel.toLuceneIDTAPerformanceModel(model);

    CountRegionsAnalysis.countRegions(model.getLocalModels());
  }

  @Test
  public void lucene_real_perf() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.REAL);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = LuceneIDTAPerformanceModel.toLuceneIDTAPerformanceModel(model);

    CountRegionsAnalysis.countPerfRelevantRegions(model.getLocalModels());
  }

  @Test
  public void convert_user_total() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.USER);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = ConvertIDTAPerformanceModel.toConvertIDTAPerformanceModel(model);

    CountRegionsAnalysis.countRegions(model.getLocalModels());
  }

  @Test
  public void convert_user_perf() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.USER);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = ConvertIDTAPerformanceModel.toConvertIDTAPerformanceModel(model);

    CountRegionsAnalysis.countPerfRelevantRegions(model.getLocalModels());
  }

  @Test
  public void runBench_user_total() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.USER);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = RunBenchCIDTAPerformanceModel.toRunBenchCIDTAPerformanceModel(model);

    CountRegionsAnalysis.countRegions(model.getLocalModels());
  }

  @Test
  public void runBench_user_perf() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName, BaseExecutor.USER);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);
    model = RunBenchCIDTAPerformanceModel.toRunBenchCIDTAPerformanceModel(model);

    CountRegionsAnalysis.countPerfRelevantRegions(model.getLocalModels());
  }
}
