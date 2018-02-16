package edu.cmu.cs.mvelezce.tool.analysis.taint.java.lotrack;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/28/17.
 */
public class ProgramAnalysisTest {

    private void checkIfEqual(Map<JavaRegion, Set<Set<String>>> outputSave, Map<JavaRegion, Set<Set<String>>> outputRead) {
        for(Map.Entry<JavaRegion, Set<Set<String>>> save : outputSave.entrySet()) {
            boolean oneIsEqual = false;
            for(Map.Entry<JavaRegion, Set<Set<String>>> read : outputRead.entrySet()) {
                JavaRegion saveRegion = save.getKey();
                Set<Set<String>> saveOptions = save.getValue();
                JavaRegion readRegion = read.getKey();
                Set<Set<String>> readOptions = read.getValue();

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
//        Map<JavaRegion, Set<Set<String>>> relevantRegionToOptions = new HashMap<>();
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
//        Map<JavaRegion, Set<Set<String>>>  outputSave = ProgramAnalysis.analyze(Sleep1.CLASS, args, Sleep1.FILENAME, programFiles, relevantRegionToOptions);
//
//        args = new String[0];
//        Map<JavaRegion, Set<Set<String>>>  outputRead = ProgramAnalysis.analyze(Sleep1.CLASS, args, Sleep1.FILENAME, programFiles, relevantRegionToOptions);
//
//        this.checkIfEqual(outputSave, outputRead);
//    }

    @Test
    public void testAnalysePipeline2() throws IOException {
//        String programName = "Dummy3";
//
//        // Program arguments
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> outputSave = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
//
//        args = new String[0];
//        Map<JavaRegion, Set<Set<String>>> outputRead = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
//
//        this.checkIfEqual(outputSave, outputRead);
    }

    @Test
    public void testAnalyse1() {
//        Map<JavaRegion, Set<Set<String>>> output = ProgramAnalysis.analyze(JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
////        Assert.assertEquals(4, output.size());
    }

    @Test
    public void testReplaceConstraintWithUsedTerms1() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("DISTTHRESHOLD");
//        usedTerms.add("TIMEOUT");
//        usedTerms.add("FREQTHRESHOLD");
//
//        String constraint = "(DISTTHRESHOLD = 0 ^ TIMEOUT_Theta ^ !(TIMEOUT_Eta) || !(FREQTHRESHOLD_Beta))";
//
//        String res = ProgramAnalysis.replaceConstraintWithUsedTerms(constraint, usedTerms);
//        System.out.println(res);
    }

    @Test
    public void testReplaceConstraintWithUsedTerms2() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("FREQTHRESHOLD");
//        usedTerms.add("TIMEOUT");
//        usedTerms.add("DISTTHRESHOLD");
//        usedTerms.add("MINALPHA");
//
//        String constraint = "(TIMEOUT_Beta ^ TIMEOUT = 0 ^ TIMEOUT_Gamma) || (TIMEOUT_Beta ^ DISTTHRESHOLD = 0 ^ TIMEOUT_Gamma) || (TIMEOUT_Beta ^ FREQTHRESHOLD = 0 ^ TIMEOUT_Gamma) || (TIMEOUT_Beta ^ MINALPHA = 0 ^ TIMEOUT_Gamma)";
//
//        String res = ProgramAnalysis.replaceConstraintWithUsedTerms(constraint, usedTerms);
//        System.out.println(res);
    }

    @Test
    public void testReplaceConstraintWithUsedTerms3() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("A");
//        usedTerms.add("B");
//        usedTerms.add("C");
//        usedTerms.add("D");
//
//        String constraint = "(!A = 0 ^ !(C = 0) || !(D = 0)) || (!B = 0 ^ !(C = 0) || !(D = 0))";
//
//        String res = ProgramAnalysis.replaceConstraintWithUsedTerms(constraint, usedTerms);
//        System.out.println(res);
    }

    @Test
    public void testGetConjunctions1() {
////        String constraint = "(DISTTHRESHOLD = 0 ^ TIMEOUT_Theta ^ !(TIMEOUT_Eta) || !(FREQTHRESHOLD_Beta))";
//        String constraint = "false || (false || false || true ^ false || false || false || !(A = 0) || false)";
//
//        List<String> res = ProgramAnalysis.getConjunctions(constraint);
//        Assert.assertEquals(res.size(), 1);
    }

    @Test
    public void testGetConjunctions2() {
//        String constraint = "(TIMEOUT_Beta ^ TIMEOUT = 0 ^ TIMEOUT_Gamma) || (TIMEOUT_Beta ^ DISTTHRESHOLD = 0 ^ TIMEOUT_Gamma) || (TIMEOUT_Beta ^ FREQTHRESHOLD = 0 ^ TIMEOUT_Gamma) || (TIMEOUT_Beta ^ MINALPHA = 0 ^ TIMEOUT_Gamma)";
//
//        List<String> res = ProgramAnalysis.getConjunctions(constraint);
//        Assert.assertEquals(res.size(), 4);
    }

    @Test
    public void testGetConjunctions3() {
//        String constraint = "(!A = 0 ^ !(C = 0) || !(D = 0)) || (!B = 0 ^ !(C = 0) || !(D = 0))";
//
//        List<String> res = ProgramAnalysis.getConjunctions(constraint);
//        Assert.assertEquals(res.size(), 2);
    }

