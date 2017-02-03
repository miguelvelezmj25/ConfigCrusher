package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

import java.util.List;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementBlock extends Statement {
    private List<Statement> statements;

    // TODO List<? extends Statement> ?
    public StatementBlock(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <T> void accept(Visitor<T> visitor) {
        visitor.visitStatementBlock(this);
    }

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
