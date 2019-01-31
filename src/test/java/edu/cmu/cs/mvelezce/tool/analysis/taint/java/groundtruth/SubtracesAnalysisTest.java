package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class SubtracesAnalysisTest {

  @Test
  public void Example1() throws IOException, InterruptedException {
    String programName = Example1Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Example1Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis analysis = new SubtracesAnalysis(programName, options);
    analysis.analyze(args);
  }
}