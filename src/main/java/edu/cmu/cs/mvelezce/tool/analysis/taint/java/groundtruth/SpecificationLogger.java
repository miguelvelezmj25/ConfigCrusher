package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;

public class SpecificationLogger {

  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final Map<StackTraceDecision, ThenElseCounts> STACK_TRACE_DECISIONS_TO_BRANCH_COUNTS = new HashMap<>();

  static final String RESULTS_FILE = "results.ser";
  static final String INTERNAL_NAME = Type.getInternalName(SpecificationLogger.class);

  static {
    Method[] methods = SpecificationLogger.class.getDeclaredMethods();

    for (Method method : methods) {
      METHODS_TO_DESCRIPTORS.put(method.getName(), Type.getMethodDescriptor(method));
    }
  }

  private static void updatedThenBranchCount(ThenElseCounts thenElseCounts) {
    int thenCounts = thenElseCounts.getThenCount();
    thenCounts++;
    thenElseCounts.setThenCount(thenCounts);
  }

  private static void updatedElseBranchCount(ThenElseCounts thenElseCounts) {
    int elseCounts = thenElseCounts.getElseCount();
    elseCounts++;
    thenElseCounts.setElseCount(elseCounts);
  }

  public static void logICMPLTDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 < value2) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logICMPEQDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 == value2) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logICMPNEDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 != value2) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logICMPGEDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 >= value2) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logIFICMPGTDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 > value2) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logIFICMPLEDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 <= value2) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logIFLTDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue < 0) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logIFGEDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue >= 0) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logIFNEDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue != 0) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logIFLEDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue <= 0) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void logIFEQDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = SpecificationLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue == 0) {
      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

  }

  public static void saveExecutedDecisions() {
    try {
      FileOutputStream fos = new FileOutputStream(RESULTS_FILE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(STACK_TRACE_DECISIONS_TO_BRANCH_COUNTS);
      oos.close();
      fos.close();
    }
    catch (IOException ioe) {
      throw new RuntimeException("There was an error serializing the results", ioe);
    }
  }

  private static ThenElseCounts getThenElseCounts(String methodName, int decisionCount) {
    String[] stackTrace = SpecificationLogger.getStackTrace();
    String decision = methodName + "." + decisionCount;
    StackTraceDecision stackTraceDecision = new StackTraceDecision(stackTrace, decision);
    SpecificationLogger.addStackTraceDecision(stackTraceDecision);

    return STACK_TRACE_DECISIONS_TO_BRANCH_COUNTS.get(stackTraceDecision);
  }

  private static String[] getStackTrace() {
    int stackTraceOffset = 5;
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
    String[] stackTrace = new String[stackTraceElements.length - stackTraceOffset];

    // 4 is the offset in the stack trace
    for (int i = stackTraceOffset; i < stackTraceElements.length; i++) {
      StackTraceElement stackTraceElement = stackTraceElements[i];
      stackTrace[i - stackTraceOffset] = getStackTraceEntry(stackTraceElement.getMethodName(),
          stackTraceElement.getLineNumber());
    }

    return stackTrace;
  }

  private static String getStackTraceEntry(String methodName, int lineNumber) {
    return methodName + ":" + lineNumber;
  }

  private static void addStackTraceDecision(StackTraceDecision stackTraceDecision) {
    if (!STACK_TRACE_DECISIONS_TO_BRANCH_COUNTS.containsKey(stackTraceDecision)) {
      ThenElseCounts thenElseCounts = new ThenElseCounts();
      STACK_TRACE_DECISIONS_TO_BRANCH_COUNTS.put(stackTraceDecision, thenElseCounts);
    }
  }

  static String getMethodDescriptor(String methodName) {
    String methodDescriptor = SpecificationLogger.METHODS_TO_DESCRIPTORS.get(methodName);

    if (methodDescriptor == null) {
      throw new RuntimeException(
          "Could not find the method " + methodName + " to add it in the instrumentation");
    }

    return methodDescriptor;
  }
}
