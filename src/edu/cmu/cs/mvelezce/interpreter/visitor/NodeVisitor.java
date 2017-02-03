package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.*;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.*;
import edu.cmu.cs.mvelezce.interpreter.ast.value.ValueInt;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class NodeVisitor implements Visitor<ValueInt>  {
    private final Map<String, ValueInt> store;
    private Parser parser;
    private int sleepTime;
//    StringBuffer output;

    public NodeVisitor(Parser parser) {
        this.store = new HashMap<>();
        this.parser = parser;
        this.sleepTime = 0;
    }

    public Object evaluate() {
        return null;
//        // TODO this should return a program
//        Expression ast = this.parser.parse();
//        return ast.accept(this);
    }

    public Map<String, ValueInt> evaluate(Statement ast) {
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
    public ValueInt visitExpressionConfiguration(ExpressionConfiguration expressionConfiguration) {
        return this.store.get(expressionConfiguration.getName());
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

}
