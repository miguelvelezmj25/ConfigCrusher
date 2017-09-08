package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.Sleep14;
import edu.cmu.cs.mvelezce.Sleep18;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipelineTest {

    public static final double TIMING_ERROR = 0.5;
//    public static final double TIMING_ERROR = 0.05;
//    public static final double TIMING_ERROR = 1.0;

    @Test
    public void testSleep14() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep14";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep14";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

        // TESTING
        args = new String[0];
        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        configurations = Helper.getConfigurations(options);

        for(Set<String> configuration : configurations) {
            System.out.println(configuration);
            Adapter adapter = new SleepAdapter();
            String[] sleepConfiguration = adapter.configurationAsMainArguments(configuration);
            long start = System.nanoTime();
            Sleep14.main(sleepConfiguration);
            long end = System.nanoTime();

            // TODO
//            System.out.println(pm.evaluate(configuration));
//            System.out.println(Region.getSecondsExecutionTime(start, end));
//            Assert.assertEquals(pm.evaluate(configuration), Region.getSecondsExecutionTime(start, end), JavaPipelineTest.TIMING_ERROR);
        }
    }

    @Test
    public void testSleep18() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep18";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep18";

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

        // TESTING
        args = new String[0];
        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> configurations = compression.compressConfigurations(args);
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        configurations = Helper.getConfigurations(options);

        for(Set<String> configuration : configurations) {
            System.out.println(configuration);
            Adapter adapter = new SleepAdapter();
            String[] sleepConfiguration = adapter.configurationAsMainArguments(configuration);
            long start = System.nanoTime();
            Sleep18.main(sleepConfiguration);
            long end = System.nanoTime();

            // TODO
//            System.out.println(pm.evaluate(configuration));
//            System.out.println(Region.getSecondsExecutionTime(start, end));
//            Assert.assertEquals(pm.evaluate(configuration), Region.getSecondsExecutionTime(start, end), JavaPipelineTest.TIMING_ERROR);
        }
    }

    @Test
    public void testZip() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "zipme";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/zipme/out/production/zipme/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/zipme/";
        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

        // Program arguments
//        args = new String[0];

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i5";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModel(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
    }

    @Test
    public void testZipSimple() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "zipme-simple";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/zipme/out/production/zipme/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/zipme/";
        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

        // Program arguments
//        args = new String[0];

//        args = new String[1];
//        args[0] = "-saveres";

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
        System.out.println(pm);
    }

    @Test
    public void testElevatorSimple() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "elevator-simple";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/out/production/elevator/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/";
        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

        // Program arguments
//        args = new String[0];

//        args = new String[1];
//        args[0] = "-saveres";

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
        System.out.println(pm);

//
    }

    @Test
    public void testPngtastic() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "pngtastic";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/target/classes/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic/target/classes/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic/";
        String entryPoint = "com.googlecode.pngtastic.PngtasticColorCounter";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testElevator() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "elevator";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/out/production/elevator/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/";
        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i5";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep1";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i5";

        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep3() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep3";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep3";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep21() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep21";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep21";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep22() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep22";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep22";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep23() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep23";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep23";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep24() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep24";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep24";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep25() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep25";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep25";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";R

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep26() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep26";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep26";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep27() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep27";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep27";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);

        // T = + 1.31 + 0.2A - 0.3B + 0.4C
    }

    @Test
    public void testSleep28() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep28";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep28";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep29() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep29";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep29";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep2() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep2";
        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep2";

        // Program arguments
        String[] args;
        args = new String[0];

        Map<JavaRegion, Set<String>> partialRegionsToOptions = null; // TODO make change since interface changed ProgramAnalysis.analyze(programName, args);

//        args = new String[1];
//        args[0] = "-saveres";
//        args[0] = "-i1";

        args = new String[3];
        args[0] = "-delres";
        args[1] = "-saveres";
        args[2] = "-i1";

//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
                instrumentSrcDirectory, instrumentClassDirectory, entryPoint, partialRegionsToOptions);
