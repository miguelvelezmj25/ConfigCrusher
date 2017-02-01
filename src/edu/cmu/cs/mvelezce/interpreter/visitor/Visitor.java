package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionBinary;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionUnary;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionVariable;

/**
 * Created by miguelvelez on 1/31/17.
 */
public interface Visitor<V> {

    public V visitExpressionBinary(ExpressionBinary expressionBinary);
    public V visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt);
    public V visitExpressionUnary(ExpressionUnary expressionUnary);
    public V visitVarExpr(ExpressionVariable varExpr);
}
