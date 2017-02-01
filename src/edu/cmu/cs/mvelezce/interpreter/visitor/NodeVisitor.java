package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionBinary;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionUnary;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class NodeVisitor implements Visitor {
    @Override
    public Object visitExpressionBinary(ExpressionBinary expressionBinary) {
        return null;
    }

    @Override
    public Object visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return null;
    }

    @Override
    public Object visitExpressionUnary(ExpressionUnary expressionUnary) {
        return null;
    }
}