//        JavaPipeline.buildPerformanceModelRepeat(programName, args, originalSrcDirectory, originalClassDirectory,
//                instrumentSrcDirectory, instrumentClassDirectory, entryPoint);
    }

    @Test
    public void testSleep1_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep1";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep1/edu.cmu.cs.mvelezce.Sleep1.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep2_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep2";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep2";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep2/edu.cmu.cs.mvelezce.Sleep2.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep3_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep3";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep3";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep3/edu.cmu.cs.mvelezce.Sleep3.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep4_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep4";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep4";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep4/edu.cmu.cs.mvelezce.Sleep4.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep5_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep5";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep5";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep5/edu.cmu.cs.mvelezce.Sleep5.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep7_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep7";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep7";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep7/edu.cmu.cs.mvelezce.Sleep7.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep8_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep8";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep8";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep8/edu.cmu.cs.mvelezce.Sleep8.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep9_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep9";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep9";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep9/edu.cmu.cs.mvelezce.Sleep9.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep10_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep10";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep10";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep10/edu.cmu.cs.mvelezce.Sleep10.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep11_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep11";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep11";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep11/edu.cmu.cs.mvelezce.Sleep11.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep12_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep12";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep12";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep12/edu.cmu.cs.mvelezce.Sleep12.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep13_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep13";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep13";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep13/edu.cmu.cs.mvelezce.Sleep13.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep14_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep14";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep14";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep14/edu.cmu.cs.mvelezce.Sleep14.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");
        features.add("C");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep15_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep15";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep15";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep15/edu.cmu.cs.mvelezce.Sleep15.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");
        features.add("C");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep16_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep16";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep16";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep16/edu.cmu.cs.mvelezce.Sleep16.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep17_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep17";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep17";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep17/edu.cmu.cs.mvelezce.Sleep17.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");
        features.add("C");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep18_1() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep18";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep18";
        String sdgFile = "/Users/mvelezce/Documents/Programming/Java/Projects/performancemodel-mapper/src/main/resources/joana/programs/sleep18/edu.cmu.cs.mvelezce.Sleep18.main.pdg";
        List<String> features = new java.util.ArrayList<>();
        features.add("A");
        features.add("B");
        features.add("C");

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint, sdgFile, features);
        System.out.println(pm);

    }

    @Test
    public void testSleep4() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep4";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep4";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

    @Test
    public void testSleep7() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep7";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep7";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

    @Test
    public void testSleep8() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep8";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep8";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

    @Test
    public void testSleep9() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep9";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep9";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

    @Test
    public void testSleep10() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // TODO method 1 and method 2 depend on features so instrumentation is not needed
        String programName = "sleep10";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep10";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

    @Test
    public void testSleep11() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep11";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep11";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

    @Test
    public void testSleep12() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep12";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep12";

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

    @Test
    public void testSleep13() throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String programName = "sleep13";
        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
        String entryPoint = "edu.cmu.cs.mvelezce.Sleep13";

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

//        String[] args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";

        PerformanceModel pm = JavaPipeline.buildPerformanceModel(programName, args, srcDirectory, classDirectory, entryPoint);
        System.out.println(pm);

    }

