package edu.cmu.cs.mvelezce.language.ast;

/**
 * A Token representing a part of a program with a specific type.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class Token {

    private String value;
    private Tag tag;

    /**
     * Initializes a {@code Token}.
     *
     * @param value
     * @param tag
     */
    public Token(String value, Tag tag) {
        if(tag == null) {
            throw new IllegalArgumentException("The tag cannot be null");
        }

        this.value = value;
        this.tag = tag;
    }

    /**
     * Initializes a {@code Token} that represents the end of file object. This Token has a null value.
     */
    public Token() {
        this(null, Tag.EOF);
    }

    /**
     * Returns the value of this token.
     *
     * @return
     */
    public String getValue() { return this.value; }

    /**
     * Returns the tag of this token.
     *
     * @return
     */
    public Tag getTag() { return this.tag; }

    @Override
    public String toString() { return "Token [value=" + this.value + ", tag=" + this.tag + "]"; }
}
