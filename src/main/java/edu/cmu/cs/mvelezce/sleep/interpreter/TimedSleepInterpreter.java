package edu.cmu.cs.mvelezce.sleep.interpreter;

import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.ast.value.IntValue;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.sleep.visitor.TimedVisitor;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.pipeline.sleep.SleepRegion;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * Created by mvelezce on 4/13/17.
 */
public class TimedSleepInterpreter extends SleepInterpreter implements TimedVisitor<IntValue, Void> {
    private int totalExecutionTime;
    private Map<Statement, Integer> regionToTime;

    public TimedSleepInterpreter(Statement ast) {
        super(ast);

        this.totalExecutionTime = 0;
        this.regionToTime = new HashedMap<>();
    }

    @Override
    public Void visitSleepStatement(SleepStatement sleepStatement) {
        IntValue time = sleepStatement.getTime().accept(this);
        this.totalExecutionTime += time.getValue();

        return null;
    }

    @Override
    public Void visitTimedStatement(TimedStatement timedStatement) {
        SleepRegion hold = new SleepRegion(timedStatement.getStatements());
        Region region = Regions.getRegion(hold);
        region.startTime(this.totalExecutionTime);

        int time = totalExecutionTime;
        timedStatement.getStatements().accept(this);

        region.endTime(this.totalExecutionTime);
        this.regionToTime.put(timedStatement.getStatements(), this.totalExecutionTime - time);

        return null;
    }

    public int getTotalExecutionTime() { return this.totalExecutionTime; }

    public Map<Statement, Integer> getRegionToTime() { return this.regionToTime; }

}
