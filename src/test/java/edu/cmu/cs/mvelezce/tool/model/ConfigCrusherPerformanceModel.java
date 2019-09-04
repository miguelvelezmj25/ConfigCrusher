package edu.cmu.cs.mvelezce.tool.model;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.timer.DynamicConfigCrusherTimerRegionInstrumenter;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class ConfigCrusherPerformanceModel {

  @Test
  public void runningExampleConfigCrusher() throws Exception {
    String programName = TrivialAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    BaseRegionInstrumenter<Set<Set<String>>> instrumenter =
        new DynamicConfigCrusherTimerRegionInstrumenter(programName);
    instrumenter.instrument(args);
    Map<JavaRegion, Set<Set<String>>> regionsToOptionSet = instrumenter.getRegionsToData();

    System.out.println();

    //    RegionAnalysis<Set<Set<String>>> analysis = new DefaultStaticAnalysis();
    //    Map<Region, Set<Set<String>>> regionsToOptionSet =
    // analysis.transform(javaRegionsToOptionSet);
    //
    //    Executor executor = new ConfigCrusherExecutor(programName);
    //    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
    //
    //    Set<String> options = new HashSet<>(RunningExampleAdapter.getRunningExampleOptions());
    //    Set<Set<String>> configurations = BruteForceExecutor
    //        .getBruteForceConfigurationsFromOptions(options);
    //
    //    args = new String[2];
    //    args[0] = "-delres";
    //    args[1] = "-saveres";
    //
    //    PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName,
    //        measuredPerformance,
    //        regionsToOptionSet);
    //    PerformanceModel performanceModel = builder.createModel(args);
  }
}
