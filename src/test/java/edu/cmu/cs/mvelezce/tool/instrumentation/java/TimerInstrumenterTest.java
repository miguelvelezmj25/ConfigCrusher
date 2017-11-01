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
    //    @Test
//    public void testSleep30() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "sleep30";
//        String instrumentedSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
//        String instrumentedClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
//        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//
//        // Format return statements with method calls
//        Formatter.format(instrumentedSrcDirectory, instrumentedClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
//
//        // Program arguments
//    String[] args = new String[0];

    protected void compile() throws IOException, InterruptedException {
        Instrumenter compiler = new CompileInstrumenter(TimerInstrumenterTest.srcDir, TimerInstrumenterTest.classDir);
        compiler.compileFromSource();
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
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions);
//        instrumenter.instrument(args);
//    }

//    @Test
//    public void testElevator() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "elevator";
//        String instrumentedSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/";
//        String instrumentedClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/out/production/elevator/";
//        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/";
//        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/elevator/out/production/elevator/";
//
//        // Format return statements with method calls
//        Formatter.format(instrumentedSrcDirectory, instrumentedClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
//
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter.instrument(instrumentSrcDirectory, instrumentClassDirectory, decisionsToOptions);
//    }

    protected void format() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, InterruptedException {
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter compiler = new Formatter(TimerInstrumenterTest.srcDir, TimerInstrumenterTest.classDir);
        compiler.instrument(args);
    }
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter.instrument(instrumentSrcDirectory, instrumentClassDirectory, decisionsToOptions);
//    }

//    @Test
//    public void testSleep31() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
//        String programName = "sleep31";
//        String instrumentedSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
//        String instrumentedClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//        String instrumentSrcDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
//        String instrumentClassDirectory = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy/";
//
//        // Format return statements with method calls
//        Formatter.format(instrumentedSrcDirectory, instrumentedClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
//
//        // Program arguments
//        String[] args = new String[0];
//
////        String[] args = new String[1];
////        args[0] = "-saveres";
//
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter.instrument(instrumentSrcDirectory, instrumentClassDirectory, decisionsToOptions);
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
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions);
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
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions);
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
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions);
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
////        args = new String[2];
////        args[0] = "-delres";
////        args[1] = "-saveres";
//
//        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
//        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);
//
//        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(srcDirectory, classDirectory, decisionsToOptions);
//        instrumenter.instrument(args);
//    }

    @Test
    public void runningExample() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "running-example";
        String entry = "edu.cmu.cs.mvelezce.Example";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/running-example/target/classes";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void graph0() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "graph0";
        String entry = "edu.cmu.cs.mvelezce.Graph0";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";


        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void colorCounter() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "pngtasticColorCounter";
        String entry = "counter.com.googlecode.pngtastic.Run";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-counter";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-counter/out/production/pngtastic-counter";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void optimizer() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "pngtasticOptimizer";
        String entry = "optimizer.com.googlecode.pngtastic.Run";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-optimizer";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/pngtastic-optimizer/out/production/pngtastic-optimizer";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions0() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions0";
        String entry = "edu.cmu.cs.mvelezce.Regions0";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions1() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions1";
        String entry = "edu.cmu.cs.mvelezce.Regions1";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions2() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions2";
        String entry = "edu.cmu.cs.mvelezce.Regions2";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions";
        String entry = "edu.cmu.cs.mvelezce.Regions";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions8() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions8";
        String entry = "edu.cmu.cs.mvelezce.Regions8";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions9() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions9";
        String entry = "edu.cmu.cs.mvelezce.Regions9";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions10() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions10";
        String entry = "edu.cmu.cs.mvelezce.Regions10";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions11() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions11";
        String entry = "edu.cmu.cs.mvelezce.Regions11";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions12() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions12";
        String entry = "edu.cmu.cs.mvelezce.Regions12";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions13() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions13";
        String entry = "edu.cmu.cs.mvelezce.Regions13";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions14() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions14";
        String entry = "edu.cmu.cs.mvelezce.Regions14";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions15() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions15";
        String entry = "edu.cmu.cs.mvelezce.Regions15";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
//        String[] args = new String[0];

        String[] args = new String[1];
        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions16() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions16";
        String entry = "edu.cmu.cs.mvelezce.Regions16";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void regions18() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "regions18";
        String entry = "edu.cmu.cs.mvelezce.Regions18";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/dummy/out/production/dummy";

        this.compile();
//        this.format();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void prevayler() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "prevayler";
        String entry = "org.prevayler.demos.demo1.PrimeNumbers";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/prevayler";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/prevayler/target/classes";

        this.compile();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }

    @Test
    public void kanzi() throws NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException, InterruptedException {
        String programName = "kanzi";
        String entry = "kanzi.Run";
        TimerInstrumenterTest.srcDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/kanzi";
        TimerInstrumenterTest.classDir = "/Users/mvelezce/Documents/Programming/Java/Projects/performance-mapper-evaluation/instrumented/kanzi/target/classes";

        this.compile();

        // Program arguments
        String[] args = new String[0];

//        String[] args = new String[1];
//        args[0] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> decisionsToOptions = analysis.analyze(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        Instrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName, entry, TimerInstrumenterTest.classDir, decisionsToOptions);
        instrumenter.instrument(args);
    }
}