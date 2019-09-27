package edu.cmu.cs.mvelezce.evaluation.programs.trivial;

import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.evaluation.EvaluationTest;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.UsesBFExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.timer.DynamicConfigCrusherTimerRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.ConfigCrusherPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class TrivialEvaluationTest extends EvaluationTest {

  @Test
  public void trivialDynamicConfigCrusher() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    BaseRegionInstrumenter<Set<Set<String>>> instrumenter =
        new DynamicConfigCrusherTimerRegionInstrumenter(programName);
    instrumenter.instrument(args);
    Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToData();

    Map<Region, Set<Set<String>>> regionsToOptionSet =
        BaseDynamicRegionAnalysis.changToRegions(javaRegionsToOptionSet);

    Executor executor = new ConfigCrusherExecutor(programName);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    System.err.println("Might need to create a different PM builder based on the dynamic info");
    PerformanceModelBuilder builder =
        new ConfigCrusherPerformanceModelBuilder(
            programName, measuredPerformance, regionsToOptionSet);
    PerformanceModel performanceModel = builder.createModel(args);

    Evaluation eval = new Evaluation(programName);

    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    eval.writeConfigurationToPerformance(
        Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
  }

  @Test
  public void trivialBruteForce() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;
    Executor executor = new UsesBFExecutor(programName);

    String[] args = new String[0];
    Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

    Evaluation eval = new Evaluation(programName);
    eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
  }

  @Test
  public void trivialGroundTruth() throws IOException, InterruptedException {
    this.analyzeGroundTruth(TrivialAdapter.PROGRAM_NAME);
  }

  @Test
  public void compareAllTrivial0() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;

    Evaluation eval = new Evaluation(programName);
    eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH, true);
  }

  @Test
  public void compareAllTrivial1() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;

    Evaluation eval = new Evaluation(programName);
    eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH, true);
  }
}
