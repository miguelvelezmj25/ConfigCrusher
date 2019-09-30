package edu.cmu.cs.mvelezce.tool.analysis.region;

import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;

/**
 * Created by mvelezce on 4/11/17.
 */
public class SleepRegion extends Region {

  // Used for location
  private Statement statement;

  private SleepRegion(String regionID, Statement statement) {
    super(new Region.Builder(regionID));
    this.statement = statement;
  }

  public SleepRegion(Statement statement) {
    super(new Region.Builder());
    this.statement = statement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SleepRegion that = (SleepRegion) o;

    return statement.equals(that.statement);
  }

  @Override
  public int hashCode() {
    return statement.hashCode();
  }

  @Override
  public String toString() {
    return "SleepRegion{" +
        "statement=" + statement +
        '}';
  }

  public Statement getStatement() {
    return this.statement;
  }
}
