package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.workload;

import edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.StatementComparator;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class WorkloadStatementComparatorTest {

  private static Set<PhosphorControlFlowInfo> getMediumControlFlowInfos(
      StatementComparator comparator)
      throws IOException {
    String mediumFileName = "medium.json";

    return comparator.readFromFile(mediumFileName);
  }

  private static Set<PhosphorControlFlowInfo> getSmallControlFlowInfos(
      StatementComparator comparator)
      throws IOException {
    String mediumFileName = "small.json";

    return comparator.readFromFile(mediumFileName);
  }

  private static void compareOverlappingStatements(StatementComparator comparator,
      Set<PhosphorControlFlowInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowInfo> largeControlFlowInfos) throws FileNotFoundException {
    comparator.compareOverlapping(smallControlFlowInfos, largeControlFlowInfos);
  }

  private static void compareMissingStatements(StatementComparator comparator,
      Set<PhosphorControlFlowInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowInfo> largeControlFlowInfos) throws FileNotFoundException {
    comparator.compareMissing(smallControlFlowInfos, largeControlFlowInfos);
  }

  @Test
  public void measureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    StatementComparator comparator = new WorkloadStatementComparator(programName);

    Set<PhosphorControlFlowInfo> mediumControlFlowInfos = getMediumControlFlowInfos(comparator);
    Set<PhosphorControlFlowInfo> smallControlFlowInfos = getSmallControlFlowInfos(comparator);

    compareOverlappingStatements(comparator, smallControlFlowInfos, mediumControlFlowInfos);
//    compareProcessedOverlappingStatements(smallControlFlowInfos, mediumControlFlowInfos);
    compareMissingStatements(comparator, smallControlFlowInfos, mediumControlFlowInfos);
  }
}