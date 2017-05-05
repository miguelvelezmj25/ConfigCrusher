package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep2;
import edu.cmu.cs.mvelezce.java.programs.Sleep3;
import edu.cmu.cs.mvelezce.java.programs.Sleep4;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaRegion;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class InstrumenterTest {

    @Test
    public void testInstrumentPipeline1() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 20);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep1.FILENAME);

        Instrumenter.instrument(Sleep1.CLASS, Sleep1.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep1.CLASS, programFiles));
    }

    @Test
    public void testInstrumentPipeline2() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.MAIN_METHOD, 20);
        regions.add(region);

        region = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.METHOD_1, 16);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep2.FILENAME);

        Instrumenter.instrument(Sleep2.CLASS, Sleep2.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep2.CLASS, programFiles));
    }

    @Test
    public void testInstrumentPipeline3() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 28);
        regions.add(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 45);
        regions.add(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_1, 16);
        regions.add(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_2, 16);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep3.FILENAME);

        Instrumenter.instrument(Sleep3.CLASS, Sleep3.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep3.CLASS, programFiles));
    }

    @Test
    public void testInstrumentPipeline4() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 20);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep4.FILENAME);

        Instrumenter.instrument(Sleep4.CLASS, Sleep4.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep4.CLASS, programFiles));
    }

}