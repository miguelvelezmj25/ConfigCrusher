package edu.cmu.cs.mvelezce.language.ast.expression;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * A variable expression.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class ExpressionVariable extends Expression {
    private String name;

    public ExpressionVariable(String name) {
        if(name == null) {
            throw new IllegalArgumentException("The name cannot be null");
        }

        if(name.isEmpty()) {
            throw new IllegalArgumentException("The name cannot be empty");
        }

        this.name = name;
    }

    /**
     * Returns the name of the variable.
     *
     * @return
     */
    public String getName() { return this.name; }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpressionVariable(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressionVariable that = (ExpressionVariable) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() { return this.name; }
}
