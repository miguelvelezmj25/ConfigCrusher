package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class ExpressionUnary extends Expression {
    private Expression expression;
    private String operation;

    public ExpressionUnary(Expression expression, String operation) {
        this.expression = expression;
        this.operation = operation;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitExpressionUnary(this);
    }

    public Expression getExpression() { return this.expression; }

    public String getOperation() { return this.operation; }
}
