package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow.TaintFlowAnalysis;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class TimerInstrumenterTest {

    protected static String srcDir;
    protected static String classDir;

    protected void compile() {
        Instrumenter compiler = new CompileInstrumenter(TimerInstrumenterTest.srcDir, TimerInstrumenterTest.classDir);
        compiler.compileFromSource();
    }

    protected void format() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter compiler = new Formatter(TimerInstrumenterTest.srcDir, TimerInstrumenterTest.classDir);
        compiler.instrument(args);
    }

//    @Test
//    public void testElevatorSimple() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "elevator-simple";
//        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/out/production/elevator/";
//        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/";
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new TimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions.keySet());
//        instrumenter.instrument(args);
//    }

//    @Test
//    public void testElevator() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "elevator";
//        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";
//        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
//        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/";
//        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/out/production/elevator/";
//
//        // Format return statements with method calls
//        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
//
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter.instrument(instrumentSrcDirectory, instrumentClassDirectory, decisionsToOptions.keySet());
//    }

//    @Test
//    public void testSleep30() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "sleep30";
//        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
//        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
//        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
//        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//
//        // Format return statements with method calls
//        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter.instrument(instrumentSrcDirectory, instrumentClassDirectory, decisionsToOptions.keySet());
//    }

//    @Test
//    public void testSleep31() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "sleep31";
//        String originalSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
//        String originalClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
//        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
//        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//
//        // Format return statements with method calls
//        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter.instrument(instrumentSrcDirectory, instrumentClassDirectory, decisionsToOptions.keySet());
//    }

//    @Test
//    public void testGPL() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "gpl";
//        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/gpl/out/production/gpl/";
//        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/gpl/";
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new TimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions.keySet());
//        instrumenter.instrument(args);
//    }
//
//    @Test
//    public void testSleep1() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "Sleep1";
//        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new TimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions.keySet());
//        instrumenter.instrument(args);
//    }
//
//    @Test
//    public void testSleep3() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "Sleep3";
//        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new TimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions.keySet());
//        instrumenter.instrument(args);
//    }
//
//    @Test
//    public void testSleep17() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "Sleep17";
//        String classDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String srcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
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
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new TimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions.keySet());
//        instrumenter.instrument(args);
//    }

    @Test
    public void runningExample() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String programName = "running-example";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";

//        this.compile();
        this.format();

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        Instrumenter instrumenter = new TimerRegionInstrumenter(programName, TimerInstrumenterTest.classDir, decisionsToOptions.keySet());
        instrumenter.instrument(args);
    }

    @Test
    public void graph0() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String programName = "graph0";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

//        this.compile();
        this.format();

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        Instrumenter instrumenter = new TimerRegionInstrumenter(programName, TimerInstrumenterTest.classDir, decisionsToOptions.keySet());
        instrumenter.instrument(args);
    }

    @Test
    public void colorCounter() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String programName = "pngtasticColorCounter";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-counter";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-counter/out/production/pngtastic-counter";

//        this.compile();
        this.format();

        // Program arguments
//        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        Instrumenter instrumenter = new TimerRegionInstrumenter(programName, TimerInstrumenterTest.classDir, decisionsToOptions.keySet());
        instrumenter.instrument(args);
    }
}