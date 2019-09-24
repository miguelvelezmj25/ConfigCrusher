package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SubtraceManager {

  private static final Map<SubtraceLabel, UUID> SUBTRACE_LABELS_TO_IDS = new HashMap<>();
  private static final Map<UUID, SubtraceLabel> IDS_TO_SUBTRACE_LABELS = new HashMap<>();

  private final Map<ControlFlowDecision, Integer> decisionsToCounts = new HashMap<>();

  public static Map<UUID, SubtraceLabel> getIdsToSubtraceLabels() {
    return IDS_TO_SUBTRACE_LABELS;
  }

  public static void saveIdsToSubtraceLabels(String programName) throws IOException {
    String outputFile =
        Options.DIRECTORY
            + "/analysis/spec/traces/subtraceManager/java/programs/"
            + programName
            + "/"
            + programName
            + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, IDS_TO_SUBTRACE_LABELS.values());
  }

  private int getDecisionExecutionCount(ControlFlowDecision decisionLabelWithContext) {
    return decisionsToCounts.getOrDefault(decisionLabelWithContext, 0);
  }

  private void setExecutionCount(ControlFlowDecision decisionLabelWithContext, int execCount) {
    decisionsToCounts.put(decisionLabelWithContext, execCount);
  }

  public UUID getLabel(String decision, Deque<UUID> stack) {
    UUID stackTop = stack.peekFirst();
    ControlFlowDecision controlFlowDecision = new ControlFlowDecision(stackTop, decision);
    int execCount = this.getDecisionExecutionCount(controlFlowDecision);
    execCount++;
    this.setExecutionCount(controlFlowDecision, execCount);

    ControlFlowStatement controlFlowStatement = new ControlFlowStatement(decision);
    SubtraceLabel subtraceLabel = new SubtraceLabel(stackTop, controlFlowStatement, execCount);

    if (SUBTRACE_LABELS_TO_IDS.containsKey(subtraceLabel)) {
      return SUBTRACE_LABELS_TO_IDS.get(subtraceLabel);
    }

    SUBTRACE_LABELS_TO_IDS.put(subtraceLabel, subtraceLabel.getUUID());
    IDS_TO_SUBTRACE_LABELS.put(subtraceLabel.getUUID(), subtraceLabel);

    return subtraceLabel.getUUID();
  }

  // Use to track how many times a control flow decision has been executed
  // A control flow decision is an execution of a control flow statement under a specific context
  private static class ControlFlowDecision extends ControlFlowStatement {

    private final UUID context;

    ControlFlowDecision(@Nullable UUID context, String decision) {
      super(decision);

      this.context = context;
    }

    public UUID getContext() {
      return context;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      if (!super.equals(o)) {
        return false;
      }

      ControlFlowDecision that = (ControlFlowDecision) o;

      return Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + (context != null ? context.hashCode() : 0);
      return result;
    }
  }
}
