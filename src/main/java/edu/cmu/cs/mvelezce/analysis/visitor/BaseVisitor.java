package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

import java.util.List;

/**
 * A concrete implementation of the Visitor Pattern that returns the same expressions that are passed.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class BaseVisitor implements Visitor<Expression> {
    @Override
    public Expression visitExpressionBinary(ExpressionBinary expressionBinary) {
        expressionBinary.getLeft().accept(this);
        expressionBinary.getRight().accept(this);

        return expressionBinary;
    }

    @Override
    public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
        return expressionConfigurationConstant;
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
        statementIf.getThenBlock().accept(this);
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
