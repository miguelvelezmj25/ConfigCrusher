package edu.cmu.cs.mvelezce.model;

import java.util.HashSet;
import java.util.Set;

public class PerformanceModel<T> implements IPerformanceModel<T> {

  private final Set<LocalPerformanceModel<T>> localModels;

  // Dummy constructor for jackson xml
  private PerformanceModel() {
    this.localModels = new HashSet<>();
  }

  public PerformanceModel(Set<LocalPerformanceModel<T>> localModels) {
    this.localModels = localModels;
  }

  public Set<LocalPerformanceModel<T>> getLocalModels() {
    return localModels;
  }

  @Override
  public double evaluate(String config) {
    throw new UnsupportedOperationException("Implement");
    //    double performance = this.baseTimeHumanReadable;
    //
    //    for(Map.Entry<Region, Map<Set<String>, Double>> entry :
    // this.regionsToPerformanceTablesHumanReadable.entrySet()) {
    //      Set<String> optionsInRegion = new HashSet<>();
    //
    //      for(Set<String> options : entry.getValue().keySet()) {
    //        optionsInRegion.addAll(options);
    //      }
    //
    //      Set<String> configurationValueInRegion = new HashSet<>(configuration);
    //      configurationValueInRegion.retainAll(optionsInRegion);
    //
    //      for(Map.Entry<Set<String>, Double> configurationToPerformance :
    // entry.getValue().entrySet()) {
    //        if(configurationToPerformance.getKey().equals(configurationValueInRegion)) {
    //          performance += configurationToPerformance.getValue();
    //        }
    //      }
    //    }
    //
    //    DecimalFormat decimalFormat = new DecimalFormat("#.###");
    //    String perfString = decimalFormat.format(performance);
    //    performance = Double.valueOf(perfString);
    //
    //    return performance;

  }
}
