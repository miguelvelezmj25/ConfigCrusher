package edu.cmu.cs.mvelezce.language.ast.statement;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementIf extends Statement {
    private Expression condition;
    private StatementBlock thenBlock;

    public StatementIf(Expression condition, StatementBlock statementThen) {
        this.condition = condition;
        this.thenBlock = statementThen;
    }

    @Override
    public <T> void accept(Visitor<T> visitor) {
        visitor.visitStatementIf(this);
    }

    public Expression getCondition() { return this.condition; }

    public StatementBlock getThenBlock() { return this.thenBlock; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatementIf that = (StatementIf) o;

        if (!condition.equals(that.condition)) return false;
        return thenBlock.equals(that.thenBlock);
    }

    @Override
    public int hashCode() {
        int result = condition.hashCode();
        result = 31 * result + thenBlock.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String result = "if(" + this.condition +")";
//                "{\n";
//        String[] statements = StringUtils.split(this.thenBlock.toString(), '\n');
//
//        for(String statement : statements) {
//            result += "    " + statement + "\n";
//        }
//
//        result += "}";

        return result;
    }
}
