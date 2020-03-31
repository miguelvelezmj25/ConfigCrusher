package edu.cmu.cs.mvelezce.eval.java.regions;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;

import java.util.Map;
import java.util.Set;

public class CountRegionsAnalysis {

  public static void countRegions(Set<LocalPerformanceModel<Partition>> localModels) {
    System.out.println("There are " + localModels.size() + " regions");
  }

  public static void countPerfRelevantRegions(Set<LocalPerformanceModel<Partition>> localModels) {
    int numPerfRegions = 0;

    for (LocalPerformanceModel<Partition> localModel : localModels) {
      Map<Partition, Double> model = localModel.getModel();

      for (double perf : model.values()) {
        if (perf >= 1E8) {
          numPerfRegions++;
          break;
        }
      }
    }

    System.out.println("There are " + numPerfRegions + " performance-relevant regions");
  }
}
