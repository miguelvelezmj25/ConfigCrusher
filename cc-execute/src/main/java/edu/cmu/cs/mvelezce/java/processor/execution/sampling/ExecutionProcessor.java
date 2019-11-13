package edu.cmu.cs.mvelezce.java.processor.execution.sampling;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.java.processor.execution.BaseExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawPerfExecution;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.Hotspot;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ExecutionProcessor extends BaseExecutionProcessor<RawPerfExecution> {

  private final Set<JavaRegion> regions;

  public ExecutionProcessor(
      String programName,
      Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions,
      Set<JavaRegion> regions) {
    super(programName, itersToRawPerfExecutions);

    this.regions = regions;
  }

  @Override
  protected ProcessedPerfExecution getProcessedPerfExec(RawPerfExecution rawPerfExecution) {
    Map<String, Long> regionToPerf = this.process(rawPerfExecution.getHotspots());
    Set<String> config = rawPerfExecution.getConfiguration();

    return new ProcessedPerfExecution(config, regionToPerf);
  }

  private Map<String, Long> process(List<Hotspot> hotspots) {
    throw new UnsupportedOperationException("implement");
  }
}
