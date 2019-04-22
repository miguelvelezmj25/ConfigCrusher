package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import java.io.IOException;
import java.util.Set;

public class TestingGC {

  public static void main(String[] args) throws IOException {
    PhosphorConstraintExecutionAnalysis analysis = new PhosphorConstraintExecutionAnalysis(
        "MeasureDiskOrderedScan");

    Set<DecisionTaints> x = analysis.getResults();
    System.out.println();
  }

}
