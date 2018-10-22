package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorAnalysis;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ResultAnalyzer {

  private final String programName;

  ResultAnalyzer(String programName) {
    this.programName = programName;
  }

  void readPhosphorResults() throws IOException {
    DynamicAnalysis<Set<Constraint>> analysis = new PhosphorAnalysis(this.programName);

    String[] args = new String[0];
    Map<JavaRegion, Set<Constraint>> res = analysis.analyze(args);
    System.out.println(res);
  }

}
