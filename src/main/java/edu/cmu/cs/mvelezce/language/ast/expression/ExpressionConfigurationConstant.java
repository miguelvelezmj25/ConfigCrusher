package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * A constant configuration expression. If this object is instantiated, we assume that it's value is true.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class ExpressionConfigurationConstant extends Expression {
    private String name;

    /**
     * Initialize an {@code ExpressionConfigurationConstant}.
     *
     * @param name
     */
    public ExpressionConfigurationConstant(String name) {
        if(name == null) {
            throw new IllegalArgumentException("The name cannot be null");
        }

        if(name.isEmpty()) {
            throw new IllegalArgumentException("The name cannot be empty");
        }

        this.name = name;
    }

    /**
     * Returns the name of this configuration.
     * 
     * @return
     */
    public String getName() { return this.name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressionConfigurationConstant that = (ExpressionConfigurationConstant) o;

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
