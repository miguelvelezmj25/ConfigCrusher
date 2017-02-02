package edu.cmu.cs.mvelezce.interpreter.parser;

import edu.cmu.cs.mvelezce.interpreter.ast.Tag;
import edu.cmu.cs.mvelezce.interpreter.ast.Token;
import edu.cmu.cs.mvelezce.interpreter.ast.expression.*;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.*;
import edu.cmu.cs.mvelezce.interpreter.lexer.Lexer;

import java.util.LinkedList;
import java.util.List;

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

    public Statement parse() {
        List<Statement> program = new LinkedList<>();
        program.add(this.stmt());

        while(this.currentToken.getTag() != Tag.EOF) {
            program.add(this.stmt());
        }

        return new StatementBlock(program);
    }

    private Statement stmt() {
        if(this.currentToken.getTag() == Tag.VAR || this.currentToken.getTag() == Tag.CONFIG) {
            Token token = this.currentToken;

            ExpressionVariable variable = null;

            if(this.currentToken.getTag() == Tag.VAR) {
                this.checkToken(Tag.VAR);
                variable = new ExpressionVariable(token.getValue());

            }
            else {
                this.checkToken(Tag.CONFIG);
                variable = new ExpressionConfiguration(token.getValue());
            }

            token = this.currentToken;
            this.checkToken(Tag.EQUAL);
            String operation = token.getValue();

            return new StatementAssignment(variable, operation, this.expr());
        }

        if(this.currentToken.getTag() == Tag.IF) {
            this.checkToken(Tag.IF);
            this.checkToken(Tag.LEFT_PARENT);
            Expression expression = this.expr();
            this.checkToken(Tag.RIGHT_PARENT);
            this.checkToken(Tag.LEFT_BRACKET);

            List<Statement> statements = new LinkedList<>();
            statements.add(this.stmt());

            while(this.currentToken.getTag() != Tag.RIGHT_BRACKET) {
                statements.add(this.stmt());
            }

            this.checkToken(Tag.RIGHT_BRACKET);

            return new StatementIf(expression, new StatementBlock(statements));
        }

        if(this.currentToken.getTag() == Tag.SLEEP) {
            this.checkToken(Tag.SLEEP);
            this.checkToken(Tag.LEFT_PARENT);
            Expression expression = this.expr();
            this.checkToken(Tag.RIGHT_PARENT);

            return new StatementSleep(expression);
        }

        if(this.currentToken.getTag() == Tag.WHILE) {
            this.checkToken(Tag.WHILE);
            this.checkToken(Tag.LEFT_PARENT);
            Expression expression = this.expr();
            this.checkToken(Tag.RIGHT_PARENT);
            this.checkToken(Tag.LEFT_BRACKET);

            List<Statement> statements = new LinkedList<>();
            statements.add(this.stmt());

            while(this.currentToken.getTag() != Tag.RIGHT_BRACKET) {
                statements.add(this.stmt());
            }

            this.checkToken(Tag.RIGHT_BRACKET);

            return new StatementWhile(expression, new StatementBlock(statements));
        }

        throw new IllegalArgumentException("Error while parsing input");
    }

    private Expression term() {
        Token token = this.currentToken;

        if (this.currentToken.getTag() == Tag.NOT) {
            this.checkToken(Tag.NOT);
            return new ExpressionUnary(token.getValue(), this.term());
        }

        if(token.getTag() == Tag.VAR) {
            this.checkToken(Tag.VAR);
            return new ExpressionVariable(token.getValue());
        }

        if(token.getTag() == Tag.INTEGER) {
            this.checkToken(Tag.INTEGER);
            return new ExpressionConstantInt(Integer.parseInt(token.getValue()));
        }

        if(token.getTag() == Tag.CONFIG) {
            this.checkToken(Tag.CONFIG);
            return new ExpressionConfiguration(token.getValue());
        }

        throw new IllegalArgumentException("Error while parsing input");
    }

    private Expression expr() {
        Expression expression = this.term();

        Token token = this.currentToken;

        if(token.getTag() == Tag.PLUS) {
            this.checkToken(Tag.PLUS);
        }
        else if(token.getTag() == Tag.MINUS) {
            this.checkToken(Tag.MINUS);
        }
        else if(token.getTag() == Tag.MULT) {
            this.checkToken(Tag.MULT);
        }
        else if(token.getTag() == Tag.DIV) {
            this.checkToken(Tag.DIV);
        }
        else {
            return expression;
        }

        return new ExpressionBinary(expression, token.getValue(), this.term());
    }

//    private Expression expr() {
//        Expression expression = this.term();
//
//        while(this.currentToken.getTag() == Tag.PLUS
//                || this.currentToken.getTag() == Tag.MINUS) {
//            Token token = this.currentToken;
//            if(token.getTag() == Tag.PLUS) {
//                this.checkToken(Tag.PLUS);
//            }
//            else {
//                this.checkToken(Tag.MINUS);
//            }
//
//            expression = new ExpressionBinary(expression, token.getValue(), this.term());
//        }
//
//        return expression;
//    }
//
//    private Expression term() {
//        Expression expression = this.factor();
//
//        while(this.currentToken.getTag() == Tag.MULT
//                || this.currentToken.getTag() == Tag.DIV) {
//            Token token = this.currentToken;
//
//            if(token.getTag() == Tag.MULT) {
//                this.checkToken(Tag.MULT);
//            }
//            else {
//                this.checkToken(Tag.DIV);
//            }
//
//            expression = new ExpressionBinary(expression, token.getValue(), this.factor());
//        }
//
//        return expression;
//
//    }
//
//    private Expression factor() {
//        Token token = this.currentToken;
//
//        if (this.currentToken.getTag() == Tag.PLUS) {
//            this.checkToken(Tag.PLUS);
//            return new ExpressionUnary(token.getValue(), this.factor());
//        }
//        else if(this.currentToken.getTag() == Tag.MINUS) {
//            this.checkToken(Tag.MINUS);
//            return new ExpressionUnary(token.getValue(), this.factor());
//        }
//
//        if(this.currentToken.getTag() == Tag.INTEGER) {
//            this.checkToken(Tag.INTEGER);
//            return new ExpressionConstantInt(Integer.parseInt(token.getValue()));
//        }
//
//        this.checkToken(Tag.LEFT_PARENT);
//        Expression expression = this.expr();
//        this.checkToken(Tag.RIGHT_PARENT);
//
//        return expression;
//    }

    private void checkToken(Tag type) {
        if(this.currentToken.getTag() == type) {
            this.currentToken = this.lexer.getNextToken();
        }
        else {
            throw new IllegalArgumentException("Invalid syntax");
        }
    }

}
