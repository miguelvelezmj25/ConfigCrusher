package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionBinary;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionUnary;

/**
 * Created by miguelvelez on 1/31/17.
 */
public interface Visitor {

    public Object visitExpressionBinary(ExpressionBinary expressionBinary);
    public Object visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt);
    public Object visitExpressionUnary(ExpressionUnary expressionUnary);

}
