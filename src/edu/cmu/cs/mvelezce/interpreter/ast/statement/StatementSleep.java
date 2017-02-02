package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementSleep extends Statement {
    private Expression time;

    public StatementSleep(Expression time) {
        this.time = time;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO
        visitor.visitStatementSleep(this);
        return null;
    }

    public Expression getTime() { return this.time; }

    @Override
    public String toString() { return "sleep(" + this.time + ")";
    }
}
