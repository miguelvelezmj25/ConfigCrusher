package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * A BasicBlock in a CFG. It contains an id, a statement, and a list of expressions which are the conditions
 * for this statement to be executied.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class BasicBlock {

    private String id;
    private Statement statement;
    private List<Expression> conditions;

    /**
     * Initializes a {@code BasicBlock}.
     * @param id
     * @param statement
     * @param conditions
     */
    public BasicBlock(String id, Statement statement, List<Expression> conditions) {
        this.id = id;
        this.statement = statement;
        this.conditions = conditions;
    }

    /**
     * Initializes a {@code Basic Block} without conditions.
     * @param id
     * @param statement
     */
    public BasicBlock(String id, Statement statement) {
        this(id, statement, new ArrayList<>());
    }

    /**
     * Initializes a {@code Basic Block} without a statement and conditions.
     * @param id
     */
    public BasicBlock(String id) {
        this(id, null);
    }

    /**
     * Returns true if the BasicBlock does not contain a statement. This means that it is either the entry or exit
     * BasicBlock fo the CFG.
     * @return
     */
    public boolean isSpecial() { return this.statement == null; }

    /**
     * Returns true if the BasicBlock does contain a statement.
     * @return
     */
    public boolean isStatement() { return this.statement != null; }

    /**
     * Returns the statement of this BasicBlock.
     * @return
     */
    public Statement getStatement() { return this.statement; }

    /**
     * Returns a list of the expressions that this BasicBlock depends on.
     * @return
     */
    public List<Expression> getConditions() { return this.conditions; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBlock that = (BasicBlock) o;

        if (!id.equals(that.id)) return false;
        if (statement != null ? !statement.equals(that.statement) : that.statement != null) return false;
        return conditions.equals(that.conditions);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        result = 31 * result + conditions.hashCode();
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
