package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.thread;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowStatementInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class ThreadStatementComparatorTest {

  private static Set<PhosphorControlFlowStatementInfo> getMediumControlFlowInfos(
      ThreadStatementComparator comparator) throws IOException {
    String mediumFileName = "multi.json";

    return comparator.readFromFile(mediumFileName);
  }

  private static Set<PhosphorControlFlowStatementInfo> getSmallControlFlowInfos(
      ThreadStatementComparator comparator) throws IOException {
    String mediumFileName = "single.json";

    return comparator.readFromFile(mediumFileName);
  }

  private static void compareOverlappingStatements(
      ThreadStatementComparator comparator,
      Set<PhosphorControlFlowStatementInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowStatementInfo> largeControlFlowInfos)
      throws FileNotFoundException {
    comparator.compareOverlapping(smallControlFlowInfos, largeControlFlowInfos);
  }

  private static void compareMissingStatements(
      ThreadStatementComparator comparator,
      Set<PhosphorControlFlowStatementInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowStatementInfo> largeControlFlowInfos)
      throws FileNotFoundException {
    comparator.compareMissing(smallControlFlowInfos, largeControlFlowInfos);
  }

  @Test
  public void measureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    ThreadStatementComparator comparator = new ThreadStatementComparator(programName);

    Set<PhosphorControlFlowStatementInfo> mediumControlFlowInfos =
        getMediumControlFlowInfos(comparator);
    Set<PhosphorControlFlowStatementInfo> smallControlFlowInfos =
        getSmallControlFlowInfos(comparator);

    compareOverlappingStatements(comparator, smallControlFlowInfos, mediumControlFlowInfos);
    //    compareProcessedOverlappingStatements(smallControlFlowInfos, mediumControlFlowInfos);
    compareMissingStatements(comparator, smallControlFlowInfos, mediumControlFlowInfos);
  }
}
