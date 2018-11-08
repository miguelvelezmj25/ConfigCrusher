package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;

public class BranchCoverageLogger {

  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final Map<String, ThenElseCounts> DECISIONS_TO_BRANCH_COUNTS = new HashMap<>();

  static final String RESULTS_FILE = "results.ser";
  static final String INTERNAL_NAME = Type.getInternalName(BranchCoverageLogger.class);


  static {
    Method[] methods = BranchCoverageLogger.class.getDeclaredMethods();

    for (Method method : methods) {
      METHODS_TO_DESCRIPTORS.put(method.getName(), Type.getMethodDescriptor(method));
    }
  }

  public static void logICMPLTDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 < value2) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logICMPEQDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 == value2) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logICMPNEDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 != value2) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logICMPGEDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 >= value2) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logIFICMPGTDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 > value2) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logIFICMPLEDecision(int value1, int value2, String methodName,
      int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (value1 <= value2) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logIFLTDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue < 0) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logIFGEDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue >= 0) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logIFNEDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue != 0) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logIFLEDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    if (decisionValue <= 0) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
    }
  }

  public static void logIFEQDecision(int decisionValue, String methodName, int decisionCount) {
    ThenElseCounts thenElseCounts = BranchCoverageLogger
        .getThenElseCounts(methodName, decisionCount);

    // If decisionValue == false
    if (decisionValue == 0) {
      BranchCoverageLogger.updatedElseBranchCount(thenElseCounts);
    }
    else {
      BranchCoverageLogger.updatedThenBranchCount(thenElseCounts);
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

  public static void saveExecutedDecisions() {
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

  private static ThenElseCounts getThenElseCounts(String methodName,
      int decisionCount) {
    String decision = methodName + "." + decisionCount;
    BranchCoverageLogger.addDecision(decision);
    return DECISIONS_TO_BRANCH_COUNTS.get(decision);
  }

  private static void addDecision(String decision) {
    if (!DECISIONS_TO_BRANCH_COUNTS.containsKey(decision)) {
      ThenElseCounts thenElseCounts = new ThenElseCounts();
      DECISIONS_TO_BRANCH_COUNTS.put(decision, thenElseCounts);
    }
  }

  static String getMethodDescriptor(String methodName) {
    String methodDescriptor = BranchCoverageLogger.METHODS_TO_DESCRIPTORS.get(methodName);

    if (methodDescriptor == null) {
      throw new RuntimeException(
          "Could not find the method " + methodName + " to add it in the instrumentation");
    }

    return methodDescriptor;
  }
}
