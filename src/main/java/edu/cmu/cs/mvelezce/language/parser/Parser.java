package edu.cmu.cs.mvelezce.language.parser;

import edu.cmu.cs.mvelezce.language.ast.Tag;
import edu.cmu.cs.mvelezce.language.ast.Token;
import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
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
        List<Statement> program = new ArrayList<>();
        program.add(this.stmt());

        while(this.currentToken.getTag() != Tag.EOF) {
            program.add(this.stmt());
        }

        return new StatementBlock(program);
    }

    private Statement stmt() {
        if(this.currentToken.getTag() == Tag.VAR || this.currentToken.getTag() == Tag.CONFIG) {
            Token token = this.currentToken;

            this.checkToken(Tag.VAR);
            ExpressionVariable variable = new ExpressionVariable(token.getValue());

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

            List<Statement> statements = new ArrayList<>();
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

            List<Statement> statements = new ArrayList<>();
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
            return new ExpressionConfigurationConstant(token.getValue());
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

    private void checkToken(Tag type) {
        if(this.currentToken.getTag() == type) {
            this.currentToken = this.lexer.getNextToken();
        }
        else {
            throw new IllegalArgumentException("Invalid syntax");
        }
    }

}
