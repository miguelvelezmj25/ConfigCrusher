package edu.cmu.cs.mvelezce.interpreter.cfg;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class BasicBlock {

    private String id;
    private List<BasicBlock> predecessors;
    private List<BasicBlock> successors;
    private Statement statement;
    private Expression expression;

    public BasicBlock(String id, Statement statement, Expression expression) {
        this.id = id;
        this.predecessors = new ArrayList<>();
        this.successors = new ArrayList<>();
        this.statement = statement;
        this.expression = expression;

        if(this.statement != null && this.expression != null) {
            throw new IllegalArgumentException("You cannot have a basic block with a statement and expression");
        }
    }

    public String getId() { return this.id; }

    public List<BasicBlock> getPredecessors() { return this.predecessors; }

    public List<BasicBlock> getSuccessors() { return this.successors; }

    public Statement getStatement() { return this.statement; }

    public Expression getExpression() { return this.expression; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBlock that = (BasicBlock) o;

        if (!id.equals(that.id)) return false;
        if (predecessors != null ? !predecessors.equals(that.predecessors) : that.predecessors != null) return false;
        if (successors != null ? !successors.equals(that.successors) : that.successors != null) return false;
        if (statement != null ? !statement.equals(that.statement) : that.statement != null) return false;
        return expression != null ? expression.equals(that.expression) : that.expression == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (predecessors != null ? predecessors.hashCode() : 0);
        result = 31 * result + (successors != null ? successors.hashCode() : 0);
        result = 31 * result + (statement != null ? statement.hashCode() : 0);
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        return result;
    }
}
