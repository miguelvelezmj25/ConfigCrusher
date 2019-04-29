package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubtraceManager {

//  private static final Set<String> PROBLEMATIC_DECISIONS = new HashSet<>();
  private static final Map<SubtraceLabel, UUID> SUBTRACE_LABELS_TO_LABELS = new HashMap<>();
  private static final Map<UUID, SubtraceLabel> LABELS_TO_SUBTRACE_LABELS = new HashMap<>();

  private final Map<DecisionLabelWithContext, Integer> DECISION_LABELS_WITH_CONTEXT_TO_COUNTS = new HashMap<>();

//  static {
//    PROBLEMATIC_DECISIONS.add("com/sleepycat/je/utilint/JVMSystemUtils.<clinit>()V");
//    PROBLEMATIC_DECISIONS
//        .add("com/sleepycat/je/log/FSyncManager.<init>(Lcom/sleepycat/je/dbi/EnvironmentImpl;)V");
//    PROBLEMATIC_DECISIONS
//        .add("com/sleepycat/je/log/FileManager$3.accept(Ljava/io/File;Ljava/lang/String;)Z");
//  }
//
//  public static boolean isProblematicDecision(String decision, String topDecision) {
////    boolean isProblematicDecision = isProblematicDecision(decision);
////
////    if (!isProblematicDecision) {
////      return false;
////    }
//
//    return isExitingEarlierDecisionInSameMethod(decision, topDecision);
//  }

//  private static boolean isExitingEarlierDecisionInSameMethod(String decision,
//      String topDecision) {
//    String[] decisionEntries = decision.split("\\.");
//    String[] topSubtraceDecisionEntries = topDecision.split("\\.");
//
//    if (decisionEntries.length != 3 | topSubtraceDecisionEntries.length != 3) {
//      throw new RuntimeException("Expected to split the decision into 3 elements");
//    }
//
//    return decisionEntries[0].equals(topSubtraceDecisionEntries[0]) && decisionEntries[1]
//        .equals(topSubtraceDecisionEntries[1]) && Integer.valueOf(decisionEntries[2]) > Integer
//        .valueOf(topSubtraceDecisionEntries[2]);
//  }
//
//  private static boolean isProblematicDecision(String decision) {
//    for (String problematicDecision : PROBLEMATIC_DECISIONS) {
//      if (decision.startsWith(problematicDecision)) {
//        return true;
//      }
//    }
//
//    return false;
//  }

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
