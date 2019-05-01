package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class StatementComparatorTest {

  private static Set<PhosphorControlFlowInfo> getMediumControlFlowInfos(String programName)
      throws IOException {
    String mediumFileName = "medium.json";

    return StatementComparator.readFromFile(programName, mediumFileName);
  }

  private static Set<PhosphorControlFlowInfo> getSmallControlFlowInfos(String programName)
      throws IOException {
    String mediumFileName = "small.json";

    return StatementComparator.readFromFile(programName, mediumFileName);
  }

  private static void compareOverlappingStatements(Set<PhosphorControlFlowInfo> controlFlowInfos1,
      Set<PhosphorControlFlowInfo> controlFlowInfos2) {
    StatementComparator.compareOverlapping(controlFlowInfos1, controlFlowInfos2);
  }

  // TODO explore missing statements
//  private static void compareOverlappingStatements(Set<PhosphorControlFlowInfo> controlFlowInfos1,
//      Set<PhosphorControlFlowInfo> controlFlowInfos2) {
//    StatementComparator.compareOverlapping(controlFlowInfos1, controlFlowInfos2);
//  }

  @Test
  public void measureDiskOrderedScan() throws IOException {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    Set<PhosphorControlFlowInfo> mediumControlFlowInfos = getMediumControlFlowInfos(programName);
    Set<PhosphorControlFlowInfo> smallControlFlowInfos = getSmallControlFlowInfos(programName);

    compareOverlappingStatements(smallControlFlowInfos, mediumControlFlowInfos);
  }
}