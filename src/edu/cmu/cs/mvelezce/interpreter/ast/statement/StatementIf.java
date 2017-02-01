package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementIf extends Statement {
    private Expression condition; // TODO this is a binaryop
    private Statement statementThen;
    private Statement statementElse;

    private StatementIf(Expression condition, Statement statementThen, Statement statementElse) {
        this.condition = condition;
        this.statementThen = statementThen;
        this.statementElse = statementElse;
    }

    @Override
    public Object accept(Visitor visitor) {
        return null; // TODO
    }

    public Expression getCondition() { return this.condition; }

    public Statement getStatementThen() { return this.statementThen; }

    public Statement getStatementElse() { return this.statementElse; }
}
