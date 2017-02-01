package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.*;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.StatementAssignment;
import edu.cmu.cs.mvelezce.interpreter.ast.value.ValueInt;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;

import java.util.HashMap;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class NodeVisitor implements Visitor<ValueInt> {
    private Parser parser;

//    private final HashMap<String, ValueInt> store = new HashMap<>();
//    int time;
//    StringBuffer output;

    public NodeVisitor(Parser parser) {
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
//        return store.get(varExpr.getName());
        return null;
    }

    public void visitAssignment(StatementAssignment a) {
//        ValueInt rhv = a.getRight().accept(this);
//        store.put(a.getLeft(), rhv);
    }
}
