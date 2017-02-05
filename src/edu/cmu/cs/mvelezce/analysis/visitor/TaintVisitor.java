package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintVisitor implements Visitor {
    @Override
    public Object visitExpressionBinary(ExpressionBinary expressionBinary) { return null; }

    @Override
    public Object visitExpressionConfiguration(ExpressionConfiguration expressionConfiguration) { return null; }

    @Override
    public Object visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) { return null; }

    @Override
    public Object visitExpressionUnary(ExpressionUnary expressionUnary) { return null; }

    @Override
    public Object visitExpressionVariable(ExpressionVariable varExpr) { return null; }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        // TODO
    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {
        // TODO
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        // TODO
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        // TODO
    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {
        // TODO
    }
}
