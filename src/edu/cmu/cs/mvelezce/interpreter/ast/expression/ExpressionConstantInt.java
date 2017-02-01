package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class ExpressionConstantInt extends Expression {
    private final int value;

    public ExpressionConstantInt(int value) {
        this.value = value;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitExpressionConstantInt(this);
    }

    public int getValue() { return this.value; }

    @Override
    public String toString() { return String.valueOf(this.value); }
}
