package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

/**
 * Created by mvelezce on 2/6/17.
 */
public class BaseVisitor implements Visitor<Expression> {
    @Override
    public Expression visitExpressionBinary(ExpressionBinary expressionBinary) {
        return expressionBinary;
    }

    @Override
    public Expression visitExpressionConstantConfiguration(ExpressionConstantConfiguration expressionConstantConfiguration) {
        return expressionConstantConfiguration;
    }

    @Override
    public Expression visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return expressionConstantInt;
    }

    @Override
    public Expression visitExpressionUnary(ExpressionUnary expressionUnary) {
        return expressionUnary;
    }

    @Override
    public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
        return expressionVariable;
    }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        statementAssignment.accept(this);
    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {
        statementBlock.accept(this);
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        statementIf.accept(this);
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        statementSleep.accept(this);
    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {
        statementAssignment.accept(this);
    }
}
