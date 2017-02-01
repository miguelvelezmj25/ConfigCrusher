package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ExpressionConfiguration extends Expression {
    private final int value;

    // TODO should be final?
    public ExpressionConfiguration(int value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return null;// TODO
    }

    public int getValue() { return this.value; }
}
