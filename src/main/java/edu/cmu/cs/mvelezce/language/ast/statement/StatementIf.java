package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;

/**
 * An if statement with a then branch. The then branch has a list of statements that are executed if the condition
 * evaluates to true.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class StatementIf extends Statement {
    private Expression condition;
    private StatementBlock thenBlock;

    /**
     * Instantiates a {@code StatementIf}.
     *
     * @param condition
     * @param thenBlock
     */
    public StatementIf(Expression condition, StatementBlock thenBlock) {
        if(condition == null) {
            throw new IllegalArgumentException("The condition cannot be null");
        }

        if(thenBlock == null) {
            throw new IllegalArgumentException("The thenBlock cannot be null");
        }

        this.condition = condition;
        this.thenBlock = thenBlock;
    }

    @Override
    public <T> void accept(Visitor<T> visitor) {
        visitor.visitStatementIf(this);
    }

    /**
     * Returns the condition of the statement.
     *
     * @return
     */
    public Expression getCondition() { return this.condition; }

    /**
     * Returns the list of statements of the then branch.
     * @return
     */
    public StatementBlock getThenBlock() { return this.thenBlock; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatementIf that = (StatementIf) o;

        if (!condition.equals(that.condition)) return false;
        return thenBlock.equals(that.thenBlock);
    }

    @Override
    public int hashCode() {
        int result = condition.hashCode();
        result = 31 * result + thenBlock.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "if(" + this.condition +")";
    }
}
