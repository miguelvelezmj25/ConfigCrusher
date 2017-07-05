package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.test.util.repeat.Repeat;
import edu.cmu.cs.mvelezce.test.util.repeat.RepeatRule;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import org.json.simple.parser.ParseException;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Created by mvelezce on 6/30/17.
 */
public class BruteForceTest {

    public static void saveBFPerformance(String programName, Set<PerformanceEntry> measuredPerf) throws IOException {
        File file = new File(edu.cmu.cs.mvelezce.tool.execute.java.BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);

        if(file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("Could not delete " + file);
            }
        }

        StringBuilder result = new StringBuilder();
        result.append("configuration,performance");
        result.append("\n");

        for(PerformanceEntry perfEntry : measuredPerf) {
            if(perfEntry.getRegionToInnerRegions().size() != 1) {
                throw new RuntimeException("The performance entry should only have measured the entire program " + perfEntry.getRegionToInnerRegions().keySet());
            }

            result.append('"');
            result.append(perfEntry.getConfiguration());
            result.append('"');
            result.append(",");
            long perf = perfEntry.getRegionsToExecutionTime().values().iterator().next();
            result.append(perf / 1000000000.0);
            result.append("\n");
        }

        File directory = new File(edu.cmu.cs.mvelezce.tool.execute.java.BruteForce.BF_RES_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = directory + "/" + programName + Options.DOT_CSV;
        file = new File(outputFile);
        FileWriter writer = new FileWriter(file, true);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

//    @Rule
//    public RepeatRule repeatRule = new RepeatRule();
//
//    public void deleteBFResult(String programName) {
//        if(repeatRule.getIteration() == 0) {
//            File file = new File(edu.cmu.cs.mvelezce.tool.execute.java.BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);
//
//            if(file.exists()) {
//                if(!file.delete()) {
//                    throw new RuntimeException("Could not delete " + file);
//                }
//            }
//        }
//    }

    @Test
    public void testElevator() throws IOException, ParseException, InterruptedException {
        String programName = "elevator";
        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";
        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";

        Set<PerformanceEntry> measuredPerf = BruteForce.measure(programName, 5, srcDir, classDir, entryPoint);
        BruteForceTest.saveBFPerformance(programName, measuredPerf);
    }

    @Test
    public void testZipme() throws IOException, ParseException, InterruptedException {
        String programName = "zipme";
        String srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/";
        String classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";

        Set<PerformanceEntry> measuredPerf = BruteForce.measure(programName, 1, srcDir, classDir, entryPoint);
        BruteForceTest.saveBFPerformance(programName, measuredPerf);
    }

}