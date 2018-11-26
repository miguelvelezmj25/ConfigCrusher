package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.alldynamic.AllDynamicAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample.DynamicRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gtOverapprox.GTOverapproxAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.multifacets.MultiFacetsAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample2.PhosphorExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.phosphorExample3.PhosphorExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleexample1.SimpleExample1Adapter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class PhosphorResultAnalyzerTest {

  @Test
  public void dynamicAll() {
    Collection<File> files = FileUtils.listFiles(new File(
            "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/phosphor-examples/src/main/java/edu/cmu/cs/mvelezce/analysis"),
        new String[]{"java"}, true);

    Set<String> programsWithErrors = new HashSet<>();

    for (File file : files) {
      String programName = file.getName();
      programName = programName.replace(".java", "");
      System.out.println("####################################");
      System.out.println(programName);
      List<String> options = AllDynamicAdapter.getListOfOptions();

      try {
        PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName, options);
        analyzer.analyze();
      }
      catch (Exception e) {
        programsWithErrors.add(programName);
      }

      System.out.println("####################################");
      System.out.println();
    }

    System.out.println();
    System.out.println("####################################");
    System.out.println("Programs with errors");
    System.out.println(programsWithErrors);
  }


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
  public void example3() throws IOException, InterruptedException {
    String programName = PhosphorExample3Adapter.PROGRAM_NAME;
    List<String> options = PhosphorExample3Adapter.getListOfOptions();

    PhosphorResultAnalyzer analyzer = new PhosphorResultAnalyzer(programName, options);
    analyzer.analyze();
  }

  @Test
  public void gtOverapprox() throws IOException, InterruptedException {
    String programName = GTOverapproxAdapter.PROGRAM_NAME;
    List<String> options = GTOverapproxAdapter.getListOfOptions();

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