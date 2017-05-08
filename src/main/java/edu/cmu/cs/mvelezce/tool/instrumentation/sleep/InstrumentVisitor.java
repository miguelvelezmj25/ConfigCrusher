package edu.cmu.cs.mvelezce.tool.instrumentation.sleep;

// TODO why do we pass the constraints? We are only instrumenting regions. Maybe change what regions are in the region selector so that we are not selecting regions that have the same constraints as outer region

import edu.cmu.cs.mvelezce.sleep.ast.Program;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReplacerVisitor;
import edu.cmu.cs.mvelezce.sleep.statements.TimedProgram;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.region.SleepRegion;

/**
 * Created by miguelvelez on 4/25/17.
 */
public class InstrumentVisitor extends ReplacerVisitor {

//        private Map<SleepRegion, Set<ConstantConfigurationExpression>> relevantRegionsToOptions;
//        private Stack<Set<ConstantConfigurationExpression>> constraints;

//        /**
//         * Instantiate a {@code AddTimedVisitor}.
//         */
//        public AddTimedVisitor(Map<SleepRegion, Set<ConstantConfigurationExpression>> relevantRegionsToOptions) {
//            this.relevantRegionsToOptions = relevantRegionsToOptions;
//            this.constraints = new Stack<>();
//        }

    /**
     * Instantiate a {@code AddTimedVisitor}.
     */
    public InstrumentVisitor() { ; }

    @Override
    public Statement visitProgram(Program program) {
        // TODO make this work like instrumenting a java program
        Statement visitedProgram = super.visitProgram(program);

        SleepRegion region = new SleepRegion(program.getBlockStatement());
        TimedProgram timedProgram = new TimedProgram(region.getRegionID(), visitedProgram);
//        Regions.addProgram(region);

        return timedProgram;
    }

    /**
     * Replace the thenBlock of a IfStatement if the entire statement is relevant.
     *
     * @param ifStatement
     * @return
     */
    @Override
    public Statement visitIfStatement(IfStatement ifStatement) {
//            if(region != null) {
//                this.constraints.push(this.relevantRegionsToOptions.get(oldRegion));
//            }

        Statement visitedIfStatement = super.visitIfStatement(ifStatement);

        Region region = new SleepRegion(ifStatement.getThenBlock());
        region = Regions.getRegion(region);

        if(region != null) {
//                this.constraints.pop();

//                if(!this.constraints.contains(this.relevantRegionsToOptions.get(oldRegion))) {
            IfStatement hold = (IfStatement) visitedIfStatement;
            TimedStatement timedStatement = new TimedStatement(region.getRegionID(), hold.getThenBlock());
            return new IfStatement(hold.getCondition(), timedStatement);
//                }
        }

        return visitedIfStatement;
    }

    /**
     * Replace the thenBlock of a IfStatement if the entire statement is relevant.
     *
     * @param sleepStatement
     * @return
     */
    @Override
    public Statement visitSleepStatement(SleepStatement sleepStatement) {
//            if(region != null) {
//                this.constraints.push(this.relevantRegionsToOptions.get(oldRegion));
//            }

        Statement visitedSleepStatement = super.visitSleepStatement(sleepStatement);

        Region region = new SleepRegion(visitedSleepStatement);
        region = Regions.getRegion(region);

        if(region != null) {
//                this.constraints.pop();

//                if(!this.constraints.contains(this.relevantRegionsToOptions.get(oldRegion))) {
            return new TimedStatement(region.getRegionID(), visitedSleepStatement);
//                }
        }

        return visitedSleepStatement;
    }
}
