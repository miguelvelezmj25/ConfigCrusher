package edu.cmu.cs.mvelezce.tool.instrumentation.java.programs;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 4/21/17.
 */
public class MainTest {
    @Test
    public void testMain1() throws Exception {
        String[] args = new String[3];
        args[0] = "sleep";
        args[1] = "1";
        args[2] = "A";

        Main.main(args);
    }

}