package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementIf extends Statement {
    private Expression condition; // TODO this is a binaryop
    private Statement statementThen;

    public StatementIf(Expression condition, Statement statementThen) {
        this.condition = condition;
        this.statementThen = statementThen;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        // TODO
        visitor.visitStatementIf(this);
        return null;
    }

    @Override
    public String toString() {
        String result = "if(" + this.condition +") {\n";
        String[] statements = StringUtils.split(this.statementThen.toString(), '\n');

        for(String statement : statements) {
            result += "    " + statement + "\n";
        }

        result += "}";

        return result;
    }
}
