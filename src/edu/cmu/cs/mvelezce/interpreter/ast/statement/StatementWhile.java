package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementWhile extends Statement {
    private Expression condition;
    private Statement body;

    public StatementWhile(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    // TODO automatic increment
    @Override
    public <T> void accept(Visitor<T> visitor) {
        visitor.visitStatementWhile(this);
    }

    public Expression getCondition() { return this.condition; }

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
