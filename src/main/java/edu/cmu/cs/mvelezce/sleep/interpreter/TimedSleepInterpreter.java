package edu.cmu.cs.mvelezce.sleep.interpreter;

import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.ast.value.IntValue;
import edu.cmu.cs.mvelezce.sleep.statements.TimedProgram;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.sleep.visitor.TimedVisitor;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;

/**
 * Created by mvelezce on 4/13/17.
 */
public class TimedSleepInterpreter extends SleepInterpreter implements TimedVisitor<IntValue, Void> {
    private int totalExecutionTime;

    public TimedSleepInterpreter(Statement ast) {
        super(ast);

        this.totalExecutionTime = 0;
    }

    @Override
    public Void visitSleepStatement(SleepStatement sleepStatement) {
        IntValue time = sleepStatement.getTime().accept(this);
        this.totalExecutionTime += time.getValue();

        return null;
    }

    @Override
    public Void visitTimedStatement(TimedStatement timedStatement) {
//        Region region = Regions.getRegion(timedStatement.getRegionID()); TODO
//        region.enter(this.totalExecutionTime); TODO

        timedStatement.getStatements().accept(this);

//        region.exit(this.totalExecutionTime); TODO

        return null;
    }

    @Override
    public Void visitTimedProgram(TimedProgram timedProgram) {
        // TODO make this work like a java program that is executed
//        // TODO why not make it behave like the timed statement?
//        Region program = Regions.getProgram();
//        Regions.addExecutingRegion(program);
//        program.startTime(this.totalExecutionTime);
//
//        timedProgram.getStatements().accept(this);
//
//        Regions.getProgram().endTime(this.totalExecutionTime);
//        Regions.removeExecutingRegion(program);

        return null;
    }

    public int getTotalExecutionTime() { return this.totalExecutionTime; }

}
