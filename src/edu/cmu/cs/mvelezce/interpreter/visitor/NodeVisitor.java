package edu.cmu.cs.mvelezce.interpreter.visitor;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionBinary;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionUnary;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class NodeVisitor implements Visitor {
    private Parser parser;

    public NodeVisitor(Parser parser) {
        this.parser = parser;
    }

    public Object evaluate() {
        // TODO this should return a program
        Expression ast = this.parser.parse();
        return ast.accept(this);
    }

    public Object evaluate(Expression ast) {
        return ast.accept(this);
    }

    @Override
    public Object visitExpressionBinary(ExpressionBinary expressionBinary) {
        Expression left = (Expression) expressionBinary.getLeft().accept(this);
        Expression right = (Expression) expressionBinary.getRight().accept(this);

        if(expressionBinary.getOperation().equals("+")) {
            int result = ((ExpressionConstantInt) left).getValue() + ((ExpressionConstantInt) right).getValue();
            return new ExpressionConstantInt(result);
        }
        else if(expressionBinary.getOperation().equals("-")) {
            int result = ((ExpressionConstantInt) left).getValue() - ((ExpressionConstantInt) right).getValue();
            return new ExpressionConstantInt(result);
        }
        else if(expressionBinary.getOperation().equals("*")) {
            int result = ((ExpressionConstantInt) left).getValue() * ((ExpressionConstantInt) right).getValue();
            return new ExpressionConstantInt(result);
        }
        else if(expressionBinary.getOperation().equals("/")) {
            int result = ((ExpressionConstantInt) left).getValue() / ((ExpressionConstantInt) right).getValue();
            return new ExpressionConstantInt(result);
        }

        return null;

    }

    @Override
    public Object visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return expressionConstantInt;
    }

    @Override
    public Object visitExpressionUnary(ExpressionUnary expressionUnary) {
        Expression expression = (Expression) expressionUnary.getExpression().accept(this);

        // TODO it seems like the "-" should be gotten from somewhere
        // TODO it seems weird how the int is generated
        if(expressionUnary.getOperation().equals("-")) {
            return new ExpressionConstantInt(-Integer.parseInt(expression.toString()));
        }

        return expression;
    }
}
