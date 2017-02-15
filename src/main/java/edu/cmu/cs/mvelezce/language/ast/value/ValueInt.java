package edu.cmu.cs.mvelezce.language.ast.value;

/**
 * A ValueInt that represent the result of evaluating an expression
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class ValueInt {
    private final int value;

    public ValueInt(int value) { this.value = value; }

    /**
     * Returns the value.
     *
     * @return
     */
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
