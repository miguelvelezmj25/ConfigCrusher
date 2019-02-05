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
  // TODO keep a counter to track the number of times that a label has been executed.
  private static final String ENTER_DECISION = "Enter ";
  private static final String EXIT_DECISION = "Exit ";
  private static final String FALSE = "FALSE";
  private static final String TRUE = "TRUE";
  private static final Deque<String> STACK = new ArrayDeque<>();
  private static final List<String> TRACE = new ArrayList<>(200);
  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final Map<String, Integer> LABELS_TO_COUNT = new HashMap<>();

  static final String LABEL = "LABEL ";

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
    String label = labelAction(ENTER_DECISION) + labelID(labelPrefix, decisionCount);

    int labelCount = LABELS_TO_COUNT.getOrDefault(label, 0);
    labelCount++;
    LABELS_TO_COUNT.put(label, labelCount);

    label += "-" + labelCount;

    TRACE.add(label);
    STACK.addFirst(label);
  }

  public static void exitDecision(String labelPrefix, int decisionCount) {
    // TODO the logic in this method might not when executing decisions multiple times or loops.
    String stackLabel = STACK.removeFirst();
    String expectedLabel = labelAction(ENTER_DECISION) + labelID(labelPrefix, decisionCount);

    if (!stackLabel.startsWith(expectedLabel)) {
      throw new RuntimeException(
          "Expected to exit label " + expectedLabel + ", but exited label " + stackLabel
              + " instead");
    }

    String label =
        labelAction(EXIT_DECISION) + labelID(labelPrefix, decisionCount) + getExecCount(stackLabel);
    TRACE.add(label);
  }

  private static String getExecCount(String stackLabel) {
    int lastIndexOfCountDelimiter = stackLabel.lastIndexOf("-");

    return stackLabel.substring(lastIndexOfCountDelimiter);
  }

  public static void exitAtReturn() {
    while (!STACK.isEmpty()) {
      String label = labelAction(EXIT_DECISION) + STACK.removeFirst();
      TRACE.add(label);
    }
  }

  private static String labelAction(String action) {
    StringBuilder stringBuilder = new StringBuilder();

    for (String label : STACK) {
      stringBuilder.append(label);
      stringBuilder.append(" ");
    }

    stringBuilder.append(LABEL);
    stringBuilder.append(action);

    return stringBuilder.toString();
  }

  private static String labelID(String labelPrefix, int decisionCount) {
    return labelPrefix + "." + decisionCount;
  }

  public static void logIFEQEval(int value) {
    if (value == 0) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIFNEEval(int value) {
    if (value != 0) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIFLTEval(int value) {
    if (value < 0) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIFGEEval(int value) {
    if (value >= 0) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIFGTEval(int value) {
    if (value > 0) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIFLEEval(int value) {
    if (value <= 0) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ICMPEQEval(int v1, int v2) {
    if (v1 == v2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ICMPNEEval(int v1, int v2) {
    if (v1 != v2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ICMPLTEval(int v1, int v2) {
    if (v1 < v2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ICMPGEEval(int v1, int v2) {
    if (v1 >= v2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ICMPGTEval(int v1, int v2) {
    if (v1 > v2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ICMPLEEval(int v1, int v2) {
    if (v1 <= v2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ACMPEQEval(Object o1, Object o2) {
    if (o1 == o2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIF_ACMPNEEval(Object o1, Object o2) {
    if (o1 != o2) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIFNULLEval(Object object) {
    if (object == null) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

  public static void logIFNONNULLEval(Object object) {
    if (object != null) {
      TRACE.add(TRUE);
    }
    else {
      TRACE.add(FALSE);
    }
  }

}
