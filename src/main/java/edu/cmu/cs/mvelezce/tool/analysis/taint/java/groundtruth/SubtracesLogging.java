package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.ArrayDeque;
import java.util.Deque;

public class SubtracesLogging {

  private static final Deque<String> STACK = new ArrayDeque<>();

  public static void enterDecision(String labelPrefix, int decisionCount) {
    String label = labelPrefix + "." + decisionCount;
    STACK.addFirst(label);
    System.out.println("Entering " + label);
  }

  public static void exitDecision(String labelPrefix, int decisionCount) {
    String label = labelPrefix + "." + decisionCount;
    STACK.addFirst(label);
    System.out.println("Exiting " + label);
  }

  public static void exitAtReturn() {
    while (!STACK.isEmpty()) {
      System.out.println("Exiting " + STACK.removeFirst());
    }
  }

}
