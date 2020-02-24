package edu.cmu.cs.mvelezce.analysis.region.idta;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.idta.IDTAAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.BaseCountRegionsPerMethodAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.idta.IDTATimerInstrumenter;
import edu.cmu.cs.mvelezce.instrument.region.instrumenter.BaseRegionInstrumenter;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class IDTACountRegionsPerMethodAnalysisTest {

  @Test
  public void vanillaTrivial() throws IOException, InterruptedException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    BaseCountRegionsPerMethodAnalysis<Partitioning> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToPartitions);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }

  @Test
  public void vanillaBerkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "small";
    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    BaseCountRegionsPerMethodAnalysis<Partitioning> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToPartitions);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }

  @Test
  public void expandedBerkeleyDB() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    BaseCountRegionsPerMethodAnalysis<Partitioning> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToPartitions);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }

  @Test
  public void expandedLucene() throws IOException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    BaseCountRegionsPerMethodAnalysis<Partitioning> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToPartitions);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }

  @Test
  public void expandedConvert() throws IOException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseRegionInstrumenter<Partitioning> instrumenter = new IDTATimerInstrumenter(programName);
    Map<JavaRegion, Partitioning> regionsToPartitions = instrumenter.getProcessedRegionsToData();

    BaseCountRegionsPerMethodAnalysis<Partitioning> counter =
        new IDTACountRegionsPerMethodAnalysis(regionsToPartitions);
    Map<String, Integer> methodsToRegionCounts = counter.analyze();
    counter.listMethodsWithMultipleRegions(methodsToRegionCounts);
  }
}
