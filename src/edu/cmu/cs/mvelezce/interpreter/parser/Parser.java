package edu.cmu.cs.mvelezce.interpreter.parser;

import edu.cmu.cs.mvelezce.interpreter.ast.Tag;
import edu.cmu.cs.mvelezce.interpreter.ast.Token;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.Expression;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionBinary;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.ExpressionUnary;
import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = this.lexer.getNextToken();
    }

    public Expression parse() { return this.expr(); }

    private Expression expr() {
        Expression expression = this.term();

        while(this.currentToken.getTag() == Tag.PLUS
                || this.currentToken.getTag() == Tag.MINUS) {
            Token token = this.currentToken;
            if(token.getTag() == Tag.PLUS) {
                this.checkToken(Tag.PLUS);
            }
            else {
                this.checkToken(Tag.MINUS);
            }

            expression = new ExpressionBinary(expression, token.getValue(), this.term());
        }

        return expression;
    }

    private Expression term() {
        Expression expression = this.factor();

        while(this.currentToken.getTag() == Tag.MULT
                || this.currentToken.getTag() == Tag.DIV) {
            Token token = this.currentToken;

            if(token.getTag() == Tag.MULT) {
                this.checkToken(Tag.MULT);
            }
            else {
                this.checkToken(Tag.DIV);
            }

            expression = new ExpressionBinary(expression, token.getValue(), this.factor());
        }

        return expression;

    }

    private Expression factor() {
        Token token = this.currentToken;

        if (this.currentToken.getTag() == Tag.PLUS) {
            this.checkToken(Tag.PLUS);
            return new ExpressionUnary(token.getValue(), this.factor());
        }
        else if(this.currentToken.getTag() == Tag.MINUS) {
            this.checkToken(Tag.MINUS);
            return new ExpressionUnary(token.getValue(), this.factor());
        }

        if(this.currentToken.getTag() == Tag.INTEGER) {
            this.checkToken(Tag.INTEGER);
            return new ExpressionConstantInt(Integer.parseInt(token.getValue()));
        }

        this.checkToken(Tag.LEFT_PARENT);
        Expression expression = this.expr();
        this.checkToken(Tag.RIGHT_PARENT);

        return expression;
    }

    private void checkToken(Tag type) {
        if(this.currentToken.getTag() == type) {
            this.currentToken = this.lexer.getNextToken();
        }
        else {
            throw new IllegalArgumentException("Invalid syntax");
        }
    }

}
