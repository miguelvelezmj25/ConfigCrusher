package edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.idta.IDTAAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IDTASuboptimalGreedyConjunctionsCompressionTest {

  @Test
  public void trivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    List<String> options = BaseTrivialAdapter.getListOfOptions();

    String workloadSize = "small";
    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Collection<Partitioning> allPartitions = analysis.analyze().values();

    BaseCompression compression =
        new IDTASuboptimalGreedyConjunctionsCompression(programName, options, allPartitions);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);

    System.out.println(configs.size() + " to sample");
  }

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();

    String workloadSize = "small";
    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Collection<Partitioning> allPartitions = analysis.analyze().values();

    BaseCompression compression =
        new IDTASuboptimalGreedyConjunctionsCompression(programName, options, allPartitions);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);

    System.out.println(configs.size() + " to sample");
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();

    String workloadSize = "small";
    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Collection<Partitioning> allPartitions = analysis.analyze().values();

    BaseCompression compression =
        new IDTASuboptimalGreedyConjunctionsCompression(programName, options, allPartitions);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);

    System.out.println(configs.size() + " to sample");
  }

  @Test
  public void performance() throws IOException, InterruptedException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    List<String> options = BasePerformanceAdapter.getListOfOptions();

    String workloadSize = "small";
    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Collection<Partitioning> allPartitions = analysis.analyze().values();

    BaseCompression compression =
        new IDTASuboptimalGreedyConjunctionsCompression(programName, options, allPartitions);
    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);

    System.out.println(configs.size() + " to sample");
  }
}
