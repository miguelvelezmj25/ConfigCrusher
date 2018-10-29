package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.varexj;

public class UnsupportedCoverageException extends Exception {

  public final int lineNumber;

  public UnsupportedCoverageException(String message, int lineNumber) {
    super(message);
    this.lineNumber = lineNumber;
  }

  private static final long serialVersionUID = 1L;

}
