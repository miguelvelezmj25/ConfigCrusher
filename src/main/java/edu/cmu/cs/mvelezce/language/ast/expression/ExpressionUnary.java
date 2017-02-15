package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * A unary expression. It currently supports negation of expressions (!).
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class ExpressionUnary extends Expression {
    private String operation;
    private Expression expression;

    public ExpressionUnary(String operation, Expression expression) {
        if(operation == null) {
            throw new IllegalArgumentException("The operation cannot be null");
        }
        if(operation.isEmpty()) {
            throw new IllegalArgumentException("The operation cannot be empty");
        }
        if(expression == null) {
            throw new IllegalArgumentException("The expression cannot be null");
        }

        this.operation = operation;
        this.expression = expression;
    }

    /**
     * Returns the expression.
     *
     * @return
     */
    public Expression getExpression() { return this.expression; }

    /**
     * Returns the operation.
     *
     * @return
     */
    public String getOperation() { return this.operation; }

    @Override
    public <T,U> T accept(Visitor<T,U> visitor) {
        return visitor.visitExpressionUnary(this);
    }

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