//    @Test
//    public void testInstrumentRelevantRegions1() throws Exception {
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        Set<JavaRegion> regions = new HashSet<>();
//        JavaRegion region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 31, 36);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 48, 53);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_1, 19, 20);
//        Regions.addRegion(region);
//
//        region = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_2, 19, 20);
//        Regions.addRegion(region);
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep3.FILENAME);
//
//        String program = Sleep3.FILENAME.substring(Sleep3.FILENAME.lastIndexOf(".") + 1);
//
//        // Instrument and assert
//        Instrumenter.instrument(program, Sleep3.FILENAME, programFiles, regions);
//    }
//
//    public static void compareRegionOptionsCompressionToBF(String program, boolean csv) throws NoSuchFieldException {
//        // program, regions, options, BF configurations, constraints, compressed configurations, compressed over BF
//        if(csv) {
//            System.out.print(program + ", ");
//        }
//        else {
//            System.out.println(program);
//        }
//
//        Map<JavaRegion, Set<String>> queryResult = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
//        if(csv) {
//            System.out.print(queryResult.size() + ", ");
//        }
//        else {
//            System.out.println("Lotrack total number of regions: " + queryResult.size());
//        }
//        queryResult = LotrackProcessor.filterBooleans(queryResult);
//        queryResult = LotrackProcessor.filterRegionsNoOptions(queryResult);
//
//        Set<Set<String>> optionsSet = new HashSet<>(queryResult.values());
//        Set<String> uniqueOptions = new HashSet<>();
//
//        for(Set<String> options : optionsSet) {
//            uniqueOptions.addAll(options);
//        }
//
//        JavaPipelineTest.compare(optionsSet, uniqueOptions, csv);
//    }
//
//    public static void compareOptionsCompressionToBF(String program, boolean csv) throws NoSuchFieldException {
//        // program, entries, options, BF configurations, constraints, compressed configurations, compressed over BF
//        if(csv) {
//            System.out.print(program + ", ");
//        }
//        else {
//            System.out.println(program);
//        }
//
//        List<String> fields = new ArrayList<>();
//        fields.add(LotrackProcessor.USED_TERMS);
//        fields.add(LotrackProcessor.CONSTRAINT);
//
//        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
//        List<String> queryResult = ScalaMongoDriverConnector.findProjection(program, fields);
//        ScalaMongoDriverConnector.close();
//
//        if(csv) {
//            System.out.print(queryResult.size() + ", ");
//        }
//        else {
//            System.out.println("Lotrack total number of entries: " + queryResult.size());
//        }
//
//        Set<Set<String>> optionsSet = new HashSet<>();
//
////        for(String result : queryResult) {
////            JSONObject JSONResult = new JSONObject(result);
////            Set<String> options = new HashSet<>();
////
////            if(JSONResult.has(LotrackProcessor.USED_TERMS)) {
////                for(Object string : JSONResult.getJSONArray(LotrackProcessor.USED_TERMS).toList()) {
////                    String possibleString = string.toString();
////
////                    if(possibleString.equals("true") || possibleString.equals("false")) {
////                        continue;
////                    }
////
////                    options.add(string.toString());
////                }
////            }
////            else if(JSONResult.has(LotrackProcessor.CONSTRAINT)) {
////                // Be careful that this is imprecise since the constraints can be very large and does not fit in the db field
////                String[] constraints = JSONResult.getString(LotrackProcessor.CONSTRAINT).split(" ");
////
////                for(String constraint : constraints) {
////                    constraint = constraint.replaceAll("[()^|!=]", "");
////                    if(constraint.isEmpty() || StringUtils.isNumeric(constraint)) {
////                        continue;
////                    }
////
////                    if(constraint.contains(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)) {
////                        constraint = constraint.split(LotrackProcessor.LOTRACK_UNKNOWN_CONSTRAINT_SYMBOL)[0];
////                    }
////
////                    // Because the constraint gotten from Lotrack might be too long
////                    if(constraint.contains(".")) {
////                        continue;
////                    }
////
////                    if(constraint.equals("false") || constraint.equals("true")) {
////                        continue;
////                    }
////
////                    options.add(constraint);
////                }
////            }
////            else {
////                throw new NoSuchFieldException("The query result does not have neither a " + LotrackProcessor.USED_TERMS + " or " + LotrackProcessor.CONSTRAINT + " fields");
////            }
////
////            if(!options.isEmpty()) {
////                optionsSet.add(options);
////            }
////
////        }
//
//        Set<String> uniqueOptions = new HashSet<>();
//
//        for(Set<String> options : optionsSet) {
//            uniqueOptions.addAll(options);
//        }
//
//        JavaPipelineTest.compare(optionsSet, uniqueOptions, csv);
//    }
//
//    public static void compare(Set<Set<String>> optionsSet, Set<String> uniqueOptions, boolean csv) {
//        if(csv) {
//            System.out.print(uniqueOptions.size() + ", ");
//            System.out.print((int) Math.pow(2, uniqueOptions.size()) + ", ");
//            System.out.print(optionsSet.size() + ", ");
//        }
//        else {
//            System.out.println("Number of options: " + uniqueOptions.size());
//            System.out.println("Brute force number of configurations: " + (int) Math.pow(2, uniqueOptions.size()));
//            System.out.println("Number of constraints: " + optionsSet.size());
//        }
//
//        Set<Set<String>> configurations = compression.compressConfigurations(optionsSet);
//        SimpleCompressionTest.checkConfigurationIsStatisfied(optionsSet, configurations);
//
//        if(csv) {
//            System.out.print(configurations.size() + ", ");
//            System.out.println(configurations.size()/Math.pow(2, uniqueOptions.size()));
//        }
//        else {
//            System.out.println("Compressed number of configurations: " + configurations.size());
//            System.out.println("Compressed over BF: " + configurations.size()/Math.pow(2, uniqueOptions.size()));
//        }
//    }
//
//    @Test
//    public void testCompareRegionOptionsCompressionToBF1() throws NoSuchFieldException {
//        JavaPipelineTest.compareRegionOptionsCompressionToBF("platypus", false);
//    }
//
//    @Test
//    public void testCompareRegionOptionsCompressionToBFAll1() {
////        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
////        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
////
////        for(String collection : collections) {
////            try {
////                JavaPipelineTest.compareRegionOptionsCompressionToBF(collection, false);
////            }
////            catch(NoSuchFieldException nsfe) {
////                System.out.println("This program does not have taint tracking info");
////            }
////            System.out.println();
////        }
//    }
//
//    @Test
//    public void testCompareRegionOptionsCompressionToBFALL2() {
////        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
////        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
////
////        for(String collection : collections) {
////            try {
////                JavaPipelineTest.compareRegionOptionsCompressionToBF(collection, true);
////            }
////            catch(NoSuchFieldException nsfe) {
////                System.out.println("This program does not have taint tracking info");
////            }
////        }
//    }
//
//    @Test
//    public void testCompareOptionsCompressionToBF1() throws NoSuchFieldException {
//        JavaPipelineTest.compareOptionsCompressionToBF("MGrid", false);
//    }
//
//    @Test
//    public void testCompareOptionsCompressionToBF1All1() {
////        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
////        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
////
////        for(String collection : collections) {
////            try {
////                JavaPipelineTest.compareOptionsCompressionToBF(collection, false);
////            }
////            catch(NoSuchFieldException nsfe) {
////                System.out.println("This program does not have taint tracking info");
////            }
////            System.out.println();
////        }
//    }
//
//    @Test
//    public void testCompareOptionsCompressionToBFAll2() {
////        ScalaMongoDriverConnector.connect(JavaPipeline.LOTRACK_DATABASE);
////        List<String> collections = ScalaMongoDriverConnector.getCollectionNames();
////
////        for(String collection : collections) {
////            try {
////                JavaPipelineTest.compareOptionsCompressionToBF(collection, true);
////            }
////            catch(NoSuchFieldException nsfe) {
////                System.out.println("This program does not have taint tracking info");
////            }
////        }
//    }
//
//    @Test
//    public void testBuildPerformanceModel4() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 31, 36);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.MAIN_METHOD, 48, 53);
//        Regions.addRegion(region2);
//
//        JavaRegion region3 = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_1, 19, 20);
//        Regions.addRegion(region3);
//
//        JavaRegion region4 = new JavaRegion(Sleep3.PACKAGE, Sleep3.CLASS, Sleep3.METHOD_2, 19, 20);
//        Regions.addRegion(region4);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("B");
//        regionsToOptions.put(region2, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region3, options);
//
//        options = new HashSet<>();
//        options.add("B");
//        regionsToOptions.put(region4, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep3.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep3.CLASS, Sleep3.FILENAME, programFiles, regionsToOptions);
//
//        // Compare
//        double performancemodel = 0.3;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 3.5;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 2.0;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel1() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep1.PACKAGE, Sleep1.CLASS, Sleep1.MAIN_METHOD, 23, 24);
//        Regions.addRegion(region1);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep1.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep1.CLASS, Sleep1.FILENAME, programFiles, regionsToOptions);
////        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 0.3;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 0.9;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel2() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.MAIN_METHOD, 23, 28);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep2.PACKAGE, Sleep2.CLASS, Sleep2.METHOD_1, 19, 20);
//        Regions.addRegion(region2);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region2, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep2.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep2.CLASS, Sleep2.FILENAME, programFiles, regionsToOptions);
////        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 0.3;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel6() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 23, 24);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep4.PACKAGE, Sleep4.CLASS, Sleep4.MAIN_METHOD, 29, 30);
//        Regions.addRegion(region2);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region2, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep4.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep4.CLASS, Sleep4.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 0.9;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 0.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel7() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep7.PACKAGE, Sleep7.CLASS, Sleep7.MAIN_METHOD, 23, 24);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep7.PACKAGE, Sleep7.CLASS, Sleep7.MAIN_METHOD, 29, 30);
//        Regions.addRegion(region2);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region2, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep7.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep7.CLASS, Sleep7.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 1.0;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 0.9;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel8() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.MAIN_METHOD, 31, 36);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep8.PACKAGE, Sleep8.CLASS, Sleep8.MAIN_METHOD, 41, 46);
//        Regions.addRegion(region2);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region2, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep8.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep8.CLASS, Sleep8.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 1.5;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 2.1;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel9() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.MAIN_METHOD, 31, 36);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep9.PACKAGE, Sleep9.CLASS, Sleep9.MAIN_METHOD, 41, 46);
//        Regions.addRegion(region2);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region2, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep9.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep9.CLASS, Sleep9.FILENAME, programFiles, regionsToOptions);
////        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 2.1;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.9;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel10() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.MAIN_METHOD, 31, 36);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.MAIN_METHOD, 45, 50);
//        Regions.addRegion(region2);
//
//        JavaRegion region3 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.METHOD_1, 19, 20);
//        Regions.addRegion(region3);
//
//        JavaRegion region4 = new JavaRegion(Sleep10.PACKAGE, Sleep10.CLASS, Sleep10.METHOD_2, 19, 20);
//        Regions.addRegion(region4);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        options.add("B");
//        regionsToOptions.put(region2, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region3, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        options.add("B");
//        regionsToOptions.put(region4, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep10.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep9.CLASS, Sleep10.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 0.3;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 2.1;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
//
//    @Test
//    public void testBuildPerformanceModel11() throws ClassNotFoundException, IOException, NoSuchMethodException, NoSuchFieldException, ParseException {
//        // TODO we still need to get Lotrack working
//        // Java Region
//        // Indexes were gotten by looking at output of running ClassTransformerBaseTest
//        JavaRegion region1 = new JavaRegion(Sleep11.PACKAGE, Sleep11.CLASS, Sleep11.MAIN_METHOD, 19, 24);
//        Regions.addRegion(region1);
//
//        JavaRegion region2 = new JavaRegion(Sleep11.PACKAGE, Sleep11.CLASS, Sleep11.MAIN_METHOD, 30, 35);
//        Regions.addRegion(region2);
//
//        JavaRegion region3 = new JavaRegion(Sleep11.PACKAGE, Sleep11.CLASS, Sleep11.MAIN_METHOD, 39, 57);
//        Regions.addRegion(region3);
//
//        // Regions to options
//        Map<JavaRegion, Set<String>> regionsToOptions = new HashMap<>();
//
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region1, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region2, options);
//
//        options = new HashSet<>();
//        options.add("A");
//        regionsToOptions.put(region3, options);
//        // TODO we still need to get Lotrack working
//
//        // Program files
//        List<String> programFiles = new ArrayList<>();
//        programFiles.add(Sleep11.FILENAME);
//
//        // Performance model
//        PerformanceModel performanceModel = JavaPipeline.buildPerformanceModel(Sleep11.CLASS, Sleep11.FILENAME, programFiles, regionsToOptions);
//        System.out.println(performanceModel);
//
//        // Compare
//        double performancemodel = 2.2;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//
//        performancemodel = 1.1;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performancemodel, performanceModel.evaluate(configuration), JavaPipelineTest.JavaPipelineTest.TIMING_ERROR);
//    }
}