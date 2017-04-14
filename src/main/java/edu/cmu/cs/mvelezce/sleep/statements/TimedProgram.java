package edu.cmu.cs.mvelezce.sleep.statements;

import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.Visitor;
import edu.cmu.cs.mvelezce.sleep.visitor.TimedVisitor;

/**
 * Created by mvelezce on 4/14/17.
 */
public class TimedProgram extends TimedStatement {
    /**
     * Instantiates a {@code TimedStatement}.
     *
     * @param statements
     */
    public TimedProgram(Statement statements) {
        super(statements);
    }

    @Override
    public String toString() {
        return "TimedProgram: " + this.getStatements();
    }

    @Override
    public <T, U> U accept(Visitor<T, U> visitor) {
        return ((TimedVisitor<T, U>) visitor).visitTimedProgram(this);
    }
}
