package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;

public class SpecificationLogger {

  private static final Map<String, String> ID_TO_SUBTRACE = new HashMap<>();
  private static final Deque<String> IdStack = new ArrayDeque<>();

  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final Map<String, ThenElseCounts> DECISIONS_TO_BRANCH_COUNTS = new HashMap<>();

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
//    ThenElseCounts thenElseCounts = SpecificationLogger.getThenElseCounts(methodName, decisionCount);

    System.out.println(IdStack);
    String id = methodName + ":" + decisionCount;
//    id = id.hashCode() + "";

    if (decisionValue != 0) {
      System.out.println(id + " -> False branch");
//      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      System.out.println(id + " -> True branch");
//      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

    IdStack.push(id);
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
//    ThenElseCounts thenElseCounts = SpecificationLogger
//        .getThenElseCounts(methodName, decisionCount);

    System.out.println(IdStack);
    String id = methodName + ":" + decisionCount;
//    id = id.hashCode() + "";

    if (decisionValue == 0) {
      System.out.println(id + " -> False branch");
//      SpecificationLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      System.out.println(id + " -> True branch");
//      SpecificationLogger.updatedThenBranchCount(thenElseCounts);
    }

    IdStack.push(id);
  }

  public static void saveExecutedDecisions() {
    if (!SpecificationLogger.IdStack.isEmpty()) {
      System.out.println(SpecificationLogger.IdStack);
      throw new RuntimeException("The IdStack is not empty");
    }

    try {
      FileOutputStream fos = new FileOutputStream(RESULTS_FILE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(DECISIONS_TO_BRANCH_COUNTS);
      oos.close();
      fos.close();
    }
    catch (IOException ioe) {
      throw new RuntimeException("There was an error serializing the results", ioe);
    }
  }

  public static void popFromIdStack() {
    SpecificationLogger.IdStack.removeFirst();
  }

  private static ThenElseCounts getThenElseCounts(String methodName, int decisionCount) {
//    String[] callingCtx = SpecificationLogger.getCallingCtx();
    String decision = methodName + "." + decisionCount;
    SpecificationLogger.addDecision(decision);
//    CallingCtxDecision callingCtxDecision = new CallingCtxDecision(callingCtx, decision);
//    SpecificationLogger.addCallingCtxDecision(callingCtxDecision);

    return DECISIONS_TO_BRANCH_COUNTS.get(decision);
  }

  private static String[] getCallingCtx() {
    int callingCtxOffset = 5;
    StackTraceElement[] callingCtxElements = Thread.currentThread().getStackTrace();
    String[] callingCtx = new String[callingCtxElements.length - callingCtxOffset];

    for (int i = callingCtxOffset; i < callingCtxElements.length; i++) {
      StackTraceElement callingCtxElement = callingCtxElements[i];
      callingCtx[i - callingCtxOffset] = getCallingCtxEntry(callingCtxElement.getMethodName(),
          callingCtxElement.getLineNumber());
    }

    return callingCtx;
  }

  private static String getCallingCtxEntry(String methodName, int lineNumber) {
    return methodName + ":" + lineNumber;
  }

  private static void addDecision(String decision) {
    if (!DECISIONS_TO_BRANCH_COUNTS.containsKey(decision)) {
      ThenElseCounts thenElseCounts = new ThenElseCounts();
      DECISIONS_TO_BRANCH_COUNTS.put(decision, thenElseCounts);
    }
  }

//  private static void addCallingCtxDecision(CallingCtxDecision callingCtxDecision) {
//    if (!DECISIONS_TO_BRANCH_COUNTS.containsKey(callingCtxDecision)) {
//      ThenElseCounts thenElseCounts = new ThenElseCounts();
//      DECISIONS_TO_BRANCH_COUNTS.put(callingCtxDecision, thenElseCounts);
//    }
//  }

  static String getMethodDescriptor(String methodName) {
    String methodDescriptor = SpecificationLogger.METHODS_TO_DESCRIPTORS.get(methodName);

    if (methodDescriptor == null) {
      throw new RuntimeException(
          "Could not find the method " + methodName + " to add it in the instrumentation");
    }

    return methodDescriptor;
  }
}
