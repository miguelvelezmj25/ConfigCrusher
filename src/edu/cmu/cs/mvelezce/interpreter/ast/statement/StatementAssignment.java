package edu.cmu.cs.mvelezce.interpreter.ast.statement;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by mvelezce on 2/1/17.
 */
public class StatementAssignment extends Statement {
    private String left;
    private String operation;
    private Expression right;

    public StatementAssignment(String left, String operation, Expression right) {
        this.left = left;
        this.operation = operation;
        this.right = right ;
    }

    @Override
    public Object accept(Visitor visitor) {
        return null; // TODO
    }

//    public Expression getLeft() { return this.left; }

    public String getOperation() { return this.operation; }

    public Expression getRight() { return this.right; }
}
