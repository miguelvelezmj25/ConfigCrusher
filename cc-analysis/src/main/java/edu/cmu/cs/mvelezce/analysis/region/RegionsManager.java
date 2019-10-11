package edu.cmu.cs.mvelezce.analysis.region;

import jdk.internal.org.objectweb.asm.Type;

public class RegionsManager {

  public static final String INTERNAL_NAME = Type.getInternalName(RegionsManager.class);
  public static final String ENTER_REGION = "enter";
  public static final String EXIT_REGION = "exit";
  public static final String REGION_DESCRIPTOR = "(Ljava/lang/String;)V";

  //  public static final String PROGRAM_REGION_ID = "program";
  //  public static Map<String, Long> regionsToOverhead = new HashMap<>();
  //  private static Stack<String> executingRegions = new Stack<>();
  //  private static Stack<Long> executingRegionsStart = new Stack<>();
  //  private static Stack<Long> innerRegionsExecutionTime = new Stack<>();
  //  private static Map<String, Long> regionsToProcessedPerformance = new HashMap<>();
  //
  //  public static void enter(String id) {
  //    long os = System.nanoTime();
  //
  //    long start = System.nanoTime();
  //    Regions.executingRegions.push(id);
  //    Regions.executingRegionsStart.push(start);
  //    Regions.innerRegionsExecutionTime.push(0L);
  //
  //    long overhead = regionsToOverhead.get(id);
  //    long oe = System.nanoTime();
  //    regionsToOverhead.put(id, overhead + (oe - os));
  //  }
  //
  //  public static void exit(String id) {
  //    long os = System.nanoTime();
  //
  //    if (!Regions.executingRegions.peek().equals(id)) {
  //      long overhead = regionsToOverhead.get(id);
  //      long oe = System.nanoTime();
  //      regionsToOverhead.put(id, overhead + (oe - os));
  //      return;
  //    }
  //
  //    long end = System.nanoTime();
  //    long start = Regions.executingRegionsStart.pop();
  //    long innerTime = Regions.innerRegionsExecutionTime.pop();
  //    long regionExecutionTime = end - start - innerTime;
  //    long currentRegionExecutionTime = regionExecutionTime;
  //
  //    String region = Regions.executingRegions.pop();
  //    Long time = Regions.regionsToProcessedPerformance.get(region);
  //
  //    if (time != null) {
  //      regionExecutionTime += time;
  //    }
  //
  //    Regions.regionsToProcessedPerformance.put(region, regionExecutionTime);
  //
  //    if (executingRegions.isEmpty()) {
  //      return;
  //    }
  //
  //    Stack<Long> added = new Stack<>();
  //
  //    while (!innerRegionsExecutionTime.isEmpty()) {
  //      long currentInnerRegionExecutionTime = innerRegionsExecutionTime.pop();
  //      added.push(currentInnerRegionExecutionTime + currentRegionExecutionTime);
  //    }
  //
  //    while (!added.isEmpty()) {
  //      innerRegionsExecutionTime.push(added.pop());
  //    }
  //
  //    long overhead = regionsToOverhead.get(id);
  //    long oe = System.nanoTime();
  //    regionsToOverhead.put(id, overhead + (oe - os));
  //  }
  //
  //  public static Map<String, Long> getRegionsToProcessedPerformance() {
  //    return regionsToProcessedPerformance;
  //  }
}
