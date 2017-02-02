package edu.cmu.cs.mvelezce.interpreter.lexer;

import edu.cmu.cs.mvelezce.interpreter.ast.Tag;
import edu.cmu.cs.mvelezce.interpreter.ast.Token;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class Lexer {

    private String input;
    private int position;
    private String currentCharacter;
    private Map<String, Token> reservedWords;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.currentCharacter = String.valueOf(this.input.charAt(this.position));
        this.reservedWords = new HashMap<>();
        this.reservedWords.put("sleep", new Token("sleep", Tag.SLEEP));
        this.reservedWords.put("while", new Token("while", Tag.WHILE));
        this.reservedWords.put("if", new Token("if", Tag.IF));
    }

    public Token getNextToken() {

        while(this.currentCharacter != null) {

            if(StringUtils.isWhitespace(this.currentCharacter)) {
                this.skipWhitespace();
                continue;
            }

            if(StringUtils.isNumeric(this.currentCharacter)) {
                return new Token(this.integer(), Tag.INTEGER);
            }

            if(StringUtils.isAlpha(this.currentCharacter)) {
                return this.id();
            }

            if(this.currentCharacter.equals("+")) {
                this.advance();
                return new Token("+", Tag.PLUS);
            }

            if(this.currentCharacter.equals("-")) {
                this.advance();
                return new Token("-", Tag.MINUS);
            }

            if(this.currentCharacter.equals("*")) {
                this.advance();
                return new Token("*", Tag.MULT);
            }

            if(this.currentCharacter.equals("/")) {
                this.advance();
                return new Token("/", Tag.DIV);
            }

            if(this.currentCharacter.equals("(")) {
                this.advance();
                return new Token("(", Tag.LEFT_PARENT);
            }

            if(this.currentCharacter.equals(")")) {
                this.advance();
                return new Token(")", Tag.RIGHT_PARENT);
            }

            if(this.currentCharacter.equals("\n")) {
                this.advance();
                return new Token("\n", Tag.NEW_LINE);
            }

            if(this.currentCharacter.equals("<")) {
                this.advance();
                return new Token("<", Tag.LESS_THAN);
            }

            if(this.currentCharacter.equals(">")) {
                this.advance();
                return new Token(">", Tag.GREATER_THAN);
            }

            if(this.currentCharacter.equals("=")) {
                this.advance();
                return new Token("=", Tag.EQUAL);
            }

            if(this.currentCharacter.equals(";")) {
                this.advance();
                return new Token(";", Tag.SEMI);
            }

            if(this.currentCharacter.equals("{")) {
                this.advance();
                return new Token("{", Tag.LEFT_BRACKET);
            }

            if(this.currentCharacter.equals("}")) {
                this.advance();
                return new Token("}", Tag.RIGHT_BRACKET);
            }

            if(this.currentCharacter.equals("!")) {
                this.advance();
                return new Token("!", Tag.NOT);
            }

            throw new IllegalArgumentException(this.currentCharacter + " is not a valid character");

        }

        return new Token();

    }

    public String integer() {
        String integer = "";

        while(StringUtils.isNumeric(this.currentCharacter)) {
            integer += this.currentCharacter;
            this.advance();
        }

        return integer;
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

    public String peek() {
        int peekPosition = this.position + 1;

        if(peekPosition >= this.input.length()) {
            return null;
        }

        return String.valueOf(this.input.charAt(this.position));
    }

    private Token id() {
        String result = "";
        while(StringUtils.isAlphanumeric(this.currentCharacter)) {
            result += this.currentCharacter;
            this.advance();
        }

        if(this.reservedWords.containsKey(result)) {
            return this.reservedWords.get(result);
        }

        if(StringUtils.isAllLowerCase(result)) {
            return new Token(result, Tag.VAR);
        }

        if(StringUtils.isAllUpperCase(result)) {
            return new Token(result, Tag.CONFIG);
        }

        throw new IllegalArgumentException(this.currentCharacter + " is not a valid id");
    }

}
