package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace.LoggedSubtrace;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace.SubtraceLabel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;

public class SubtracesLogger {

  private static final int EXIT_AT_RETURN_FLAG_COUNT = -1;

  // TODO hash the label when not debugging?
  private static final String ENTER_DECISION = " Enter ";
  private static final String EXIT_DECISION = " Exit ";
  private static final String FALSE = "FALSE";
  private static final String TRUE = "TRUE";
  private static final List<String> TRACE = new ArrayList<>(1_000_000);
  private static final Map<String, String> METHODS_TO_DESCRIPTORS = new HashMap<>();
  private static final Map<String, Integer> LABELS_PREFIX_TO_COUNTS = new HashMap<>();

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

  // TODO synchornize
  public static void enterDecision(String labelPrefix) {
    int execCount = LABELS_PREFIX_TO_COUNTS.getOrDefault(labelPrefix, 0);
    execCount++;
    SubtraceLabel subtraceLabel = new SubtraceLabel(labelPrefix, execCount);
    LoggedSubtrace loggedSubtrace = new LoggedSubtrace(ENTER_DECISION, subtraceLabel);
    TRACE.add(loggedSubtrace.toString());

    LABELS_PREFIX_TO_COUNTS.put(labelPrefix, execCount);
  }

  // TODO synchornize
  public static void exitDecision(String labelPrefix) {
    int execCount = LABELS_PREFIX_TO_COUNTS.get(labelPrefix);
    SubtraceLabel subtraceLabel = new SubtraceLabel(labelPrefix, execCount);
    LoggedSubtrace loggedSubtrace = new LoggedSubtrace(EXIT_DECISION, subtraceLabel);
    TRACE.add(loggedSubtrace.toString());
  }

  // TODO synchornize
  public static void exitAtReturn(String labelPrefix) {
    SubtraceLabel subtraceLabel = new SubtraceLabel(labelPrefix, EXIT_AT_RETURN_FLAG_COUNT);
    LoggedSubtrace loggedSubtrace = new LoggedSubtrace(EXIT_DECISION, subtraceLabel);
    TRACE.add(loggedSubtrace.toString());
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
