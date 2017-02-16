package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import org.apache.commons.lang3.StringUtils;

/**
 * A while statement that executes a list of statements as long as the condition evaluates to true.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class StatementWhile extends Statement {
// TODO automatic increment
    private Expression condition;
    private Statement body;

    /**
     * Instantiates a {@code StatementWhile}.
     *
     * @param condition
     * @param body
     */
    public StatementWhile(Expression condition, Statement body) {
        if(condition == null) {
            throw new IllegalArgumentException("The condition cannot be null");
        }

        if(body == null) {
            throw new IllegalArgumentException("The body cannot be null");
        }

        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T,U> U accept(Visitor<T,U> visitor) {
        return visitor.visitStatementWhile(this);
    }

    /**
     * Returns the condition of the statement.
     *
     * @return
     */
    public Expression getCondition() { return this.condition; }

    /**
     * Returns the body of the statement that is executed as long as the condition evaluates to truw.
     *
     * @return
     */
    public Statement getBody() { return this.body; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatementWhile that = (StatementWhile) o;

        if (!condition.equals(that.condition)) return false;
        return body.equals(that.body);
    }

    @Override
    public int hashCode() {
        int result = condition.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String result = "while(" + this.condition +") {\n";
        String[] statements = StringUtils.split(this.body.toString(), '\n');

        for(String statement : statements) {
            result += "    " + statement + "\n";
        }

        result += "}";

        return result;
    }
}
