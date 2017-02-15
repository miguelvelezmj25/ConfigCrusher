package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;

import java.util.List;

/**
 * A block of statements as a list. Used to represent a whole program and groups of statements inside of branches.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class StatementBlock extends Statement {
    private List<Statement> statements;

    // TODO List<? extends Statement> ?

    /**
     * Instantiate a {@code StatementBlock}.
     *
     * @param statements
     */
    public StatementBlock(List<Statement> statements) {
        if(statements == null) {
            throw new IllegalArgumentException("The statements cannot be null");
        }

        this.statements = statements;
    }

    @Override
    public <T,U> U accept(Visitor<T,U> visitor) {
        return visitor.visitStatementBlock(this);
    }

    /**
     * Returns the list of statements in the block.
     *
     * @return
     */
    public List<Statement> getStatements() { return this.statements; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatementBlock that = (StatementBlock) o;

        return statements.equals(that.statements);
    }

    @Override
    public int hashCode() {
        return statements.hashCode();
    }

    @Override
    public String toString() {
        String result = "";

        for(Statement statement : this.statements) {
            result += statement + "\n";
        }

        return result.substring(0, result.length());
    }

}
