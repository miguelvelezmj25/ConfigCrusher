package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

public class SubtracesLogging {

  public static void main(String[] args) {
    SubtracesLogging.enterDecision("label", 10);
    SubtracesLogging.exitDecision("label", 10);
    SubtracesLogging.exitAtReturn();
  }

  public static void enterDecision(String labelPrefix, int decisionCount) {
    System.out.println("Entering " + labelPrefix + "." + decisionCount);
  }

  public static void exitDecision(String labelPrefix, int decisionCount) {
    System.out.println("Exiting " + labelPrefix + "." + decisionCount);
  }

  public static void exitAtReturn() {
    System.out.println("Exiting everything on the stack");
  }

}
