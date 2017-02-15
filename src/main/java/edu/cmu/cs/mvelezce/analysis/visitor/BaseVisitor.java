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
        if(expressionBinary == null) {
            throw new IllegalArgumentException("The expressionBinary cannot be null");
        }

        expressionBinary.getLeft().accept(this);
        expressionBinary.getRight().accept(this);

        return expressionBinary;
    }

    @Override
    public Expression visitExpressionConstantConfiguration(
            ExpressionConfigurationConstant expressionConfigurationConstant) {
        if(expressionConfigurationConstant == null) {
            throw new IllegalArgumentException("The expressionConfigurationConstant cannot be null");
        }

        return expressionConfigurationConstant;
    }

    @Override
    public Expression visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        if(expressionConstantInt == null) {
            throw new IllegalArgumentException("The expressionConstantInt cannot be null");
        }

        return expressionConstantInt;
    }

    @Override
    public Expression visitExpressionUnary(ExpressionUnary expressionUnary) {
        if(expressionUnary == null) {
            throw new IllegalArgumentException("The expressionUnary cannot be null");
        }

        expressionUnary.getExpression().accept(this);

        return expressionUnary;
    }

    @Override
    public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
        if(expressionVariable == null) {
            throw new IllegalArgumentException("The expressionVariable cannot be null");
        }

        return expressionVariable;
    }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        if(statementAssignment == null) {
            throw new IllegalArgumentException("The statementAssignment cannot be null");
        }

        statementAssignment.getVariable().accept(this);
        statementAssignment.getRight().accept(this);
    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {
        if(statementBlock == null) {
            throw new IllegalArgumentException("The statementBlock cannot be null");
        }

        List<Statement> statements = statementBlock.getStatements();

        for(Statement statement : statements) {
            statement.accept(this);
        }
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        if(statementIf == null) {
            throw new IllegalArgumentException("The statementIf cannot be null");
        }

        statementIf.getCondition().accept(this);
        statementIf.getThenBlock().accept(this);
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        if(statementSleep == null) {
            throw new IllegalArgumentException("The statementSleep cannot be null");
        }

        statementSleep.getTime().accept(this);
    }

    @Override
    public void visitStatementWhile(StatementWhile statementWhile) {
        if(statementWhile == null) {
            throw new IllegalArgumentException("The statementWhile cannot be null");
        }

        statementWhile.getCondition().accept(this);
        statementWhile.getBody().accept(this);
    }
}
