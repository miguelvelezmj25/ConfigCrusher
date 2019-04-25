package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubtraceManager {

  private static final Map<SubtraceLabel, UUID> SUBTRACE_LABELS_TO_LABELS = new HashMap<>();
  private static final Map<UUID, SubtraceLabel> LABELS_TO_SUBTRACE_LABELS = new HashMap<>();

  private final Map<DecisionLabelWithContext, Integer> DECISION_LABELS_WITH_CONTEXT_TO_COUNTS = new HashMap<>();

  private int getExecutionCount(DecisionLabelWithContext decisionLabelWithContext) {
    return DECISION_LABELS_WITH_CONTEXT_TO_COUNTS.getOrDefault(decisionLabelWithContext, 0);
  }

  private void setExecutionCount(DecisionLabelWithContext decisionLabelWithContext,
      int execCount) {
    DECISION_LABELS_WITH_CONTEXT_TO_COUNTS.put(decisionLabelWithContext, execCount);
  }

  public UUID getLabel(String decision, Deque<UUID> stack) {
    UUID stackTop = stack.peekFirst();
    DecisionLabelWithContext decisionLabelWithContext = new DecisionLabelWithContext(stackTop,
        decision);
    int execCount = this.getExecutionCount(decisionLabelWithContext);
    execCount++;
    this.setExecutionCount(decisionLabelWithContext, execCount);

    SubtraceLabel subtraceLabel = new SubtraceLabel(stackTop, decision, execCount);

    if (SUBTRACE_LABELS_TO_LABELS.containsKey(subtraceLabel)) {
      return SUBTRACE_LABELS_TO_LABELS.get(subtraceLabel);
    }

    SUBTRACE_LABELS_TO_LABELS.put(subtraceLabel, subtraceLabel.getUUID());
    LABELS_TO_SUBTRACE_LABELS.put(subtraceLabel.getUUID(), subtraceLabel);

    return subtraceLabel.getUUID();
  }

  public static Map<UUID, SubtraceLabel> getLabelsToSubtraceLabels() {
    return LABELS_TO_SUBTRACE_LABELS;
  }
}
