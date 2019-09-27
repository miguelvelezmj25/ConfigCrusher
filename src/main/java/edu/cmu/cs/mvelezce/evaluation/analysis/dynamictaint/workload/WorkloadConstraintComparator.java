package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.workload;

import edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.ConstraintComparator;

public class WorkloadConstraintComparator extends ConstraintComparator {

  WorkloadConstraintComparator(String programName) {
    super(programName);
  }

  @Override
  protected String getDir() {
    return WORKLOAD_DIR;
  }

}