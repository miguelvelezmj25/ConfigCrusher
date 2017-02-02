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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueInt valueInt = (ValueInt) o;

        return value == valueInt.value;
    }

    @Override
    public int hashCode() { return this.value; }
}
