package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class BasicBlock {

    private String id;
    private Statement statement;

    public BasicBlock(String id, Statement statement) {
        this.id = id;
        this.statement = statement;

    }

    public BasicBlock(String id) {
        this(id, null);
    }

    public boolean isSpecial() { return this.statement == null; }

    public boolean isStatement() { return this.statement != null; }

    public String getId() { return this.id; }

    public Statement getStatement() { return this.statement; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBlock that = (BasicBlock) o;

        if (!id.equals(that.id)) return false;
        return statement != null ? statement.equals(that.statement) : that.statement == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if(this.statement != null) {
            return this.statement.toString();
        }

        return this.id;
    }
}
