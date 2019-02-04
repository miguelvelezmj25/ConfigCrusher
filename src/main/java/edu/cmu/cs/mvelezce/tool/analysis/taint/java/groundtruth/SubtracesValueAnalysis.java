package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Analyses what value each subtrace has.
 */
public class SubtracesValueAnalysis {

  private final String programName;
  private final Map<Set<String>, List<String>> configsToTraces;
  private final List<String> alignedTrace;

  public SubtracesValueAnalysis(String programName, Map<Set<String>, List<String>> configsToTraces,
      List<String> alignedTrace) {
    this.programName = programName;
    this.configsToTraces = configsToTraces;
    this.alignedTrace = alignedTrace;
  }

  // TODO this method returns what we will save and what should be read
  public Set<ConfigLabelValue> analyze() {
    Set<ConfigLabelValue> configLabelValues = new HashSet<>();

    for (Map.Entry<Set<String>, List<String>> entry : this.configsToTraces.entrySet()) {
      Set<String> config = entry.getKey();
      List<String> trace = entry.getValue();

      Map<String, String> labelsToValues = this.getLabelsToValues(trace);

      ConfigLabelValue configLabelValue = new ConfigLabelValue(config, labelsToValues);
      configLabelValues.add(configLabelValue);
    }

    return configLabelValues;
  }

  private Map<String, String> getLabelsToValues(List<String> trace) {
    Map<String, String> labelsToValues = new HashMap<>();

    for (String label : this.alignedTrace) {
      labelsToValues.put(label, "");
    }

    Map<String, Integer> traceElementsToIndexes = this.getTraceElementsToIndexes(trace);

    for (String label : this.alignedTrace) {
      int index = traceElementsToIndexes.getOrDefault(label, -1);

      if (index < 0 || index == (trace.size() - 1)) {
        continue;
      }

      String element = trace.get(index + 1);

      if (!element.startsWith(SubtracesLogger.LABEL)) {
        labelsToValues.put(label, element);
      }

    }

    return labelsToValues;

  }

  private Map<String, Integer> getTraceElementsToIndexes(List<String> trace) {
    Map<String, Integer> traceElementsToIndexes = new HashMap<>();

    for (int i = 0; i < trace.size(); i++) {
      traceElementsToIndexes.put(trace.get(i), i);
    }

    return traceElementsToIndexes;
  }

  private static class ConfigLabelValue {

    private final Set<String> config;
    private final Map<String, String> labelsToValues;

    public ConfigLabelValue(Set<String> config, Map<String, String> labelsToValues) {
      this.config = config;
      this.labelsToValues = labelsToValues;
    }
  }

}
