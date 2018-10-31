package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class BranchCoverageLogger {

  public static final String RESULTS_FILE = "results.ser";

  private static final Set<String> EXECUTED_DECISIONS = new HashSet<>();

  public static void main(String[] args) {
    BranchCoverageLogger.saveExecutedDecisions();
  }

  public static void logExecutedDecision(String methodName, int decisionCount) {
    EXECUTED_DECISIONS.add(methodName + decisionCount);
  }

  public static void saveExecutedDecisions() {
    try {
      FileOutputStream fos = new FileOutputStream(RESULTS_FILE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(EXECUTED_DECISIONS);
      oos.close();
      fos.close();
    }
    catch (IOException ioe) {
      throw new RuntimeException("There was an error serializing the results", ioe);
    }
  }
}
