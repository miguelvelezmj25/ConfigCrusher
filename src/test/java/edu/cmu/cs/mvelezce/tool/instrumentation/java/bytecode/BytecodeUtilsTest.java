package edu.cmu.cs.mvelezce.tool.instrumentation.java.bytecode;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 6/26/17.
 */
public class BytecodeUtilsTest {

    @Test
    public void toBytecodeDescriptor() {
        String desc = BytecodeUtils.toBytecodeDescriptor("boolean[]");
        System.out.println(desc);
    }

}