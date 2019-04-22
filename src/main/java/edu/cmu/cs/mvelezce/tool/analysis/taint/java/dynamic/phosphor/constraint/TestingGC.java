package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

public class TestingGC {


  public static void main(String[] args) throws IOException {
    String programName = "MeasureDiskOrderedScan";

    PhosphorConstraintExecutionAnalysis analysis = new PhosphorConstraintExecutionAnalysis(
        programName);

    Set<DecisionTaints> results = analysis.getResults();
    System.out.println(results.size());

    File outputFile = new File("results.ser");
    FileOutputStream fos = new FileOutputStream(outputFile);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(results);
    oos.close();
    fos.close();
  }

}
