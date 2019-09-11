package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.indexFiles.IndexFilesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler.PrevaylerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.return2Example.Return2ExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.returnExample.ReturnExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.returnExample2.ReturnExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample.SimpleForExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample2.SimpleForExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample3.SimpleForExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubtracesAnalysisExecutorTest {

  @Test
  public void Example1() throws IOException, InterruptedException {
    String programName = Example1Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Example1Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis analysis = new SubtracesAnalysisExecutor(programName, options);
    analysis.analyze(args);
  }

  @Test
  public void SimpleForExample() throws IOException, InterruptedException {
    String programName = SimpleForExampleAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleForExampleAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void SimpleForExample2() throws IOException, InterruptedException {
    String programName = SimpleForExample2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleForExample2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis analysis = new SubtracesAnalysisExecutor(programName, options);
    analysis.analyze(args);
  }

  @Test
  public void ReturnExample() throws IOException, InterruptedException {
    String programName = ReturnExampleAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(ReturnExampleAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis analysis = new SubtracesAnalysisExecutor(programName, options);
    analysis.analyze(args);
  }

  @Test
  public void ReturnExample2() throws IOException, InterruptedException {
    String programName = ReturnExample2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(ReturnExample2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis analysis = new SubtracesAnalysisExecutor(programName, options);
    analysis.analyze(args);
  }

  @Test
  public void Return2Example() throws IOException, InterruptedException {
    String programName = Return2ExampleAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Return2ExampleAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SubtracesAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces2() throws IOException, InterruptedException {
    String programName = Subtraces2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Subtraces2Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void SimpleForExample3() throws IOException, InterruptedException {
    String programName = SimpleForExample3Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleForExample3Adapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Prevayler() throws IOException, InterruptedException {
    String programName = PrevaylerAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(PrevaylerAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());
    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    analysis = new SubtracesAnalysisExecutor(programName);
    args = new String[0];
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(MeasureDiskOrderedScanAdapter.getListOfOptions());
    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    analysis = new SubtracesAnalysisExecutor(programName);
    args = new String[0];
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void indexFiles() throws IOException, InterruptedException {
    String programName = IndexFilesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(IndexFilesAdapter.getListOfOptions());
    DynamicAnalysis<Map<Set<String>, List<String>>> analysis =
        new SubtracesAnalysisExecutor(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    analysis = new SubtracesAnalysisExecutor(programName);
    args = new String[0];
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
