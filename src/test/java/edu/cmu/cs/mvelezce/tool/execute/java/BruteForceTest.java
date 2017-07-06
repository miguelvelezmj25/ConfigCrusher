package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mvelezce on 6/30/17.
 */
public class BruteForceTest {

    @Test
    public void testElevator() throws IOException, ParseException, InterruptedException {
        String programName = "elevator";
        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";
        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";

//        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5, srcDir, classDir, entryPoint);
        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
    }

    @Test
    public void testZipme() throws IOException, ParseException, InterruptedException {
        String programName = "zipme";
        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/";
        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";

        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1, srcDir, classDir, entryPoint);
    }

}