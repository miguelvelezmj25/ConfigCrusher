package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;

/**
 * Created by mvelezce on 4/11/17.
 */
public class SleepRegion extends Region implements Cloneable {
    private Statement statement;

    public SleepRegion(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() { return this.statement; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SleepRegion that = (SleepRegion) o;

        return statement.equals(that.statement);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + statement.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SleepRegion{" +
                "statement=" + statement +
                '}';
    }

}
