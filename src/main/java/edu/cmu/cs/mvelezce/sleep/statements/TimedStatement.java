package edu.cmu.cs.mvelezce.sleep.statements;

import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.Visitor;
import edu.cmu.cs.mvelezce.sleep.visitor.TimedVisitor;

import java.util.UUID;

/**
 * A statement that should be timed.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class TimedStatement extends Statement {
    private UUID regionID;
    private Statement statements;

    /**
     * Instantiates a {@code TimedStatement}.
     *
     * @param statements
     */
    public TimedStatement(UUID regionID, Statement statements) {
        if(regionID == null) {
            throw new IllegalArgumentException("The regionID cannot be null");
        }

        if(statements == null) {
            throw new IllegalArgumentException("The statements cannot be null");
        }

        this.regionID = regionID;
        this.statements = statements;
    }

    public UUID getRegionID() { return this.regionID; }

    public Statement getStatements() { return this.statements; }

    @Override
    public String toString() { return "Time: " + this.statements; }

    @Override
    public <T, U> U accept(Visitor<T, U> visitor) {
        return ((TimedVisitor<T, U>) visitor).visitTimedStatement(this);
    }

}
