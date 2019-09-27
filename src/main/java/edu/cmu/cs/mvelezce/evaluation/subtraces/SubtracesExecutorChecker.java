package edu.cmu.cs.mvelezce.evaluation.subtraces;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Check that all subtraces are explorer with the given configurations. */
public abstract class SubtracesExecutorChecker {

  private final Set<SubtraceAnalysisInfo> subtraceAnalysisInfos;
  private final Set<Set<String>> configsToExecute;

  protected SubtracesExecutorChecker(
      Set<SubtraceAnalysisInfo> subtraceAnalysisInfos, Set<Set<String>> configsToExecute) {
    this.subtraceAnalysisInfos = subtraceAnalysisInfos;
    this.configsToExecute = configsToExecute;
  }

  public void analyze() {
    Map<String, String> unexploredSubtracesAndValues = new HashMap<>();

    for (SubtraceAnalysisInfo subtraceAnalysisInfo : this.subtraceAnalysisInfos) {
      Map<String, String> unexploredSubtraces =
          this.checkExploredAllSubtraces(
              subtraceAnalysisInfo.getSubtrace(), subtraceAnalysisInfo.getValuesToConfigs());
      unexploredSubtracesAndValues.putAll(unexploredSubtraces);
    }

    this.printUnexploredSubtraces(unexploredSubtracesAndValues);
  }

  private void printUnexploredSubtraces(Map<String, String> unexploredSubtracesAndValues) {
    if (unexploredSubtracesAndValues.isEmpty()) {
      System.out.println("Explored all subtraces!");

      return;
    }

    System.out.println("Did not explore the following subtraces");

    for (Map.Entry<String, String> entry : unexploredSubtracesAndValues.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  private Map<String, String> checkExploredAllSubtraces(
      String subtrace, Map<String, Set<Set<String>>> valuesToConfigs) {
    Map<String, String> unexploredSubtracesAndValues = new HashMap<>();

    for (Map.Entry<String, Set<Set<String>>> entry : valuesToConfigs.entrySet()) {
      Set<Set<String>> configsThatExplore = entry.getValue();

      if (configsThatExplore.isEmpty()) {
        continue;
      }

      boolean exploredSubtraceValue = this.checkExploredSubtraceValue(configsThatExplore);

      if (exploredSubtraceValue) {
        continue;
      }

      unexploredSubtracesAndValues.put(subtrace, entry.getKey());
    }

    return unexploredSubtracesAndValues;
  }

  private boolean checkExploredSubtraceValue(Set<Set<String>> configsThatExplore) {
    for (Set<String> configToExecute : this.configsToExecute) {
      if (configsThatExplore.contains(configToExecute)) {
        return true;
      }
    }

    return false;
  }
}
