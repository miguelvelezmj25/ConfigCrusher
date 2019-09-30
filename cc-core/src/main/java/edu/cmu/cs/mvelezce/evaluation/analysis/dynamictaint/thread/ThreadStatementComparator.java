package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.thread;

import edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint.StatementComparator;

public class ThreadStatementComparator extends StatementComparator {

  ThreadStatementComparator(String programName) {
    super(programName);
  }

  @Override
  protected String getDir() {
    return THREADS_DIR;
  }

}