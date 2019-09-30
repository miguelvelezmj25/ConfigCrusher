package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.thread;

import edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.ConstraintComparator;

public class ThreadConstraintComparator extends ConstraintComparator {

  ThreadConstraintComparator(String programName) {
    super(programName);
  }

  @Override
  protected String getDir() {
    return THREADS_DIR;
  }

}