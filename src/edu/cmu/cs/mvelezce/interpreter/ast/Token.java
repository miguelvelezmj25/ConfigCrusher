package edu.cmu.cs.mvelezce.interpreter.ast;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class Token {

    private String value;
    private Tag tag;

    public Token(String value, Tag tag) {
        this.value = value;
        this.tag = tag;
    }

    public Token() {
        this(null, Tag.EOF);
    }

    public String getValue() { return this.value; }

    public Tag getTag() { return this.tag; }

    @Override
    public String toString() { return "Token [value=" + this.value + ", tag=" + this.tag + "]"; }
}
