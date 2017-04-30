package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Sleep4;
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
        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 31, 36);
        regions.add(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 48, 53);
        regions.add(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_1, 19, 20);
        regions.add(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_2, 19, 20);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep4.FILENAME);

        Instrumenter.instrument(Sleep4.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(programFiles));
    }

    @Test
    public void testInstrument1() throws Exception {
        // Java Region
        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
        Set<JavaRegion> regions = new HashSet<>();
        JavaRegion region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 31, 36);
        regions.add(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 48, 53);
        regions.add(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_1, 19, 20);
        regions.add(region);

        region = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.METHOD_2, 19, 20);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep4.FILENAME);

        // Instrument and assert
        Instrumenter.instrument(Sleep4.FILENAME, programFiles, regions);
    }

}