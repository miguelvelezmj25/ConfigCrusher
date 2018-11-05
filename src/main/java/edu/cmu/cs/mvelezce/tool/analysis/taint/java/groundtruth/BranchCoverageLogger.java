package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;
import org.apache.commons.lang3.tuple.MutablePair;

public class BranchCoverageLogger {

  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final Map<String, MutablePair<Integer, Integer>> DECISIONS_TO_BRANCH_COUNTS = new HashMap<>();

  static final String RESULTS_FILE = "results.ser";
  static final String INTERNAL_NAME = Type.getInternalName(BranchCoverageLogger.class);


  static {
    Method[] methods = BranchCoverageLogger.class.getDeclaredMethods();

    for (Method method : methods) {
      METHODS_TO_DESCRIPTORS.put(method.getName(), Type.getMethodDescriptor(method));
    }
  }

  public static void logIFEQDecision(int decisionValue, String methodName, int decisionCount) {
    String decision = methodName + "." + decisionCount;
    addDecision(decision);
    MutablePair<Integer, Integer> thenElseCounts = DECISIONS_TO_BRANCH_COUNTS.get(decision);

    if (decisionValue == 1) {
      int thenCounts = thenElseCounts.getLeft();
      thenCounts++;
      thenElseCounts.setLeft(thenCounts);
    }
    else {
      int elseCounts = thenElseCounts.getRight();
      elseCounts++;
      thenElseCounts.setRight(elseCounts);
    }
  }

  private static void addDecision(String decision) {
    if (!DECISIONS_TO_BRANCH_COUNTS.containsKey(decision)) {
      MutablePair<Integer, Integer> thenElseCounts = MutablePair.of(0, 0);
      DECISIONS_TO_BRANCH_COUNTS.put(decision, thenElseCounts);
    }
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

  static String getMethodDescriptor(String methodName) {
    String methodDescriptor = BranchCoverageLogger.METHODS_TO_DESCRIPTORS.get(methodName);

    if (methodDescriptor == null) {
      throw new RuntimeException(
          "Could not find the method " + methodName + " to add it in the instrumentation");
    }

    return methodDescriptor;
  }
}
