package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class PhosphorResultAnalyzerTest {

  @Test
  public void dynamicRunningExample() throws IOException, InterruptedException {
    String programName = DynamicRunningExampleAdapter.PROGRAM_NAME;
    List<String> options = DynamicRunningExampleAdapter.getListOfOptions();

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName, options);
    analyzer.analyze();
  }

  @Test
  public void simpleExample1() throws IOException, InterruptedException {
    String programName = SimpleExample1Adapter.PROGRAM_NAME;
    List<String> options = SimpleExample1Adapter.getListOfOptions();

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName, options);
    analyzer.analyze();
  }

  @Test
  public void example1() throws IOException, InterruptedException {
    String programName = Example1Adapter.PROGRAM_NAME;
    List<String> options = Example1Adapter.getListOfOptions();

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName, options);
    analyzer.analyze();
  }

  @Test
  public void example2() throws IOException, InterruptedException {
    String programName = PhosphorExample2Adapter.PROGRAM_NAME;
    List<String> options = PhosphorExample2Adapter.getListOfOptions();

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName, options);
    analyzer.analyze();
  }

  @Test
  public void multiFacets() throws IOException, InterruptedException {
    String programName = MultiFacetsAdapter.PROGRAM_NAME;
    List<String> options = MultiFacetsAdapter.getListOfOptions();

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName, options);
    analyzer.analyze();
  }

  }