package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ExpressionVariable extends Expression {
    private String name;

    public ExpressionVariable(String name) {
        this.name = name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVarExpr(this);
    }

    public String getName() { return this.name; }
}
