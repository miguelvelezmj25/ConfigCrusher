package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.java.programs.*;
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

    // TODO corner case
//    @Test
//    public void testInstrumentPipeline5() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        Set<JavaRegion> regions = new HashSet<>();
//        JavaRegion region = new JavaRegion(Sleep5.PACKAGE, Sleep5.CLASS, Sleep5.MAIN_METHOD, 20);
//        regions.add(region);
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep5.FILENAME);
//
//        Instrumenter.instrument(Sleep5.CLASS, Sleep5.FILENAME, args, programFiles, regions);
//
//        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep5.CLASS, programFiles));
//    }

    // TODO corner case
//    @Test
//    public void testInstrumentPipeline6() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        Set<JavaRegion> regions = new HashSet<>();
//        JavaRegion region = new JavaRegion(Sleep6.PACKAGE, Sleep6.CLASS, Sleep6.MAIN_METHOD, 20);
//        regions.add(region);
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep6.FILENAME);
//
//        Instrumenter.instrument(Sleep6.CLASS, Sleep6.FILENAME, args, programFiles, regions);
//
//        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep6.CLASS, programFiles));
//    }

    @Test
    public void testInstrumentPipeline7() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep7.PACKAGE, Sleep7.CLASS, Sleep7.MAIN_METHOD, 20);
        regions.add(region);

        region = new JavaRegion(Sleep7.PACKAGE, Sleep7.CLASS, Sleep7.MAIN_METHOD, 41);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep7.FILENAME);

        Instrumenter.instrument(Sleep7.CLASS, Sleep7.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep7.CLASS, programFiles));
    }

    @Test
    public void testInstrumentPipeline8() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.MAIN_METHOD, 20);
        regions.add(region);

        region = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.MAIN_METHOD, 42);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep8.FILENAME);

        Instrumenter.instrument(Sleep8.CLASS, Sleep8.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep8.CLASS, programFiles));
    }

    @Test
    public void testInstrumentPipeline9() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.MAIN_METHOD, 24);
        regions.add(region);

        region = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.MAIN_METHOD, 41);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep9.FILENAME);

        Instrumenter.instrument(Sleep9.CLASS, Sleep9.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep9.CLASS, programFiles));
    }

    @Test
    public void testInstrumentPipeline10() throws Exception {
        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.MAIN_METHOD, 28);
        regions.add(region);

        region = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.MAIN_METHOD, 42);
        regions.add(region);

        region = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.METHOD_1, 16);
        regions.add(region);

        region = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.METHOD_2, 28);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep10.FILENAME);

        Instrumenter.instrument(Sleep10.CLASS, Sleep10.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep10.CLASS, programFiles));
    }
//
//    @Test
//    public void testInstrumentPipeline4() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        Set<JavaRegion> regions = new HashSet<>();
//        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 20);
//        regions.add(region);
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep4.FILENAME);
//
//        Instrumenter.instrument(Sleep4.CLASS, Sleep4.FILENAME, args, programFiles, regions);
//
//        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep4.CLASS, programFiles));
//    }
//
//    @Test
//    public void testInstrumentPipeline4() throws Exception {
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        Set<JavaRegion> regions = new HashSet<>();
//        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 20);
//        regions.add(region);
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep4.FILENAME);
//
//        Instrumenter.instrument(Sleep4.CLASS, Sleep4.FILENAME, args, programFiles, regions);
//
//        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep4.CLASS, programFiles));
//    }

}