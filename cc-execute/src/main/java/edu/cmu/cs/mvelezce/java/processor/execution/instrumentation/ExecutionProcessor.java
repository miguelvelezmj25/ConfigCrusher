package edu.cmu.cs.mvelezce.java.processor.execution.instrumentation;

import edu.cmu.cs.mvelezce.java.processor.execution.BaseExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.instrumentation.raw.RawPerfExecution;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.region.RegionsManager;

import java.util.*;

public abstract class ExecutionProcessor extends BaseExecutionProcessor<RawPerfExecution> {

  public ExecutionProcessor(
      String programName, Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions) {
    super(programName, itersToRawPerfExecutions);
  }

  @Override
  protected ProcessedPerfExecution getProcessedPerfExec(RawPerfExecution rawPerfExecution) {
    Map<String, Long> regionToPerf = this.process(rawPerfExecution.getTrace());
    Set<String> config = rawPerfExecution.getConfiguration();

    return new ProcessedPerfExecution(config, regionToPerf);
  }

  private Map<String, Long> process(List<String> trace) {
    Map<String, Long> regionsToPerf = this.addRegions(trace);
    this.addPerfs(regionsToPerf, trace);

    return regionsToPerf;
  }

  private void addPerfs(Map<String, Long> regionsToPerf, List<String> trace) {
    Deque<String> executingRegions = new ArrayDeque<>();
    Deque<Long> executionRegionsStart = new ArrayDeque<>();
    Deque<Long> nestedRegionsExecTime = new ArrayDeque<>();

    for (int i = 0; i < trace.size(); i++) {
      String entry = trace.get(i);
      String[] items = entry.split(RegionsManager.COMMA);
      String action = items[0];
      String region = items[1];
      long time = Long.parseLong(items[2]);

      if (action.equals(RegionsManager.START)) {
        executingRegions.push(region);
        executionRegionsStart.push(time);
        nestedRegionsExecTime.push(0L);
      } else {
        if (!region.equals(executingRegions.peek())) {
          throw new RuntimeException("Exiting a region that we did not expected");
        }

        executingRegions.pop();

        long regionStart = executionRegionsStart.pop();
        long nestedRegionTime = nestedRegionsExecTime.pop();
        long regionExecTime = time - regionStart - nestedRegionTime;

        long currentTime = regionsToPerf.get(region);
        currentTime += regionExecTime;
        regionsToPerf.put(region, currentTime);

        if (executingRegions.isEmpty()) {
          continue;
        }

        Deque<Long> tmp = new ArrayDeque<>(nestedRegionsExecTime.size());

        while (!nestedRegionsExecTime.isEmpty()) {
          long nestedRegionExecTime = nestedRegionsExecTime.pop();
          tmp.push(nestedRegionExecTime + regionExecTime);
        }

        while (!tmp.isEmpty()) {
          long exec = tmp.pop();
          nestedRegionsExecTime.push(exec);
        }
      }
    }
  }

  private Map<String, Long> addRegions(List<String> trace) {
    Map<String, Long> regionsToPerf = new HashMap<>();

    for (String entry : trace) {
      String[] items = entry.split(RegionsManager.COMMA);
      regionsToPerf.put(items[1].trim(), 0L);
    }

    return regionsToPerf;
  }
}
