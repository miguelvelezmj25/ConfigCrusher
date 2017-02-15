package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;

/**
 * A sleep statement.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class StatementSleep extends Statement {
    private Expression time;

    /**
     * Instantiates a {@code StatementSleep}.
     *
     * @param time
     */
    public StatementSleep(Expression time) {
        this.time = time;
    }

    @Override
    public <T,U> U accept(Visitor<T,U> visitor) {
        return visitor.visitStatementSleep(this);
    }

    /**
     * Returns the time to sleep.
     *
     * @return
     */
    public Expression getTime() { return this.time; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatementSleep that = (StatementSleep) o;

        return time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }

    @Override
    public String toString() { return "sleep(" + this.time + ")";
    }
}
