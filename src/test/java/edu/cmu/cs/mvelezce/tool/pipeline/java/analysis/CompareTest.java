package edu.cmu.cs.mvelezce.tool.pipeline.java.analysis;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mvelezce on 6/30/17.
 */
public class CompareTest {

    @Test
    public void testComparePMToBFElevator() throws Exception {
        String programName = "elevator";
        Compare.comparePMToBF(programName);
    }

}