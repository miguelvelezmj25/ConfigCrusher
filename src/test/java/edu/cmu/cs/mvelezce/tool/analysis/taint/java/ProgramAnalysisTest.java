package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.pipeline.java.JavaPipeline;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ProgramAnalysisTest {

    private void checkIfEqual(Map<JavaRegion, Set<String>>  outputSave, Map<JavaRegion, Set<String>>  outputRead) {
        for(Map.Entry<JavaRegion, Set<String>> save : outputSave.entrySet()) {
            boolean oneIsEqual = false;
            for(Map.Entry<JavaRegion, Set<String>> read : outputRead.entrySet()) {
                JavaRegion saveRegion = save.getKey();
                Set<String> saveOptions = save.getValue();
                JavaRegion readRegion = read.getKey();
                Set<String> readOptions = read.getValue();

                if(saveRegion.getRegionID().equals(readRegion.getRegionID()) && saveRegion.getStartBytecodeIndex() == readRegion.getStartBytecodeIndex()
//                        && saveRegion.getEndBytecodeIndex() == readRegion.getEndBytecodeIndex()
                        && saveRegion.getRegionPackage().equals(readRegion.getRegionPackage())
                        && saveRegion.getRegionClass().equals(readRegion.getRegionClass())
                        && saveRegion.getRegionMethod().equals(readRegion.getRegionMethod())
                        && saveOptions.equals(readOptions)) {
                    oneIsEqual = true;
                    break;
                }
            }

            Assert.assertTrue(oneIsEqual);
        }
    }

//    @Test
//    public void testAnalysePipeline1() throws Exception {
//        // TODO call Lotrack
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        Map<JavaRegion, Set<String>> relevantRegionToOptions = new HashMap<>();
//        Set<String> relevantOptions = new HashSet<>();
//        relevantOptions.add("A");
//        JavaRegion region = new JavaRegion("ecb89258-9de1-416f-955c-194d09e9b249", Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 20, -1);
//        relevantRegionToOptions.put(region, relevantOptions);
//        // TODO call Lotrack
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep1.FILENAME);
//
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<String>>  outputSave = ProgramAnalysis.analyze(Sleep1.CLASS, args, Sleep1.FILENAME, programFiles, relevantRegionToOptions);
//
//        args = new String[0];
//        Map<JavaRegion, Set<String>>  outputRead = ProgramAnalysis.analyze(Sleep1.CLASS, args, Sleep1.FILENAME, programFiles, relevantRegionToOptions);
//
//        this.checkIfEqual(outputSave, outputRead);
//    }

    @Test
    public void testAnalysePipeline2() throws IOException, ParseException {
        String programName = "Dummy3";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>>  outputSave = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);

        args = new String[0];
        Map<JavaRegion, Set<String>>  outputRead = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);

        this.checkIfEqual(outputSave, outputRead);
    }

    @Test
    public void testAnalyse1() throws ParseException {
        Map<JavaRegion, Set<String>> output = ProgramAnalysis.analyze(JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
        Assert.assertEquals(4, output.size());
    }

    @Test
    public void testElevator() throws IOException, ParseException {
        String programName = "elevator";

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testGPL() throws IOException, ParseException {
        String programName = "gpl";

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testElevatorm() throws IOException, ParseException {
        String programName = "elevator-m";

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testSleep2() throws IOException, ParseException {
        String programName = "Sleep2";

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testSleep3() throws IOException, ParseException {
        String programName = "sleep3";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper/src/main/resources/joana/programs/sleep3/edu.cmu.cs.mvelezce.Sleep3.main.pdg";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep3.main";
        List<String> features = new ArrayList<>();
        features.add("A");
        features.add("B");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, sdgFile, entryPoint, features);
    }

}