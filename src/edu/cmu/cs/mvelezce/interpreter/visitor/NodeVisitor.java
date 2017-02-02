package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.*;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.*;
import edu.cmu.cs.mvelezce.interpreter.ast.value.ValueInt;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class NodeVisitor implements Visitor<ValueInt>  {
    private final Map<ExpressionVariable, ValueInt> store;
    private Parser parser;
//    int time;
//    StringBuffer output;

    public NodeVisitor(Parser parser) {
        this.store = new HashMap<>();
        this.parser = parser;
    }

    public Object evaluate() {
        // TODO this should return a program
        Expression ast = this.parser.parse();
        return ast.accept(this);
    }

    public ValueInt evaluate(Expression ast) {
        return ast.accept(this);
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
    public ValueInt visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return new ValueInt(expressionConstantInt.getValue());
    }

    @Override
    public ValueInt visitExpressionUnary(ExpressionUnary expressionUnary) {
        ValueInt result = expressionUnary.getExpression().accept(this);

        // TODO it seems like the "-" should be gotten from somewhere
        // TODO it seems weird how the int is generated
        if(expressionUnary.getOperation().equals("-")) {
            return new ValueInt(-result.getValue());
        }

        return result;
    }

    @Override
    public ValueInt visitVarExpr(ExpressionVariable varExpr) {
        return this.store.get(varExpr);
    }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        ValueInt value = statementAssignment.getRight().accept(this);
        this.store.put(statementAssignment.getVariable(), value);
    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {

    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {

    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        System.out.println(statementSleep);
    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {

    }

}
