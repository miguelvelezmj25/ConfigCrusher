package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

/**
 * Created by miguelvelez on 1/31/17.
 */
public interface Visitor <V> {

    public V visitExpressionBinary(ExpressionBinary expressionBinary);
    public V visitExpressionConstantConfiguration(ExpressionConstantConfiguration expressionConstantConfiguration);
    public V visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt);
    public V visitExpressionUnary(ExpressionUnary expressionUnary);
    public V visitExpressionVariable(ExpressionVariable expressionVariable);

    public void visitStatementAssignment(StatementAssignment statementAssignment);
    public void visitStatementBlock(StatementBlock statementBlock);
    public void visitStatementIf(StatementIf statementIf);
    public void visitStatementSleep(StatementSleep statementSleep);
    public void visitStatementWhile(StatementWhile statementWhile);
}