    @Test
    public void testGetConstraintSet1() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("DISTTHRESHOLD");
//        usedTerms.add("TIMEOUT");
//        usedTerms.add("FREQTHRESHOLD");
//        usedTerms.add("MINALPHA");
//
//        String constraint = "(DISTTHRESHOLD = 0 ^ TIMEOUT ^ !(TIMEOUT) || !(FREQTHRESHOLD))";
//
//        Set<String> res = ProgramAnalysis.getConstraintSet(constraint, usedTerms);
//        Assert.assertEquals(res.size(), 3);
    }

    @Test
    public void testGetConstraintSet2() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("FREQTHRESHOLD");
//        usedTerms.add("TIMEOUT");
//        usedTerms.add("DISTTHRESHOLD");
//        usedTerms.add("MINALPHA");
//
//        String constraint = "(TIMEOUT ^ TIMEOUT = 0 ^ TIMEOUT)";
//
//        Set<String> res = ProgramAnalysis.getConstraintSet(constraint, usedTerms);
//        Assert.assertEquals(res.size(), 1);
    }

    @Test
    public void testGetConstraintSet3() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("A");
//        usedTerms.add("B");
//        usedTerms.add("C");
//        usedTerms.add("D");
//
//        String constraint = "(!A = 0 ^ !(C = 0) || !(D = 0))";
//
//        Set<String> res = ProgramAnalysis.getConstraintSet(constraint, usedTerms);
//        Assert.assertEquals(res.size(), 3);
    }

    @Test
    public void testGetConstraintSet4() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("A");
//        usedTerms.add("B");
//        usedTerms.add("C");
//        usedTerms.add("D");
//
//        String constraint = "(!B = 0 ^ !(C = 0) || !(D = 0))";
//
//        Set<String> res = ProgramAnalysis.getConstraintSet(constraint, usedTerms);
//        Assert.assertEquals(res.size(), 3);
    }

    @Test
    public void testGetConstraintSet5() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("FREQTHRESHOLD");
//        usedTerms.add("TIMEOUT");
//        usedTerms.add("DISTTHRESHOLD");
//        usedTerms.add("MINALPHA");
//
//        String constraint = "(TIMEOUT ^ DISTTHRESHOLD = 0 ^ TIMEOUT)";
//
//        Set<String> res = ProgramAnalysis.getConstraintSet(constraint, usedTerms);
//        Assert.assertEquals(res.size(), 2);
    }

    @Test
    public void testGetConstraintSet6() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("FREQTHRESHOLD");
//        usedTerms.add("TIMEOUT");
//        usedTerms.add("DISTTHRESHOLD");
//        usedTerms.add("MINALPHA");
//
//        String constraint = "(TIMEOUT ^ FREQTHRESHOLD = 0 ^ TIMEOUT)";
//
//        Set<String> res = ProgramAnalysis.getConstraintSet(constraint, usedTerms);
//        Assert.assertEquals(res.size(), 2);
    }

    @Test
    public void testGetConstraintSet7() {
//        List<String> usedTerms = new ArrayList<>();
//        usedTerms.add("FREQTHRESHOLD");
//        usedTerms.add("TIMEOUT");
//        usedTerms.add("DISTTHRESHOLD");
//        usedTerms.add("MINALPHA");
//
//        String constraint = "(TIMEOUT ^ MINALPHA = 0 ^ TIMEOUT)";
//
//        Set<String> res = ProgramAnalysis.getConstraintSet(constraint, usedTerms);
//        Assert.assertEquals(res.size(), 2);
    }

    @Test
    public void testElevator() throws IOException {
//        String programName = "elevator";
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testPngtastic() throws IOException {
//        String programName = "pngtastic";
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testZipme() throws IOException {
//        String programName = "zipme";
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testGPL() throws IOException {
//        String programName = "gpl";
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testSleep2() throws IOException {
//        String programName = "Sleep2";
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
////        String[] args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testSleep0() throws IOException {
//        String programName = "sleep0";
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testSleep30() throws IOException {
//        String programName = "sleep30";
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

    @Test
    public void testSleep31() throws IOException {
//        String programName = "sleep31";
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
    }

//    @Test
//    public void testSleep3() throws IOException {
//        String programName = "sleep3";
//        String sdgFile = USER_HOME + "/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep3/edu.cmu.cs.mvelezce.Sleep3.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep3.main";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, sdgFile, entryPoint, features);
//    }

//    @Test
//    public void testSleep15() throws IOException {
//        String programName = "sleep15";
//        String sdgFile = USER_HOME + "/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep15/edu.cmu.cs.mvelezce.Sleep15.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep15.main";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//        features.add("C");
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, sdgFile, entryPoint, features);
//    }

//    @Test
//    public void testSleep17() throws IOException {
//        String programName = "sleep17";
//        String sdgFile = USER_HOME + "/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep17/edu.cmu.cs.mvelezce.Sleep17.main.pdg";
//        String entryPoint = "edu.cmu.cs.mvelezce.Sleep17.main";
//        List<String> features = new ArrayList<>();
//        features.add("A");
//        features.add("B");
//        features.add("C");
//
//        // Program arguments
////        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        Map<JavaRegion, Set<Set<String>>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, sdgFile, entryPoint, features);
//    }

}