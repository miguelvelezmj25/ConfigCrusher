package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.statement.BlockStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.tool.analysis.Region;

import java.util.Iterator;
import java.util.List;

/**
 * Created by mvelezce on 4/11/17.
 */
public class SleepRegion extends Region {
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
        if(statement.equals(that.statement)) return true;

        if(statement instanceof TimedStatement) {
            statement = ((TimedStatement) statement).getStatements();
        }

        if(that.statement instanceof TimedStatement) {
            that.statement = ((TimedStatement) that.statement).getStatements();
        }

        // TODO make this less awful
        if(statement instanceof BlockStatement && that.statement instanceof BlockStatement) {
//            IfStatement thisIfStatement = ((IfStatement) statement);
//            IfStatement thatIfStatement = ((IfStatement) that.statement);
//
//            if(!thisIfStatement.getCondition().equals(thatIfStatement.getCondition())) return false;
//
//            Statement thisThenBlock = thisIfStatement.getThenBlock();
//            Statement thatThenBlock = thatIfStatement.getThenBlock();
//
//            if(!(thisThenBlock instanceof BlockStatement) && !(thatThenBlock instanceof BlockStatement)) {
//                return thisThenBlock.equals(thatThenBlock);
//            }
//
//            if(thisThenBlock instanceof BlockStatement && !(thatThenBlock instanceof BlockStatement)) return false;
//
//            if(!(thisThenBlock instanceof BlockStatement) && thatThenBlock instanceof BlockStatement) return false;
//
//            List<Statement> thisThenBlockStatements = ((BlockStatement) thisThenBlock).getStatements();
//            List<Statement> thatThenBlockStatements = ((BlockStatement) thatThenBlock).getStatements();

            List<Statement> thisThenBlockStatements = ((BlockStatement) statement).getStatements();
            List<Statement> thatThenBlockStatements = ((BlockStatement) that.statement).getStatements();

            Iterator<Statement> thisThenBlockStatementsIterator = thisThenBlockStatements.iterator();
            Iterator<Statement> thatThenBlockStatementsIterator = thatThenBlockStatements.iterator();

            while(thisThenBlockStatementsIterator.hasNext() && thatThenBlockStatementsIterator.hasNext()) {
                Statement thisStatement = thisThenBlockStatementsIterator.next();
                Statement thatStatement = thatThenBlockStatementsIterator.next();

                if(thisStatement instanceof TimedStatement) {
                    thisStatement = ((TimedStatement) thisStatement).getStatements();
                }

                if(thatStatement instanceof TimedStatement) {
                    thatStatement = ((TimedStatement) thatStatement).getStatements();
                }

                if(thisStatement instanceof IfStatement && thatStatement instanceof IfStatement) {
                    SleepRegion thisRegion = new SleepRegion(((IfStatement) thisStatement).getThenBlock());
                    SleepRegion thatRegion = new SleepRegion(((IfStatement) thatStatement).getThenBlock());

                    return thisRegion.equals(thatRegion);
                }

                if(!thisStatement.equals(thatStatement)) return false;
            }

            if(!thisThenBlockStatementsIterator.hasNext() && !thatThenBlockStatementsIterator.hasNext()) return true;
        }

        return false;
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
