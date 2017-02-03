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
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpressionUnary(this);
    }

    public Expression getExpression() { return this.expression; }

    public String getOperation() { return this.operation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressionUnary that = (ExpressionUnary) o;

        if (!operation.equals(that.operation)) return false;
        return expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        int result = operation.hashCode();
        result = 31 * result + expression.hashCode();
        return result;
    }

    @Override
    public String toString() { return this.operation + "" + this.expression; }
}
