package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementAssignment extends Statement {
    private ExpressionVariable variable;
    private String operation;
    private Expression right;

    public StatementAssignment(ExpressionVariable variable, String operation, Expression right) {
        this.variable = variable;
        this.operation = operation;
        this.right = right ;
    }

    public ExpressionVariable getVariable() { return this.variable; }

    public String getOperation() { return this.operation; }

    public Expression getRight() { return this.right; }

    @Override
    public <T> void accept(Visitor<T> visitor) {
        visitor.visitStatementAssignment(this);
    }

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
