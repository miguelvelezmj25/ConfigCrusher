package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementSleep extends Statement {
    private int time;

    public StatementSleep(int time) {
        this.time = time;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO
        visitor.visitStatementSleep(this);
        return null;
    }

    public int getTime() { return this.time; }

    @Override
    public String toString() { return "Sleep " + this.time;
    }
}
