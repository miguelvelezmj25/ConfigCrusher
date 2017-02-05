package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class BasicBlock {

    private String id;
    private Statement statement;
    private Expression expression;

    public BasicBlock(String id, Statement statement, Expression expression) {
        this.id = id;
        this.statement = statement;
        this.expression = expression;

        if(this.statement != null && this.expression != null) {
            throw new IllegalArgumentException("You cannot have a basic block with a statement and expression");
        }
    }

    public BasicBlock(String id, Statement statement) {
        this(id, statement, null);
    }

    public BasicBlock(String id, Expression expression) {
        this(id, null, expression);
    }

    public boolean isSpecial() { return this.statement == null && this.expression == null; }

    public boolean isStatement() { return this.statement != null && this.expression == null; }

    public boolean isExpression() { return this.statement == null && this.expression != null; }

    public String getId() { return this.id; }

    public Statement getStatement() { return this.statement; }

    public Expression getExpression() { return this.expression; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBlock that = (BasicBlock) o;

        if (!id.equals(that.id)) return false;

        boolean crap;

        if (statement != null) {
            crap = !statement.equals(that.statement);
        }
        else {
            crap = that.statement != null;
        }

        if(crap) {
            return false;
        }

        return expression != null ? expression.equals(that.expression) : that.expression == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if(this.statement != null) {
            return this.statement.toString();
        }

        if(this.expression != null) {
            return this.expression.toString();
        }

        return this.id;
    }
}
