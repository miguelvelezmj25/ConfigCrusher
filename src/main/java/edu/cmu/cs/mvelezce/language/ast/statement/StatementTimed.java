package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

/**
 * A statement that should be timed.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class StatementTimed extends Statement {
    private Statement statements;

    /**
     * Instantiates a {@code StatementTimed}.
     *
     * @param statements
     */
    public StatementTimed(Statement statements) {
        if(statements == null) {
            throw new IllegalArgumentException("The statements cannot be null");
        }

        this.statements = statements;
    }

    @Override
    public <T,U> U accept(Visitor<T,U> visitor) {
        return visitor.visitStatementTimed(this);
    }

    public Statement getStatements() { return this.statements; }

    @Override
    public String toString() { return "Time: " + this.statements; }
}
