package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.tool.analysis.Region;

import java.util.HashSet;

/**
 * Created by mvelezce on 4/11/17.
 */
public class SleepRegion extends Region {
    // Used for location
    private Statement statement;

    private SleepRegion(String regionID, Statement statement) {
        super(regionID);
        this.statement = statement;
    }

    public SleepRegion(Statement statement) {
        this.statement = statement;
    }

    public double getSecondsExecutionTime() {
        return this.getExecutionTime();
    }

    public Region clone() throws CloneNotSupportedException {
        SleepRegion sleepRegion = new SleepRegion(this.getRegionID(), this.statement);
        sleepRegion.setStartTime(this.getStartTime());
        sleepRegion.setEndTime(this.getEndTime());
        sleepRegion.setInnerRegions(new HashSet<>(this.getInnerRegions()));

        return sleepRegion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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

    public Statement getStatement() { return this.statement; }
}
