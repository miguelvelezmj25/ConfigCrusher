package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ExpressionConfiguration extends ExpressionVariable {

    public ExpressionConfiguration(String name) {
        super(name);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpressionConfiguration(this);
    }

}
