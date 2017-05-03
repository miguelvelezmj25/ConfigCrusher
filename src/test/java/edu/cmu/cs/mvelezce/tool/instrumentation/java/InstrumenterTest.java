package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.java.programs.Sleep1;
import edu.cmu.cs.mvelezce.java.programs.Sleep3;
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
        JavaRegion region = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep1.FILENAME);

        Instrumenter.instrument(Sleep1.CLASS, Sleep1.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep1.CLASS, programFiles));
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
        JavaRegion region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 31, 36);
        regions.add(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 48, 53);
        regions.add(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_1, 19, 20);
        regions.add(region);

        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_2, 19, 20);
        regions.add(region);

        // Program files
        List<String> programFiles = new ArrayList<>();
        programFiles.add(Sleep3.FILENAME);

        Instrumenter.instrument(Sleep3.CLASS, Sleep3.FILENAME, args, programFiles, regions);

        Assert.assertTrue(Instrumenter.checkAllFilesInstrumented(Sleep3.CLASS, programFiles));
    }

}