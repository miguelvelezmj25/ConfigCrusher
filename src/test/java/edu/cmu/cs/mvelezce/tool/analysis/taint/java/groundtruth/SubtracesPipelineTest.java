package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.andContext.AndContextAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit.ImplicitAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit2.Implicit2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.nesting.NestingAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample3.SimpleForExample3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.simpleForExample4.SimpleForExample4Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces2.Subtraces2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces3.Subtraces3Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces4.Subtraces4Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class SubtracesPipelineTest {

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SubtracesAdapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(3, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void Subtraces2() throws IOException, InterruptedException {
    String programName = Subtraces2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Subtraces2Adapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(3, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void SimpleForExample3() throws IOException, InterruptedException {
    String programName = SimpleForExample3Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleForExample3Adapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(2, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void SimpleForExample4() throws IOException, InterruptedException {
    String programName = SimpleForExample4Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(SimpleForExample4Adapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(3, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void Trivial() throws IOException, InterruptedException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(2, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void AndContext() throws IOException, InterruptedException {
    String programName = AndContextAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(AndContextAdapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(4, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void Implicit() throws IOException, InterruptedException {
    String programName = ImplicitAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(ImplicitAdapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(2, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void Implicit2() throws IOException, InterruptedException {
    String programName = Implicit2Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Implicit2Adapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(2, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void Nesting() throws IOException, InterruptedException {
    String programName = NestingAdapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(NestingAdapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(3, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void Subtraces3() throws IOException, InterruptedException {
    String programName = Subtraces3Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Subtraces3Adapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(2, minConfigs.size());
    System.out.println(minConfigs);
  }

  @Test
  public void Subtraces4() throws IOException, InterruptedException {
    String programName = Subtraces4Adapter.PROGRAM_NAME;
    Set<String> options = new HashSet<>(Subtraces4Adapter.getListOfOptions());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    SubtracesPipeline pipeline = new SubtracesPipeline(programName, options);
    Set<Set<String>> minConfigs = pipeline.getMinConfigsToExecute(args);

    Assert.assertEquals(3, minConfigs.size());
    System.out.println(minConfigs);
  }
}