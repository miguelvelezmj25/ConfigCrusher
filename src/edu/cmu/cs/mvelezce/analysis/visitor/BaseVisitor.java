package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

import java.util.List;

/**
 * Created by mvelezce on 2/6/17.
 */
public class BaseVisitor implements Visitor<Expression> {
    @Override
    public Expression visitExpressionBinary(ExpressionBinary expressionBinary) {
        expressionBinary.getLeft().accept(this);
        expressionBinary.getRight().accept(this);

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
        expressionUnary.getExpression().accept(this);

        return expressionUnary;
    }

    @Override
    public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
        return expressionVariable;
    }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        statementAssignment.getVariable().accept(this);
        statementAssignment.getRight().accept(this);
    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {
        List<Statement> statements = statementBlock.getStatements();

        for(Statement statement : statements) {
            statement.accept(this);
        }
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        statementIf.getCondition().accept(this);
        statementIf.getStatementThen().accept(this);
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        statementSleep.getTime().accept(this);
    }

    @Override
    public void visitStatementWhile(StatementWhile statementWhile) {
        statementWhile.getCondition().accept(this);
        statementWhile.getBody().accept(this);
    }
}
