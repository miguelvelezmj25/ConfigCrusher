package edu.cmu.cs.mvelezce.analysis.region;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseCountRegionsPerMethodAnalysis<T> {

  private final Map<JavaRegion, T> regionsToData;

  public BaseCountRegionsPerMethodAnalysis(Map<JavaRegion, T> regionsToData) {
    this.regionsToData = regionsToData;
  }

  public Map<String, Integer> analyze() {
    return this.getRegionCountsPerMethod();
  }

  private Map<String, Integer> getRegionCountsPerMethod() {
    Map<String, Integer> regionsToCount = new HashMap<>();

    for (JavaRegion region : this.regionsToData.keySet()) {
      String fullyQualifiedMethod = this.getFullyQualifiedMethod(region);
      regionsToCount.putIfAbsent(fullyQualifiedMethod, 0);

      int currentCount = regionsToCount.get(fullyQualifiedMethod);
      currentCount += 1;
      regionsToCount.put(fullyQualifiedMethod, currentCount);
    }

    return regionsToCount;
  }

  private String getFullyQualifiedMethod(JavaRegion region) {
    return region.getRegionPackage()
        + "."
        + region.getRegionClass()
        + region.getRegionMethodSignature();
  }

  public void listMethodsWithMultipleRegions(Map<String, Integer> methodsToRegionCounts) {
    for (Map.Entry<String, Integer> entry : methodsToRegionCounts.entrySet()) {
      int regionsInMethod = entry.getValue();

      if (regionsInMethod == 1) {
        continue;
      }

      System.out.println(entry.getKey() + " has " + regionsInMethod + " regions");
    }
  }
}
