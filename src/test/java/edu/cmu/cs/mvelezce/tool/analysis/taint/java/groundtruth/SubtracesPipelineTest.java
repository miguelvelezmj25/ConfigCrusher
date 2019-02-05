package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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

    System.out.println(minConfigs);
  }
}