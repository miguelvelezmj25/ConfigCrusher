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
  private static final Map<CallingContextDecision, ThenElseCounts> CALLING_CONTEXT_DECISIONS_TO_BRANCH_COUNTS = new HashMap<>();

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
      oos.writeObject(CALLING_CONTEXT_DECISIONS_TO_BRANCH_COUNTS);
      oos.close();
      fos.close();
    }
    catch (IOException ioe) {
      throw new RuntimeException("There was an error serializing the results", ioe);
    }
  }

  private static ThenElseCounts getThenElseCounts(String methodName, int decisionCount) {
    String[] callingContext = SpecificationLogger.getCallingContext();
    String decision = methodName + "." + decisionCount;
    CallingContextDecision callingContextDecision = new CallingContextDecision(callingContext, decision);
    SpecificationLogger.addCallingContextDecision(callingContextDecision);

    return CALLING_CONTEXT_DECISIONS_TO_BRANCH_COUNTS.get(callingContextDecision);
  }

  private static String[] getCallingContext() {
    int callingContextOffset = 5;
    StackTraceElement[] callingContextElements = Thread.currentThread().getStackTrace();
    String[] callingContext = new String[callingContextElements.length - callingContextOffset];

    for (int i = callingContextOffset; i < callingContextElements.length; i++) {
      StackTraceElement callingContextElement = callingContextElements[i];
      callingContext[i - callingContextOffset] = getCallingContextEntry(callingContextElement.getMethodName(),
          callingContextElement.getLineNumber());
    }

    return callingContext;
  }

  private static String getCallingContextEntry(String methodName, int lineNumber) {
    return methodName + ":" + lineNumber;
  }

  private static void addCallingContextDecision(CallingContextDecision callingContextDecision) {
    if (!CALLING_CONTEXT_DECISIONS_TO_BRANCH_COUNTS.containsKey(callingContextDecision)) {
      ThenElseCounts thenElseCounts = new ThenElseCounts();
      CALLING_CONTEXT_DECISIONS_TO_BRANCH_COUNTS.put(callingContextDecision, thenElseCounts);
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
