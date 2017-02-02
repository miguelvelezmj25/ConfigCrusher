package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.*;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.*;

/**
 * Created by miguelvelez on 1/31/17.
 */
public interface Visitor<V> {

    public V visitExpressionBinary(ExpressionBinary expressionBinary);
    public V visitExpressionConfiguration(ExpressionConfiguration expressionConfiguration);
    public V visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt);
    public V visitExpressionUnary(ExpressionUnary expressionUnary);
    public V visitExpressionVariable(ExpressionVariable varExpr);
    // TODO
    public void visitStatementAssignment(StatementAssignment statementAssignment);
    public void visitStatementBlock(StatementBlock statementBlock);
    public void visitStatementIf(StatementIf statementIf);
    public void visitStatementSleep(StatementSleep statementSleep);
    public void visitStatementWhile(StatementWhile statementAssignment);
}
