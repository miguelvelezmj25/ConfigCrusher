package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.thread;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class ThreadStatementComparatorTest {

  private static Set<PhosphorControlFlowInfo> getMediumControlFlowInfos(
      ThreadStatementComparator comparator)
      throws IOException {
    String mediumFileName = "multi.json";

    return comparator.readFromFile(mediumFileName);
  }

  private static Set<PhosphorControlFlowInfo> getSmallControlFlowInfos(
      ThreadStatementComparator comparator)
      throws IOException {
    String mediumFileName = "single.json";

    return comparator.readFromFile(mediumFileName);
  }

  private static void compareOverlappingStatements(ThreadStatementComparator comparator,
      Set<PhosphorControlFlowInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowInfo> largeControlFlowInfos) throws FileNotFoundException {
    comparator.compareOverlapping(smallControlFlowInfos, largeControlFlowInfos);
  }

  private static void compareMissingStatements(ThreadStatementComparator comparator,
      Set<PhosphorControlFlowInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowInfo> largeControlFlowInfos) throws FileNotFoundException {
    comparator.compareMissing(smallControlFlowInfos, largeControlFlowInfos);
  }

  @Test
  public void measureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    ThreadStatementComparator comparator = new ThreadStatementComparator(programName);

    Set<PhosphorControlFlowInfo> mediumControlFlowInfos = getMediumControlFlowInfos(comparator);
    Set<PhosphorControlFlowInfo> smallControlFlowInfos = getSmallControlFlowInfos(comparator);

    compareOverlappingStatements(comparator, smallControlFlowInfos, mediumControlFlowInfos);
//    compareProcessedOverlappingStatements(smallControlFlowInfos, mediumControlFlowInfos);
    compareMissingStatements(comparator, smallControlFlowInfos, mediumControlFlowInfos);
  }
}