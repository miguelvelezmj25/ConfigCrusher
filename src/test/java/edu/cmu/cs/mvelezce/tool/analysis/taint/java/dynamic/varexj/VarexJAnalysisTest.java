package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.BFPhosphorAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class VarexJAnalysisTest {

  @Test
  public void analyze() throws IOException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;

    // Program arguments
    String[] args = new String[0];

    DynamicAnalysis<Set<Constraint>> bfPhosphorAnalysis = new BFPhosphorAnalysis(programName);
    Map<JavaRegion, Set<Constraint>> output = bfPhosphorAnalysis.analyze(args);
    Set<JavaRegion> sinks = output.keySet();

    DynamicAnalysis<Set<Constraint>> analysis = new VarexJAnalysis(programName, sinks);
    Map<JavaRegion, Set<Constraint>> res = analysis.analyze(args);

  }

}