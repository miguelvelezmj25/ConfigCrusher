package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleMain;
import java.io.IOException;
import org.junit.Test;

public class ResultAnalyzerTest {

  @Test
  public void dynamicRunningExamplePhosphorResults() throws IOException {
    String programName = DynamicRunningExampleMain.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.readCCPhosphorResults();
  }

  @Test
  public void dynamicRunningExampleBFPhosphorResults() throws IOException {
    String programName = DynamicRunningExampleMain.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.readBFPhosphorResults();
  }

  @Test
  public void dynamicRunningExample() throws IOException {
    String programName = DynamicRunningExampleMain.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.analyze();
  }
}