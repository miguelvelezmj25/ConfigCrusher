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
    public <T> T accept(Visitor<T> visitor) {
        // TODO
        visitor.visitStatementBlock(this);
        return null;
    }

    public List<Statement> getStatements() { return this.statements; }
}
