package edu.cmu.cs.mvelezce.interpreter.lexer;

import edu.cmu.cs.mvelezce.interpreter.ast.Tag;
import edu.cmu.cs.mvelezce.interpreter.ast.Token;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class Lexer {

    private String input;
    private int position;
    private String currentCharacter;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.currentCharacter = String.valueOf(this.input.charAt(this.position));
    }

    public Token getNextToken() {

        while(this.currentCharacter != null) {

            if (StringUtils.isWhitespace(this.currentCharacter)) {
                this.skipWhitespace();
                continue;
            }

            if (StringUtils.isNumeric(this.currentCharacter)) {
                return new Token(this.integer(), Tag.INTEGER);
            }

            if (this.currentCharacter.equals("+")) {
                this.advance();
                return new Token("+", Tag.PLUS);
            }

            if (this.currentCharacter.equals("-")) {
                this.advance();
                return new Token("-", Tag.MINUS);
            }

            if (this.currentCharacter.equals("*")) {
                this.advance();
                return new Token("*", Tag.MULT);
            }

            if (this.currentCharacter.equals("/")) {
                this.advance();
                return new Token("/", Tag.DIV);
            }

            if (this.currentCharacter.equals("(")) {
                this.advance();
                return new Token("/", Tag.LEFT_PARENT);
            }

            if (this.currentCharacter.equals(")")) {
                this.advance();
                return new Token("/", Tag.RIGHT_PARENT);
            }

            throw new IllegalArgumentException(this.currentCharacter + " is not a valid character");

        }

        return new Token();

    }

    public int integer() {
        String integer = "";

        while(StringUtils.isNumeric(this.currentCharacter)) {
            integer += this.currentCharacter;
            this.advance();
        }

        return Integer.parseInt(integer);
    }

    private void skipWhitespace() {
        while(StringUtils.isWhitespace(this.currentCharacter)) {
            this.advance();
        }
    }

    private void advance() {
        this.position++;

        if(this.position >= input.length()) {
            this.currentCharacter = null;
        }
        else {
            this.currentCharacter = String.valueOf(this.input.charAt(this.position));
        }
    }

}
