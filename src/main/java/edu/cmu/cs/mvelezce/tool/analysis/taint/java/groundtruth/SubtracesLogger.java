package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;

public class SubtracesLogger {

  // TODO hash the label when not debugging?
  private static final String ENTER_DECISION = " Enter ";
  private static final String EXIT_DECISION = " Exit ";
  private static final String FALSE = "FALSE";
  private static final String TRUE = "TRUE";
  private static final Deque<SubtraceLabel> STACK = new ArrayDeque<>();
  private static final List<String> TRACE = new ArrayList<>(200);
  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final Map<SubtraceLabel, Integer> LABELS_TO_COUNT = new HashMap<>();

  static final String INTERNAL_NAME = Type.getInternalName(SubtracesLogger.class);
  static final String RESULTS_FILE = "results.ser";

  static {
    Method[] methods = SubtracesLogger.class.getDeclaredMethods();

    for (Method method : methods) {
      METHODS_TO_DESCRIPTORS.put(method.getName(), Type.getMethodDescriptor(method));
    }
  }

  static String getMethodDescriptor(String methodName) {
    String methodDescriptor = METHODS_TO_DESCRIPTORS.get(methodName);

    if (methodDescriptor == null) {
      throw new RuntimeException(
          "Could not find the method " + methodName + " to add it in the instrumentation");
    }

    return methodDescriptor;
  }

  public static void saveTrace() {
    if (!STACK.isEmpty()) {
      throw new RuntimeException(
          "The stack that tracked entering and exiting decisions is not empty\n" + STACK);
    }

    try {
      FileOutputStream fos = new FileOutputStream(RESULTS_FILE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(TRACE);
      oos.close();
      fos.close();
    }
    catch (IOException ioe) {
      throw new RuntimeException("There was an error serializing the results", ioe);
    }
  }

  public static void enterDecision(String labelPrefix, int decisionCount) {
    Deque<SubtraceLabel> ctx = getCurrentCtx();
    String labelID = getLabelID(labelPrefix, decisionCount);
    SubtraceLabel subtraceLabel = new SubtraceLabel(ENTER_DECISION, ctx, labelID);

    int labelCount = LABELS_TO_COUNT.getOrDefault(subtraceLabel, 0);
    labelCount++;
    subtraceLabel.setExecCount(labelCount);
    LABELS_TO_COUNT.put(subtraceLabel, labelCount);

    TRACE.add(subtraceLabel.toString());
    STACK.addFirst(subtraceLabel);
  }

  private static Deque<SubtraceLabel> getCurrentCtx() {
    return new ArrayDeque<>(STACK);
  }

  public static void exitDecision(String labelPrefix, int decisionCount) {
    String exitingLabel = getLabelID(labelPrefix, decisionCount);

    while (!STACK.isEmpty() && STACK.peekFirst().getDecisionLabel().equals(exitingLabel)) {
      SubtraceLabel stackLabel = STACK.removeFirst();
      exitDecision(exitingLabel, stackLabel);
    }
  }

  public static void exitAtReturn(String labelPrefix) {
    while (!STACK.isEmpty() && STACK.peekFirst().getDecisionLabel().startsWith(labelPrefix + ".")) {
      SubtraceLabel stackLabel = STACK.removeFirst();
      String exitingLabel = stackLabel.getDecisionLabel();
      exitDecision(exitingLabel, stackLabel);
    }
  }

  private static void exitDecision(String exitingLabel, SubtraceLabel stackLabel) {
    Deque<SubtraceLabel> exitingCtx = getCurrentCtx();
    SubtraceLabel expectedLabel = new SubtraceLabel(ENTER_DECISION, exitingCtx, exitingLabel);

    if (!stackLabel.equals(expectedLabel)) {
      throw new RuntimeException(
          "Expected to exit label " + expectedLabel + ", but exited label " + stackLabel
              + " instead");
    }

    int execCount = stackLabel.getExecCount();
    SubtraceLabel subtraceLabel = new SubtraceLabel(EXIT_DECISION, exitingCtx, exitingLabel,
        execCount);
    TRACE.add(subtraceLabel.toString());
  }

  private static String getLabelID(String labelPrefix, int decisionCount) {
    return labelPrefix + "." + decisionCount;
  }

  public static void logIFEQEval(int value) {
    if (value == 0) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIFNEEval(int value) {
    if (value != 0) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIFLTEval(int value) {
    if (value < 0) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIFGEEval(int value) {
    if (value >= 0) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIFGTEval(int value) {
    if (value > 0) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIFLEEval(int value) {
    if (value <= 0) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ICMPEQEval(int v1, int v2) {
    if (v1 == v2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ICMPNEEval(int v1, int v2) {
    if (v1 != v2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ICMPLTEval(int v1, int v2) {
    if (v1 < v2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ICMPGEEval(int v1, int v2) {
    if (v1 >= v2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ICMPGTEval(int v1, int v2) {
    if (v1 > v2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ICMPLEEval(int v1, int v2) {
    if (v1 <= v2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ACMPEQEval(Object o1, Object o2) {
    if (o1 == o2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIF_ACMPNEEval(Object o1, Object o2) {
    if (o1 != o2) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIFNULLEval(Object object) {
    if (object == null) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

  public static void logIFNONNULLEval(Object object) {
    if (object != null) {
      TRACE.add(FALSE);
    }
    else {
      TRACE.add(TRUE);
    }
  }

}
