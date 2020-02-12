package edu.cmu.cs.mvelezce.eval.java.time;

import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class TimeEvaluation {

  private TimeEvaluation() {}

  static void getMeasuredTime(String approach, Set<PerformanceEntry> performanceEntries) {
    double totalTime = 0.0;

    for (PerformanceEntry entry : performanceEntries) {
      double entryTime = 0.0;
      Map<UUID, Double> regionsToPerf = entry.getRegionsToPerf();

      for (double time : regionsToPerf.values()) {
        entryTime += time;
      }

      System.out.println(
          "It took "
              + Evaluation.DECIMAL_FORMAT.format(entryTime / 1E9)
              + " secs. to measure "
              + entry.getConfiguration());

      totalTime += entryTime;
    }

    System.out.println();
    System.out.println(
        "It took "
            + Evaluation.DECIMAL_FORMAT.format(totalTime / 1E9)
            + " secs. to measure "
            + performanceEntries.size()
            + " configs with "
            + approach);
  }

  static void getE2EMeasuredTime(String approach, Set<PerformanceEntry> performanceEntries) {
    double totalTime = 0.0;

    for (PerformanceEntry entry : performanceEntries) {
      Map<UUID, Double> regionsToPerf = entry.getRegionsToPerf();

      if (regionsToPerf.size() != 1) {
        throw new RuntimeException("E2E models should only have 1 region");
      }

      totalTime += regionsToPerf.values().iterator().next();
    }

    System.out.println(
        "It took "
            + Evaluation.DECIMAL_FORMAT.format(totalTime / 1E9)
            + " secs. to measure "
            + performanceEntries.size()
            + " configs with "
            + approach);
  }
}
