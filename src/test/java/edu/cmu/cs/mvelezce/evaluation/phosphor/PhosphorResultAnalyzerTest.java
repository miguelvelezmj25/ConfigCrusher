package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import java.io.IOException;
import org.junit.Test;

public class PhosphorResultAnalyzerTest {

  @Test
  public void dynamicRunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName);
    analyzer.analyze();
  }

  @Test
  public void example2() throws IOException, InterruptedException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName);
    analyzer.analyze();
  }

  @Test
  public void example3() throws IOException, InterruptedException {
    String programName = PhosphorExample3Adapter.PROGRAM_NAME;

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName);
    analyzer.analyze();
  }

  @Test
  public void dynamicRunningExamplePhosphorResults() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName);
    analyzer.readCCPhosphorResults();
  }

  @Test
  public void dynamicRunningExampleBFPhosphorResults() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName);
    analyzer.readBFPhosphorResults();
  }
}