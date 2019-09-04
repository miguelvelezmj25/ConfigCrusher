package edu.cmu.cs.mvelezce.tool.compression.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.constraint.PhosphorConstraintAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class PhosphorCompressionTest {

  @Test
  public void Trivial() throws IOException, InterruptedException {
    String programName = TrivialAdapter.PROGRAM_NAME;

    String[] args = new String[0];

    PhosphorConstraintAnalysis constraintAnalysis = new PhosphorConstraintAnalysis(programName);
    Set<ConfigConstraint> constraints = constraintAnalysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Set<String> options = new HashSet<>(TrivialAdapter.getListOfOptions());
    PhosphorCompression phosphorCompression = new PhosphorCompression(programName, options,
        constraints);
    Set<Set<String>> write = phosphorCompression.compressConfigurations(args);

    args = new String[0];

    phosphorCompression = new PhosphorCompression(programName);
    Set<Set<String>> read = phosphorCompression.compressConfigurations(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void Subtraces() throws IOException, InterruptedException {
    String programName = SubtracesAdapter.PROGRAM_NAME;

    String[] args = new String[0];

    PhosphorConstraintAnalysis constraintAnalysis = new PhosphorConstraintAnalysis(programName);
    Set<ConfigConstraint> constraints = constraintAnalysis.analyze(args);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Set<String> options = new HashSet<>(SubtracesAdapter.getListOfOptions());
    PhosphorCompression phosphorCompression = new PhosphorCompression(programName, options,
        constraints);
    Set<Set<String>> write = phosphorCompression.compressConfigurations(args);

    args = new String[0];

    phosphorCompression = new PhosphorCompression(programName);
    Set<Set<String>> read = phosphorCompression.compressConfigurations(args);

    Assert.assertEquals(write, read);
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException, InterruptedException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;

    String[] args = new String[0];

    PhosphorConstraintAnalysis constraintAnalysis = new PhosphorConstraintAnalysis(programName);
    Set<ConfigConstraint> constraints = constraintAnalysis.analyze(args);

    System.out.println(constraints.iterator().next());
    System.out.println(constraints.size());

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    Set<String> options = new HashSet<>(MeasureDiskOrderedScanAdapter.getListOfOptions());
    PhosphorCompression phosphorCompression = new PhosphorCompression(programName, options,
        constraints);
    Set<Set<String>> write = phosphorCompression.compressConfigurations(args);

    args = new String[0];

    phosphorCompression = new PhosphorCompression(programName);
    Set<Set<String>> read = phosphorCompression.compressConfigurations(args);

    Assert.assertEquals(write, read);
  }
}