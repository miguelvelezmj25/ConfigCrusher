package edu.cmu.cs.mvelezce.interpreter.ast;

/**
 * Created by miguelvelez on 1/31/17.
 */
public class Token {

    private Object value;
    private Tag tag;

    public Token(Object value, Tag tag) {
        this.value = value;
        this.tag = tag;
    }

    public Token() {
        this(null, Tag.EOF);
    }

    public Object getValue() { return this.value; }

    public Tag getTag() { return this.tag; }

    @Override
    public String toString() { return "Token [value=" + this.value + ", tag=" + this.tag + "]"; }
}
