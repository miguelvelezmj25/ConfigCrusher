package edu.cmu.cs.mvelezce.analysis.visitor;

import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO and also check usage of LinkedList
 */
public class VisitorReplacer implements Visitor<Expression, Statement> {

    public VisitorReplacer() { ; }

    @Override
    public Expression visitExpressionBinary(ExpressionBinary expressionBinary) {
        if(expressionBinary == null) {
            throw new IllegalArgumentException("The expressionBinary cannot be null");
        }

        Expression left = expressionBinary.getLeft().accept(this);
        Expression right = expressionBinary.getRight().accept(this);

        if(expressionBinary.getLeft().equals(left) && expressionBinary.getRight().equals(right)) {
            return expressionBinary;
        }

        return new ExpressionBinary(left, expressionBinary.getOperation(), right);
    }

    @Override
    public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
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

        Expression expression = expressionUnary.getExpression().accept(this);

        if(expressionUnary.getExpression().equals(expression)) {
            return expressionUnary;
        }

        return new ExpressionUnary(expressionUnary.getOperation(), expression);
    }

    @Override
    public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
        if(expressionVariable == null) {
            throw new IllegalArgumentException("The expressionVariable cannot be null");
        }

        return expressionVariable;
    }

    @Override
    public Statement visitStatementAssignment(StatementAssignment statementAssignment) {
        if(statementAssignment == null) {
            throw new IllegalArgumentException("The statementAssignment cannot be null");
        }

        ExpressionVariable variable = (ExpressionVariable) statementAssignment.getVariable().accept(this);
        Expression right = statementAssignment.getRight().accept(this);

        if(statementAssignment.getVariable().equals(variable) && statementAssignment.getRight().equals(right)) {
            return statementAssignment;
        }

        return new StatementAssignment(variable, statementAssignment.getOperation(), right);
    }

    @Override
    public Statement visitStatementBlock(StatementBlock statementBlock) {
        if(statementBlock == null) {
            throw new IllegalArgumentException("The  cannot be null");
        }

        List<Statement> statements = statementBlock.getStatements();
        List<Statement> newStatements = new ArrayList<>();

        boolean changed = false;
        for(Statement statement : statements) {
            Statement newStatement = statement.accept(this);
            newStatements.add(newStatement);
            if(!statement.equals(newStatement)) {
                changed = true;
            }
        }

        if(!changed) {
            return statementBlock;
        }
        
        return new StatementBlock(newStatements);
    }

    @Override
    public Statement visitStatementIf(StatementIf statementIf) {
        if(statementIf == null) {
            throw new IllegalArgumentException("The statementIf cannot be null");
        }

        Expression condition = statementIf.getCondition().accept(this);
        Statement thenBlock = statementIf.getThenBlock().accept(this);

        if(statementIf.getCondition().equals(condition) && statementIf.getThenBlock().equals(thenBlock)) {
            return statementIf;
        }

        return new StatementIf(condition, thenBlock);
    }

    @Override
    public Statement visitStatementSleep(StatementSleep statementSleep) {
        if(statementSleep == null) {
            throw new IllegalArgumentException("The statementSleep cannot be null");
        }

        Expression time = statementSleep.getTime().accept(this);

        if(statementSleep.getTime().equals(time)) {
            return statementSleep;
        }

        return new StatementSleep(time);
    }

    @Override
    public Statement visitStatementWhile(StatementWhile statementWhile) {
        if(statementWhile == null) {
            throw new IllegalArgumentException("The statementWhile cannot be null");
        }

        Expression condition = statementWhile.getCondition().accept(this);
        Statement body = statementWhile.getBody().accept(this);
        
        if(statementWhile.getCondition().equals(condition) && statementWhile.getBody().equals(body)) {
            return statementWhile;
        }
        
        return new StatementWhile(condition, body);
    }

    @Override
    public Statement visitStatementTimed(StatementTimed statementTimed) {
        if(statementTimed == null) {
            throw new IllegalArgumentException("The statementTimed cannot be null");
        }

        Statement statements = statementTimed.getStatements();

        if(statementTimed.getStatements().equals(statements)) {
            return statementTimed;
        }

        return new StatementTimed(statements);
    }
}
