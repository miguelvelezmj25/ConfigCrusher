package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ExpressionConstantConfiguration extends ExpressionConstantInt {
    private String name;

    public ExpressionConstantConfiguration(String name, int value) {
        super(value);
        this.name = name;
    }

    public ExpressionConstantConfiguration(String name) {
        this(name, 0);
    }

    public String getName() { return this.name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExpressionConstantConfiguration that = (ExpressionConstantConfiguration) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpressionConstantConfiguration(this);
    }

}
