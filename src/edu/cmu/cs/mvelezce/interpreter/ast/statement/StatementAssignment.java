package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementAssignment extends Statement {
    private ExpressionVariable variable;
    private String operation;
    private Expression right;

    public StatementAssignment(ExpressionVariable variable, String operation, Expression right) {
        this.variable = variable;
        this.operation = operation;
        this.right = right ;
    }

    public ExpressionVariable getVariable() { return this.variable; }

    public String getOperation() { return this.operation; }

    public Expression getRight() { return this.right; }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO
        visitor.visitStatementAssignment(this);
        return null;
    }

    @Override
    public String toString() { return this.variable + this.operation + this.right; }
}
