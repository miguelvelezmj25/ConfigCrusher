package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class ExpressionUnary extends Expression {
    private String operation;
    private Expression expression;

    public ExpressionUnary(String operation, Expression expression) {
        this.operation = operation;
        this.expression = expression;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitExpressionUnary(this);
    }

    public Expression getExpression() { return this.expression; }

    public String getOperation() { return this.operation; }

    @Override
    public String toString() { return this.operation + " " + this.expression; }
}
