package edu.cmu.cs.mvelezce.tool.pipeline.java.analysis;

import edu.cmu.cs.mvelezce.tool.execute.java.approaches.BruteForce;
import edu.cmu.cs.mvelezce.tool.execute.java.approaches.SPLat;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaPipeline;
import org.junit.Test;

/**
 * Created by mvelezce on 6/30/17.
 */
public class CompareTest {

    @Test
    public void compareElevator1() throws Exception {
        String programName = "elevator";
        Compare.compare(programName, JavaPipeline.PM_RES_DIR, BruteForce.BF_RES_DIR);
    }

    @Test
    public void compareElevator2() throws Exception {
        String programName = "elevator";
        Compare.compare(programName, JavaPipeline.PM_RES_DIR, SPLat.SPLAT_RES_DIR);
    }

    @Test
    public void compareElevator3() throws Exception {
        String programName = "elevator";
        Compare.compare(programName, SPLat.SPLAT_RES_DIR, BruteForce.BF_RES_DIR);
    }

    @Test
    public void testComparePMToBFElevator() throws Exception {
        String programName = "elevator";
        Compare.comparePMToBF(programName);
    }

    @Test
    public void testComparePMToBFSleep1() throws Exception {
        String programName = "sleep1";
        Compare.comparePMToBF(programName);
    }

}