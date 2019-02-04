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

  // TODO keep a counter to track the number of times that a label has been executed.

  private static final String ENTER_DECISION = "Enter ";
  private static final String EXIT_DECISION = "Exit ";
  private static final String FALSE = "FALSE";
  private static final String TRUE = "TRUE";
  private static final Deque<String> STACK = new ArrayDeque<>();
  private static final List<String> TRACE = new ArrayList<>(100);
  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();

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
    STACK.addFirst(label);
    TRACE.add(label);
  }

  public static void exitDecision(String labelPrefix, int decisionCount) {
    // TODO the logic in this method might not when executing decisions multiple times or loops.
    String stackLabel = STACK.removeFirst();
    String expectedLabel = labelAction(ENTER_DECISION) + labelID(labelPrefix, decisionCount);

    if (!expectedLabel.equals(stackLabel)) {
      throw new RuntimeException(
          "Expected to exit label " + expectedLabel + ", but exited label " + stackLabel
              + " instead");
    }

    String label = labelAction(EXIT_DECISION) + labelID(labelPrefix, decisionCount);
    TRACE.add(label);
  }

  public static void exitAtReturn() {
    while (!STACK.isEmpty()) {
      String label = labelAction(EXIT_DECISION) + STACK.removeFirst();
      TRACE.add(label);
    }
  }

  private static String labelAction(String action) {
    return LABEL + action;
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
