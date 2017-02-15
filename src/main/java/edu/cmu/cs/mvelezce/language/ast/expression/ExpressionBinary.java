package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * A binary expression of the form {@code expr op expr}.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class ExpressionBinary extends Expression {
    private Expression left;
    private String operation;
    private Expression right;

    /**
     * Initialize an {@code ExpressionBinary}.
     *
     * @param left
     * @param operation
     * @param right
     */
    public ExpressionBinary(Expression left, String operation, Expression right) {
        if(left == null) {
            throw new IllegalAccessError("The left expression cannot be null");
        }

        if(operation == null) {
            throw new IllegalAccessError("The operation cannot be null");
        }

        if(operation.isEmpty()) {
            throw new IllegalAccessError("The operation cannot be empty");
        }

        if(right == null) {
            throw new IllegalAccessError("The right expression cannot be null");
        }

        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    @Override
    public <T,U> T accept(Visitor<T,U> visitor) {
        return visitor.visitExpressionBinary(this);
    }

    /**
     * Returns the left expression.
     *
     * @return
     */
    public Expression getLeft() { return this.left; }

    /**
     * Returns the right expression.
     *
     * @return
     */
    public Expression getRight() { return this.right; }

    /**
     * Returns the operation.
     *
     * @return
     */
    public String getOperation() { return this.operation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressionBinary that = (ExpressionBinary) o;

        if (!left.equals(that.left)) return false;
        if (!operation.equals(that.operation)) return false;
        return right.equals(that.right);
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + operation.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public String toString() { return this.left + " " + this.operation + " " + this.right; }

}
