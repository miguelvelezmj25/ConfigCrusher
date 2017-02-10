package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ExpressionConstantConfiguration extends Expression {
    private String name;

    public ExpressionConstantConfiguration(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressionConstantConfiguration that = (ExpressionConstantConfiguration) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpressionConstantConfiguration(this);
    }

    @Override
    public String toString() { return this.name; }
}
