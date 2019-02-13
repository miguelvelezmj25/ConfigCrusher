package edu.cmu.cs.mvelezce.evaluation.phosphor;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SatConfigAnalyzer;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.dynamic.phosphor.BFPhosphorCompression;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit.ImplicitAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.implicit2.Implicit2Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class CompareSatConfigsTest {

  @Test
  public void Trivial_forBFvsBFPhosphor() throws IOException {
    String programName = TrivialAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName);
    Set<Set<Set<String>>> bfSatConfigs = analysis.analyze(args);

    Compression<Set<Set<Set<String>>>> compressor = new BFPhosphorCompression(programName);
    Set<Set<Set<String>>> bfPhosphorSatConfigs = compressor.compressConfigurations(args);

    Assert.assertTrue(CompareSatConfigs.equalSatConfigs(bfSatConfigs, bfPhosphorSatConfigs));
  }

  @Test
  public void Implicit_forBFvsBFPhosphor() throws IOException {
    String programName = ImplicitAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName);
    Set<Set<Set<String>>> bfSatConfigs = analysis.analyze(args);

    Compression<Set<Set<Set<String>>>> compressor = new BFPhosphorCompression(programName);
    Set<Set<Set<String>>> bfPhosphorSatConfigs = compressor.compressConfigurations(args);

    Assert.assertTrue(CompareSatConfigs.equalSatConfigs(bfSatConfigs, bfPhosphorSatConfigs));
  }

  @Test
  public void Implicit2_forBFvsBFPhosphor() throws IOException {
    String programName = Implicit2Adapter.PROGRAM_NAME;
    String[] args = new String[0];

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName);
    Set<Set<Set<String>>> bfSatConfigs = analysis.analyze(args);

    Compression<Set<Set<Set<String>>>> compressor = new BFPhosphorCompression(programName);
    Set<Set<Set<String>>> bfPhosphorSatConfigs = compressor.compressConfigurations(args);

    Assert.assertTrue(CompareSatConfigs.equalSatConfigs(bfSatConfigs, bfPhosphorSatConfigs));
  }

  @Test
  public void Subtrace_forBFvsBFPhosphor() throws IOException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SatConfigAnalyzer analysis = new SatConfigAnalyzer(programName);
    Set<Set<Set<String>>> bfSatConfigs = analysis.analyze(args);

    Compression<Set<Set<Set<String>>>> compressor = new BFPhosphorCompression(programName);
    Set<Set<Set<String>>> bfPhosphorSatConfigs = compressor.compressConfigurations(args);

    Assert.assertTrue(CompareSatConfigs.equalSatConfigs(bfSatConfigs, bfPhosphorSatConfigs));
  }
}