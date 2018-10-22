package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import java.io.IOException;
import org.junit.Test;

public class ResultAnalyzerTest {

  @Test
  public void dynamicRunningExample() throws IOException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.analyze();
  }

  @Test
  public void example2() throws IOException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.analyze();
  }

  @Test
  public void dynamicRunningExamplePhosphorResults() throws IOException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.readCCPhosphorResults();
  }

  @Test
  public void dynamicRunningExampleBFPhosphorResults() throws IOException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.readBFPhosphorResults();
  }
}