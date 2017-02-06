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
 * Configurations cannot be changed.
 *
 * Created by miguelvelez on 1/31/17.
 */
public class Interpreter implements Visitor<ValueInt> {
    private Statement ast;
    private Set<String> activatedConfigurations;
    // TODO to CK: can I change configurations?
    private final Map<String, ValueInt> store;
    private int sleepTime;
// private StringBuffer output; TODO could be done

    public Interpreter(Statement ast, Set<String> activatedConfigurations) {
        this.ast = ast;
        this.activatedConfigurations = activatedConfigurations;
        this.store = new HashMap<>();
        this.sleepTime = 0;
    }

    // TODO this seems weird
    public Map<String, ValueInt> evaluate() {
        ast.accept(this);
        System.out.println("Sleep " + this.sleepTime);
        return this.store;
    }

    @Override
    public ValueInt visitExpressionBinary(ExpressionBinary expressionBinary) {
        ValueInt left = expressionBinary.getLeft().accept(this);
        ValueInt right =  expressionBinary.getRight().accept(this);

        if(expressionBinary.getOperation().equals("+")) {
            int result = left.getValue() + (right).getValue();
            return new ValueInt(result);
        }
        else if(expressionBinary.getOperation().equals("-")) {
            int result = (left).getValue() - (right).getValue();
            return new ValueInt(result);
        }
        else if(expressionBinary.getOperation().equals("*")) {
            int result = (left).getValue() * (right).getValue();
            return new ValueInt(result);
        }
        else if(expressionBinary.getOperation().equals("/")) {
            int result = (left).getValue() / (right).getValue();
            return new ValueInt(result);
        }

        // TODO do not return null
        return null;

    }

    @Override
    public ValueInt visitExpressionConstantConfiguration(ExpressionConstantConfiguration expressionConstantConfiguration) {
        int value = 0;

//        if(this.activatedConfigurations.contains(expressionConstantConfiguration.getName())
//                || this.store.containsKey(expressionConstantConfiguration.getName())) {
//            value = 1;
//        }

        if(this.activatedConfigurations.contains(expressionConstantConfiguration.getName())) {
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
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        ValueInt value = statementAssignment.getRight().accept(this);
        this.store.put(statementAssignment.getVariable().getName(), value);
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
        ValueInt condition = statementIf.getCondition().accept(this);

        if(condition.getValue() > 0) {
            statementIf.getStatementThen().accept(this);
        }
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        ValueInt time = statementSleep.getTime().accept(this);
        this.sleepTime += time.getValue();
    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {
        statementAssignment.getCondition().accept(this);
        statementAssignment.getBody().accept(this);
    }

    public Map<String, ValueInt> getStore() { return this.store; }

    public int getSleepTime() { return this.sleepTime; }
}
