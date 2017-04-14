package edu.cmu.cs.mvelezce.sleep.statements;

import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.Visitor;
import edu.cmu.cs.mvelezce.sleep.visitor.TimedVisitor;

/**
 * A statement that should be timed.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class TimedStatement extends Statement {
    private Statement statements;

    /**
     * Instantiates a {@code TimedStatement}.
     *
     * @param statements
     */
    public TimedStatement(Statement statements) {
        if(statements == null) {
            throw new IllegalArgumentException("The statements cannot be null");
        }

        this.statements = statements;
    }

    public Statement getStatements() { return this.statements; }

    @Override
    public String toString() { return "Time: " + this.statements; }

    @Override
    public <T, U> U accept(Visitor<T, U> visitor) {
        return ((TimedVisitor<T, U>) visitor).visitTimedStatement(this);
    }

}
