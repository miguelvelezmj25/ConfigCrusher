package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.subtrace;

public class ControlFlowStatement {

  private final String statement;

  ControlFlowStatement(String statement) {
    this.statement = statement;
  }

  public String getStatement() {
    return statement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ControlFlowStatement that = (ControlFlowStatement) o;

    return statement.equals(that.statement);
  }

  @Override
  public int hashCode() {
    return statement.hashCode();
  }

  @Override
  public String toString() {
    return statement;
  }
}
