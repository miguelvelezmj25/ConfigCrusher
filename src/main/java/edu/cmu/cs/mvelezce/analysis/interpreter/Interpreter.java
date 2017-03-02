package edu.cmu.cs.mvelezce.analysis.interpreter;

import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;
import edu.cmu.cs.mvelezce.language.ast.value.ValueInt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO
 *
 * Created by miguelvelez on 1/31/17.
 */
public class Interpreter implements Visitor<ValueInt, Void> {
    private Set<String> activatedConfigurations;
    private Statement ast;
    private Map<String, ValueInt> store;
    private int totalExecutionTime;
    private Map<Statement, Integer> timedBlocks;

    public Interpreter(Statement ast) {
        this.reset();
        this.ast = ast;
        this.activatedConfigurations = null;
    }

    private void reset() {
        this.store = new HashMap<>();
        this.totalExecutionTime = 0;
        this.timedBlocks = new HashMap<>();
    }

    // TODO this seems weird
    public void evaluate(Set<String> activatedConfigurations) {
        this.reset();
        this.activatedConfigurations = activatedConfigurations;

        this.ast.accept(this);
    }

    @Override
    public ValueInt visitExpressionBinary(ExpressionBinary expressionBinary) {
        ValueInt left = expressionBinary.getLeft().accept(this);
        ValueInt right =  expressionBinary.getRight().accept(this);

        if(expressionBinary.getOperation().equals("+")) {
            int result = left.getValue() + right.getValue();
            return new ValueInt(result);
        }
        else if(expressionBinary.getOperation().equals("-")) {
            int result = left.getValue() - right.getValue();
            return new ValueInt(result);
        }
        else if(expressionBinary.getOperation().equals("*")) {
            int result = left.getValue() * right.getValue();
            return new ValueInt(result);
        }
        else if(expressionBinary.getOperation().equals("/")) {
            int result = left.getValue() / right.getValue();
            return new ValueInt(result);
        }
        else if(expressionBinary.getOperation().equals("&&")) {
            if(left.getValue() == 1 && right.getValue() == 1) {
                return new ValueInt(1);
            }

            return new ValueInt(0);
        }
        else if(expressionBinary.getOperation().equals("||")) {
            if(left.getValue() == 0 && right.getValue() == 0) {
                return new ValueInt(0);
            }

            return new ValueInt(1);
        }

        // TODO do not return null
        return null;

    }

    @Override
    public ValueInt visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
        int value = 0;

        if(this.activatedConfigurations.contains(expressionConfigurationConstant.getName())) {
            value = 1;
        }

        return new ValueInt(value);
    }

    @Override
    public ValueInt visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return new ValueInt(expressionConstantInt.getValue());
    }

    @Override
    public ValueInt visitExpressionUnary(ExpressionUnary expressionUnary) {
        ValueInt result = expressionUnary.getExpression().accept(this);

        // TODO it seems like the "-" should be gotten from somewhere
        // TODO it seems weird how the int is generated
        // Negates ints
        if(expressionUnary.getOperation().equals("!")) {
            if(result.getValue() == 0) {
                return new ValueInt(1);
            }
            return new ValueInt(0);
        }

        return result;
    }

    @Override
    public ValueInt visitExpressionVariable(ExpressionVariable expressionVariable) {
        return this.store.get(expressionVariable.getName());
    }

    @Override
    public Void visitStatementAssignment(StatementAssignment statementAssignment) {
        ValueInt value = statementAssignment.getRight().accept(this);
        this.store.put(statementAssignment.getVariable().getName(), value);
        return null;
    }

    @Override
    public Void visitStatementBlock(StatementBlock statementBlock) {
        List<Statement> statements = statementBlock.getStatements();

        for(Statement statement : statements) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Void visitStatementIf(StatementIf statementIf) {
        ValueInt condition = statementIf.getCondition().accept(this);

        if(condition.getValue() > 0) {
            statementIf.getThenBlock().accept(this);
        }
        return null;
    }

    @Override
    public Void visitStatementSleep(StatementSleep statementSleep) {
        ValueInt time = statementSleep.getTime().accept(this);
        this.totalExecutionTime += time.getValue();
        return null;
    }

    @Override
    public Void visitStatementTimed(StatementTimed statement) {
        int time = totalExecutionTime;
        statement.getStatements().accept(this);
        this.timedBlocks.put(statement.getStatements(), this.totalExecutionTime - time);
        return null;
    }

    @Override
    public Void visitStatementWhile(StatementWhile statementWhile) {
        statementWhile.getCondition().accept(this);
        statementWhile.getBody().accept(this);
        return null;
    }

    public Map<String, ValueInt> getStore() { return this.store; }

    public int getTotalExecutionTime() { return this.totalExecutionTime; }

    public Map<Statement, Integer> getTimedBlocks() { return this.timedBlocks; }
}
