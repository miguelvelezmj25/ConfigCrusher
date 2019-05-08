package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.workload;

import edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.StatementComparator;

public class WorkloadStatementComparator extends StatementComparator {

  WorkloadStatementComparator(String programName) {
    super(programName);
  }

  @Override
  protected String getDir() {
    return WORKLOAD_DIR;
  }

}