package edu.cmu.cs.mvelezce.interpreter.ast.expression;

import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class ExpressionBinary extends Expression {
    private Expression left;
    private Expression right;
    private String operation;


    public ExpressionBinary(Expression left, String operation, Expression right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visitExpressionBinary(this);
    }

    public Expression getLeft() { return this.left; }

    public Expression getRight() { return this.right; }

    public String getOperation() { return this.operation; }
}
