package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class TracesAlignerTest {

  @Test
  public void alignStrings() {
    String s1 = "abbacc";
    String s2 = "aacddc";
    String s3 = "aacc";

    Set<String> strings = new HashSet<>();
    strings.add(s1);
    strings.add(s2);
    strings.add(s3);

    String alignedString = TracesAligner.alignStrings(strings);

    String expectedAlignedString = "abbacddc";
    Assert.assertEquals(expectedAlignedString, alignedString);
  }

  @Test
  public void alignSubtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    DynamicAnalysis<Map<Set<String>, List<String>>> analysis = new SubtracesAnalysis(programName);
    Map<Set<String>, List<String>> configsToTraces = analysis.analyze(args);

    TracesAligner tracesAligner = new TracesAligner(programName, configsToTraces);
    List<String> alignedTrace = tracesAligner.align();
  }

}