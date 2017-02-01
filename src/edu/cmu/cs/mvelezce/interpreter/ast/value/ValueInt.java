package edu.cmu.cs.mvelezce.interpreter.ast.value;

/**
 * Created by mvelezce on 2/1/17.
 */
public class ValueInt {
    private final int value;

    public ValueInt(int value) {
        this.value = value;
    }

    public int getValue() { return this.value; }

    @Override
    public String toString() { return String.valueOf(this.value); }
}
