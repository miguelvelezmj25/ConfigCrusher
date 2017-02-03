package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ExpressionConstantInt extends Expression {
    private final int value;

    public ExpressionConstantInt(int value) {
        this.value = value;
    }

    public int getValue() { return this.value; }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpressionConstantInt(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressionConstantInt that = (ExpressionConstantInt) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() { return String.valueOf(this.value); }
}
