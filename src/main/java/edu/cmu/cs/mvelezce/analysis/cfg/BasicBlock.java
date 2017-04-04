package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * A BasicBlock in a CFG. It contains an id, a statement, and a list of expressions which are the conditions
 * for this statement to be executed.
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
        if(id == null) {
            throw new IllegalArgumentException("The id cannot be null");
        }

        if(id.isEmpty()) {
            throw new IllegalArgumentException("The id cannot be an empty string");
        }

        if(conditions == null) {
            throw new IllegalArgumentException("The conditions cannot be null");
        }

        this.id = id;
        this.statement = statement;
        this.conditions = conditions;
    }

    /**
     * Initializes a {@code Basic Block} without conditions.
     * @param statement
     * @param conditions
     */
    public BasicBlock(Statement statement, List<Expression> conditions) {
        this(statement.toString(), statement, conditions);
    }

    /**
     * Initializes a {@code Basic Block} without conditions.
     * @param statement
     */
    public BasicBlock(Statement statement) {
        this(statement, new ArrayList<>());
    }

    /**
     * Initializes a {@code Basic Block} without a statement and conditions.
     * @param id
     */
    public BasicBlock(String id) {
        this(id, null, new ArrayList<>());
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
    public String toString() {
        if(this.statement != null) {
            return this.statement.toString();
        }

        return this.id;
    }
}
