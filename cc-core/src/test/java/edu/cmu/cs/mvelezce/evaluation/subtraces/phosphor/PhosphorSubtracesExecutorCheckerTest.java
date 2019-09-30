package edu.cmu.cs.mvelezce.evaluation.subtraces.phosphor;

import edu.cmu.cs.mvelezce.evaluation.subtraces.SubtracesExecutorChecker;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SatConfigAnalyzer;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtraceAnalysisInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.SubtracesValueAnalysis;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.dynamic.phosphor.PhosphorCompression;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.subtraces.SubtracesAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class PhosphorSubtracesExecutorCheckerTest {

  @Test
  public void Subtraces() throws IOException {
    String programName = SubtracesAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);

    Compression<Set<Set<String>>> compression = new PhosphorCompression(programName);
    Set<Set<String>> minConfigs = compression.compressConfigurations(args);

    SubtracesExecutorChecker explorer =
        new PhosphorSubtracesExecutorChecker(subtraceAnalysisInfos, minConfigs);
    explorer.analyze();
  }

  @Test
  public void MeasureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String[] args = new String[0];

    SubtracesValueAnalysis subtracesValueAnalysis = new SubtracesValueAnalysis(programName);
    Set<SubtraceAnalysisInfo> subtraceAnalysisInfos = subtracesValueAnalysis.analyze(args);

    SatConfigAnalyzer configAnalyzer = new SatConfigAnalyzer(programName);
    Set<Set<String>> minConfigs = configAnalyzer.analyze(args);

    SubtracesExecutorChecker explorer =
        new PhosphorSubtracesExecutorChecker(subtraceAnalysisInfos, minConfigs);
    explorer.analyze();
  }
}
