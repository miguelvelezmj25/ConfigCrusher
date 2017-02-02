package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

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
    public <T> T accept(Visitor<T> visitor) {
        // TODO
        visitor.visitStatementWhile(this);
        return null;
    }

    @Override
    public String toString() { return "while(" + this.condition + ")\n    "
            + this.body;
    }
}
