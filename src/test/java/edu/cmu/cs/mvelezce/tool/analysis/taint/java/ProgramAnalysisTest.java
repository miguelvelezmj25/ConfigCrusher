package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep2;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ProgramAnalysisTest {

    @Test
    public void testAnalysePipeline1() throws Exception {
        // TODO call Lotrack
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);

        // Regions to options
        Map<JavaRegion, Set<String>> relevantRegionToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        relevantRegionToOptions.put(region1, options);
        // TODO call Lotrack

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep1.FILENAME);

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>>  outputSave = ProgramAnalysis.analyse(Sleep1.CLASS, args, Sleep1.FILENAME, programFiles, relevantRegionToOptions);

        args = new String[0];
        Map<JavaRegion, Set<String>>  outputRead = ProgramAnalysis.analyse(Sleep1.CLASS, args, Sleep1.FILENAME, programFiles, relevantRegionToOptions);

        Assert.assertEquals(outputSave, outputRead);
    }

    @Test
    public void testAnalysePipeline2() throws Exception {
        // TODO call Lotrack
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        JavaRegion region1 = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.MAIN_METHOD, 23, 28);
        JavaRegion region2 = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.METHOD_1, 19, 20);

        // Regions to options
        Map<JavaRegion, Set<String>> relevantRegionToOptions = new HashMap<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        relevantRegionToOptions.put(region1, options);

        options = new HashSet<>();
        options.add("A");
        relevantRegionToOptions.put(region2, options);
        // TODO call Lotrack

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep2.FILENAME);

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> outputSave = ProgramAnalysis.analyse(Sleep2.CLASS, args, Sleep2.FILENAME, programFiles, relevantRegionToOptions);

        args = new String[0];
        Map<JavaRegion, Set<String>> outputRead = ProgramAnalysis.analyse(Sleep2.CLASS, args, Sleep2.FILENAME, programFiles, relevantRegionToOptions);

        Assert.assertEquals(outputSave, outputRead);
    }

}