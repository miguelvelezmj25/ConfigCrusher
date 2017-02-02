package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

import java.util.List;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementBlock extends Statement {
    private Statement statement1;
    private Statement statement2;

    // TODO List<? extends Statement> ?
    public StatementBlock(Statement statement1, Statement statement2) {
        this.statement1 = statement1;
        this.statement2 = statement2;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO
        visitor.visitStatementBlock(this);
        return null;
    }

    @Override
    public String toString() { return this.statement1 + "; " + this.statement2; }
}
