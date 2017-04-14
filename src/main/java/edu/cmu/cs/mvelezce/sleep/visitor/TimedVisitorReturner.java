package edu.cmu.cs.mvelezce.sleep.visitor;

import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReturnerVisitor;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;

/**
 * Created by mvelezce on 4/13/17.
 */
public class TimedVisitorReturner extends ReturnerVisitor implements TimedVisitor<Expression, Void> {

    @Override
    public Void visitTimedStatement(TimedStatement timedStatement) {
        if(timedStatement == null) {
            throw new IllegalArgumentException("The timedStatement cannot be null");
        }

        timedStatement.getStatements().accept(this);
        return null;
    }

}
