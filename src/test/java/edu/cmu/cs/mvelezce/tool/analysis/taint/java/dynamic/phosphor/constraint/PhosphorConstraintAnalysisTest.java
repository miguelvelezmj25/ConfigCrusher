package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class PhosphorConstraintAnalysisTest {

  @Test
  public void Trivial() throws IOException, InterruptedException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    List<String> options = TrivialAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    PhosphorConstraintAnalysis analysis = new PhosphorConstraintAnalysis(programName, options,
        initialConfig);
    Set<ConfigConstraint> write = analysis.analyze(args);

    args = new String[0];
    analysis = new PhosphorConstraintAnalysis(programName);
    Set<ConfigConstraint> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    List<String> options = SubtracesAdapter.getListOfOptions();
    Set<String> initialConfig = new HashSet<>();

    // Program arguments
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    PhosphorConstraintAnalysis analysis = new PhosphorConstraintAnalysis(programName, options,
        initialConfig);
    Set<ConfigConstraint> write = analysis.analyze(args);

    args = new String[0];
    analysis = new PhosphorConstraintAnalysis(programName);
    Set<ConfigConstraint> read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}