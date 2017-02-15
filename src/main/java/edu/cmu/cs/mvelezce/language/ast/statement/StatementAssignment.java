package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;

/**
 * An assignment statement of the form {@code exprVariable op expr}.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class StatementAssignment extends Statement {
    private ExpressionVariable variable;
    private String operation;
    private Expression right;

    public StatementAssignment(ExpressionVariable variable, String operation, Expression right) {
        if(variable == null) {
            throw new IllegalArgumentException("The variable cannot be null");
        }

        if(operation == null) {
            throw new IllegalArgumentException("The operation cannot be null");
        }

        if(!operation.equals("=")) {
            throw new IllegalArgumentException("The operation must be '='");
        }

        if(right == null) {
            throw new IllegalArgumentException("The right cannot be null");
        }

        this.variable = variable;
        this.operation = operation;
        this.right = right;
    }

    @Override
    public <T> void accept(Visitor<T> visitor) {
        visitor.visitStatementAssignment(this);
    }

    /**
     * Returns the variable of the assignment.
     *
     * @return
     */
    public ExpressionVariable getVariable() { return this.variable; }

    /**
     * Returns the operation of the assignment.
     *
     * @return
     */
    public String getOperation() { return this.operation; }

    /**
     * Returns the expression of the assignment.
     *
     * @return
     */
    public Expression getRight() { return this.right; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatementAssignment that = (StatementAssignment) o;

        if (!variable.equals(that.variable)) return false;
        if (!operation.equals(that.operation)) return false;
        return right.equals(that.right);
    }

    @Override
    public int hashCode() {
        int result = variable.hashCode();
        result = 31 * result + operation.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public String toString() { return this.variable + this.operation + this.right; }

}
