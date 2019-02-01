package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;

public class SubtracesLogging {

  // TODO keep a counter to track the number of times that a label has been executed.
  // TODO log the decision of the control-flow decision.
  private static final String ENTER_DECISION = "Enter ";
  private static final String EXIT_DECISION = "Exit ";

  private static final Deque<String> STACK = new ArrayDeque<>();
  private static final List<String> TRACE = new ArrayList<>(100);
  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();

  static final String INTERNAL_NAME = Type.getInternalName(SubtracesLogging.class);

  static {
    Method[] methods = SubtracesLogging.class.getDeclaredMethods();

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

    for (String element : TRACE) {
      System.out.println(element);
    }
  }

  public static void enterDecision(String labelPrefix, int decisionCount) {
    String label = ENTER_DECISION + labelPrefix + "." + decisionCount;
    STACK.addFirst(label);
    TRACE.add(label);
  }

  public static void exitDecision(String labelPrefix, int decisionCount) {
    // TODO the logic in this method might not when executing decisions multiple times or loops.
    String stackLabel = STACK.removeFirst();
    String expectedLabel = ENTER_DECISION + labelPrefix + "." + decisionCount;

    if (!expectedLabel.equals(stackLabel)) {
      throw new RuntimeException(
          "Expected to exit label " + expectedLabel + ", but exited label " + stackLabel
              + " instead");
    }

    String label = EXIT_DECISION + labelPrefix + "." + decisionCount;
    TRACE.add(label);
  }

  public static void exitAtReturn() {
    while (!STACK.isEmpty()) {
      String label = EXIT_DECISION + STACK.removeFirst();
      TRACE.add(label);
    }
  }

}
