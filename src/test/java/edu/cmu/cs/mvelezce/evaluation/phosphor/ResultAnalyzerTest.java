package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleMain;
import java.io.IOException;
import org.junit.Test;

public class ResultAnalyzerTest {

  @Test
  public void readPhosphorResults() throws IOException {
    String programName = DynamicRunningExampleMain.PROGRAM_NAME;

    ResultAnalyzer analyzer = new ResultAnalyzer(programName);
    analyzer.readPhosphorResults();
  }
}