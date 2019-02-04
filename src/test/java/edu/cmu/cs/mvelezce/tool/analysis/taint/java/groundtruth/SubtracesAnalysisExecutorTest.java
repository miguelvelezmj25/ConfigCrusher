package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.example1.Example1Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.return2Example.Return2ExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample.SimpleForExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample2.SimpleForExample2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample3.SimpleForExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

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

    DynamicAnalysis analysis = new SubtracesAnalysisExecutor(programName, options);
    analysis.analyze(args);
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
  public void SimpleForExample3() throws IOException, InterruptedException {
    String programName = SimpleForExample3Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleForExample3Adapter.getListOfOptions());

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

    DynamicAnalysis analysis = new SubtracesAnalysisExecutor(programName, options);
    analysis.analyze(args);
  }

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SubtracesAdapter.getListOfOptions());

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis = new SubtracesAnalysisExecutor(programName,
        options);
    Map<Set<String>, List<String>> write = analysis.analyze(args);

    args = new String[0];

    analysis = new SubtracesAnalysisExecutor(programName);
    Map<Set<String>, List<String>> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}