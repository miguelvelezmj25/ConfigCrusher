package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class BFPhosphorAnalysisTest {

  @Test
  public void dynamicRunningExample() throws IOException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(DynamicRunningExampleAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Set<Constraint>> analysis = new BFPhosphorAnalysis(programName, options);
    analysis.analyze(args);
  }

  @Test
  public void example2() throws IOException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(PhosphorExample2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Set<Constraint>> analysis = new BFPhosphorAnalysis(programName, options);
    analysis.analyze(args);
  }

  @Test
  public void example3() throws IOException {
    String programName = PhosphorExample3Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(PhosphorExample3Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Set<Constraint>> analysis = new BFPhosphorAnalysis(programName, options);
    analysis.analyze(args);
  }
}