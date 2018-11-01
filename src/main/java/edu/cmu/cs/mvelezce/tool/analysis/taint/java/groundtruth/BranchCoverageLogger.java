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
  private static final Map<String, Boolean> DECISIONS_TO_RESULT = new HashMap<>();

  static final String RESULTS_FILE = "results.ser";
  static final String INTERNAL_NAME = Type.getInternalName(BranchCoverageLogger.class);


  static {
    Method[] methods = BranchCoverageLogger.class.getDeclaredMethods();

    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      METHODS_TO_DESCRIPTORS.put(method.getName(), Type.getMethodDescriptor(method));
    }
  }

  public static void logIFEQDecision(int decisionValue, String methodName, int decisionCount) {
    boolean result = false;

    if (decisionValue == 1) {
      result = true;
    }

    DECISIONS_TO_RESULT.put(methodName + "." + decisionCount, result);
  }

  public static void saveExecutedDecisions() {
    try {
      FileOutputStream fos = new FileOutputStream(RESULTS_FILE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(DECISIONS_TO_RESULT);
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
