package edu.cmu.cs.mvelezce.java.processor.execution.sampling.profiler.jprofiler;

import edu.cmu.cs.mvelezce.java.processor.execution.BaseExecutionProcessor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.RawJProfilerSamplingPerfExecution;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.Hotspot;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.JProfilerSnapshotEntry;
import edu.cmu.cs.mvelezce.java.results.sampling.raw.profiler.jprofiler.snapshot.Node;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.region.manager.RegionsManager;

import java.util.*;

public abstract class JProfilerSamplingExecutionProcessor
    extends BaseExecutionProcessor<RawJProfilerSamplingPerfExecution> {

  private static final double J_PROFILER_OVERHEAD = 0.0;

  private final Map<String, String> fullyQualifiedMethodsToRegionIds = new HashMap<>();
  private final Map<JProfilerSnapshotEntry, String> snapshotEntriesToFullyQualifiedMethods =
      new HashMap<>();

  public JProfilerSamplingExecutionProcessor(
      String programName,
      Map<Integer, Set<RawJProfilerSamplingPerfExecution>> itersToRawPerfExecutions,
      Set<JavaRegion> regions,
      String measuredTime) {
    super(programName, itersToRawPerfExecutions, measuredTime);

    this.populateSnapshotEntriesToFullyQualifiedMethods();
    this.populateFullyQualifiedMethodsToRegionIds(regions);
  }

  @Override
  protected PerfExecution getProcessedPerfExec(
      RawJProfilerSamplingPerfExecution rawJProfilerSamplingPerfExecution) {
    Map<String, Long> regionToPerf = this.process(rawJProfilerSamplingPerfExecution.getHotspots());
    Set<String> config = rawJProfilerSamplingPerfExecution.getConfiguration();

    return new PerfExecution(config, regionToPerf);
  }

  private void populateSnapshotEntriesToFullyQualifiedMethods() {
    for (Map.Entry<Integer, Set<RawJProfilerSamplingPerfExecution>> entry :
        this.getItersToRawPerfExecutions().entrySet()) {
      for (RawJProfilerSamplingPerfExecution perfExec : entry.getValue()) {
        for (Hotspot hotspot : perfExec.getHotspots()) {
          String fullyQualifiedName =
              this.getHotspotFullyQualifiedName(
                  hotspot.getClassName(), hotspot.getMethodName(), hotspot.getMethodSignature());
          this.snapshotEntriesToFullyQualifiedMethods.putIfAbsent(hotspot, fullyQualifiedName);

          Deque<Node> worklist = new ArrayDeque<>(hotspot.getNodes());

          while (!worklist.isEmpty()) {
            Node node = worklist.pop();

            if (node.getClassName().isEmpty()
                && node.getMethodName().isEmpty()
                && node.getMethodSignature().isEmpty()) {
              continue;
            }

            fullyQualifiedName =
                this.getHotspotFullyQualifiedName(
                    node.getClassName(), node.getMethodName(), node.getMethodSignature());
            this.snapshotEntriesToFullyQualifiedMethods.putIfAbsent(node, fullyQualifiedName);

            worklist.addAll(node.getNodes());
          }
        }
      }
    }
  }

  private void populateFullyQualifiedMethodsToRegionIds(Set<JavaRegion> regions) {
    System.err.println("Ignoring the fact that a method can have multiple regions");
    for (JavaRegion region : regions) {
      String fullyQualifiedName =
          this.getRegionFullyQualifiedName(
              region.getRegionPackage(),
              region.getRegionClass(),
              region.getRegionMethodSignature());
      this.fullyQualifiedMethodsToRegionIds.put(fullyQualifiedName, region.getId().toString());
    }

    this.fullyQualifiedMethodsToRegionIds.put(
        BaseExecutionProcessor.TRUE_REGION, RegionsManager.PROGRAM_REGION_ID.toString());
  }

  private String getRegionFullyQualifiedName(
      String packageName, String className, String methodSignature) {
    return packageName + "." + className + "." + methodSignature;
  }

  private String getHotspotFullyQualifiedName(
      String className, String methodName, String methodSignature) {
    return className + "." + methodName + methodSignature;
  }

  private Map<String, Long> process(List<Hotspot> hotspots) {
    Map<String, Long> regionsToPerf = this.addRegions();
    this.addPerfs(regionsToPerf, hotspots);

    return regionsToPerf;
  }

  private void addPerfs(Map<String, Long> regionsToPerf, List<Hotspot> hotspots) {
    Deque<JProfilerSnapshotEntry> stack = new ArrayDeque<>();

    for (Hotspot hotspot : hotspots) {
      stack.push(hotspot);

      while (!stack.isEmpty()) {
        JProfilerSnapshotEntry entry = stack.pop();

        String fullyQualifiedName = this.snapshotEntriesToFullyQualifiedMethods.get(entry);
        String region = this.fullyQualifiedMethodsToRegionIds.get(fullyQualifiedName);

        if (region == null && !entry.getNodes().isEmpty()) {
          for (Node node : entry.getNodes()) {
            stack.push(node);
          }
        } else {
          if (region == null) {
            region = this.fullyQualifiedMethodsToRegionIds.get(BaseExecutionProcessor.TRUE_REGION);
          }

          long currentTime = regionsToPerf.get(region);
          currentTime += (entry.getTime() * 1_000 * (1 - J_PROFILER_OVERHEAD));
          regionsToPerf.put(region, currentTime);
        }
      }
    }
  }

  private Map<String, Long> addRegions() {
    Map<String, Long> regionsToPerf = new HashMap<>();

    for (String id : this.fullyQualifiedMethodsToRegionIds.values()) {
      regionsToPerf.put(id, 0L);
    }

    return regionsToPerf;
  }
}
