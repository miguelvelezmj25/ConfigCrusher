package edu.cmu.cs.mvelezce.java.processor.aggregator.sampling.profiler.jprofiler.idta;

import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;
import edu.cmu.cs.mvelezce.java.processor.aggregator.PerfAggregatorProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import edu.cmu.cs.mvelezce.region.manager.RegionsManager;
import edu.cmu.cs.mvelezce.utils.stats.DescriptiveStatisticsMap;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.*;

public class IDTAPerfAggregatorProcessor extends PerfAggregatorProcessor {

  private static final String OUTPUT_DIR = IDTAJProfilerSamplingExecutor.OUTPUT_DIR;
  private final long addedBaseTime;

  public IDTAPerfAggregatorProcessor(String programName, String measuredTime) {
    this(programName, new HashMap<>(), measuredTime, 0);
  }

  IDTAPerfAggregatorProcessor(
      String programName,
      Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecution,
      String measuredTime,
      long addedBaseTime) {
    super(programName, itersToProcessedPerfExecution, measuredTime, 0);

    this.addedBaseTime = addedBaseTime;
  }

  @Override
  protected void addAllExecutions(
      DescriptiveStatisticsMap<UUID> regionsToStats,
      Set<String> config,
      Collection<Set<PerfExecution>> allProcessedPerfExecutions) {
    super.addAllExecutions(regionsToStats, config, allProcessedPerfExecutions);

    for (Map.Entry<UUID, DescriptiveStatistics> entry : regionsToStats.getMap().entrySet()) {
      UUID region = entry.getKey();

      if (!region.equals(RegionsManager.PROGRAM_REGION_ID)) {
        continue;
      }

      double[] values = entry.getValue().getValues();
      DescriptiveStatistics descriptiveStats = new DescriptiveStatistics();

      for (double value : values) {
        descriptiveStats.addValue(this.addedBaseTime + value);
      }

      regionsToStats.put(region, descriptiveStats);
      break;
    }
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR
        + "/"
        + this.getMeasuredTime()
        + "/"
        + this.getProgramName()
        + PerfAggregatorProcessor.OUTPUT_DIR;
  }
}
