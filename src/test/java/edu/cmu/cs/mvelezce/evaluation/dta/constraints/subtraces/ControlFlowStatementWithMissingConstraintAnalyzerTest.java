package edu.cmu.cs.mvelezce.evaluation.dta.constraints.subtraces;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan.MeasureDiskOrderedScanAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class ControlFlowStatementWithMissingConstraintAnalyzerTest {

  private Set<SubtraceOutcomeConstraint> getSubtracesOutcomeConstraint(String programName)
      throws IOException {
    SubtracesConstraintsAnalyzer subtracesConstraintsAnalyzer =
        new SubtracesConstraintsAnalyzer(programName);

    return subtracesConstraintsAnalyzer.analyze(new String[0]);
  }

  @Test
  public void berkeleyDB_1() throws Exception {
    String programName = MeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String missingConstraint = "(!DUPLICATES && SEQUENTIAL)";
    Set<SubtraceOutcomeConstraint> subtracesOutcomeConstraint =
        getSubtracesOutcomeConstraint(programName);
    ControlFlowStatementWithMissingConstraintAnalyzer analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(
            programName, missingConstraint, subtracesOutcomeConstraint);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    ControlFlowStatementsWithMissingConstraint write = analysis.analyze(args);

    analysis =
        new ControlFlowStatementWithMissingConstraintAnalyzer(programName, missingConstraint);

    args = new String[0];
    ControlFlowStatementsWithMissingConstraint read = analysis.analyze(args);

    Assert.assertEquals(write, read);
  }
}
