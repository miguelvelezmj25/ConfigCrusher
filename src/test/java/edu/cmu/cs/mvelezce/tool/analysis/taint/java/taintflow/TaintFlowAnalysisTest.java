package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class TaintFlowAnalysisTest {

    @Test
    public void analyzeTest1() throws Exception {
        String systemName = "running-example";

        // Program arguments
        String[] args = new String[0];

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void runningExample() throws Exception {
        String systemName = "running-example";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void colorCounter() throws Exception {
        String systemName = "pngtasticColorCounter";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void optimizer() throws Exception {
        String systemName = "pngtasticOptimizer";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions() throws Exception {
        String systemName = "regions";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions0() throws Exception {
        String systemName = "regions0";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions1() throws Exception {
        String systemName = "regions1";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions2() throws Exception {
        String systemName = "regions2";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions3() throws Exception {
        String systemName = "regions3";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions4() throws Exception {
        String systemName = "regions4";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions5() throws Exception {
        String systemName = "regions5";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions6() throws Exception {
        String systemName = "regions6";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions7() throws Exception {
        String systemName = "regions7";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions8() throws Exception {
        String systemName = "regions8";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions9() throws Exception {
        String systemName = "regions9";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions10() throws Exception {
        String systemName = "regions10";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions11() throws Exception {
        String systemName = "regions11";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions12() throws Exception {
        String systemName = "regions12";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions13() throws Exception {
        String systemName = "regions13";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions14() throws Exception {
        String systemName = "regions14";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions15() throws Exception {
        String systemName = "regions15";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions16() throws Exception {
        String systemName = "regions16";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions18() throws Exception {
        String systemName = "regions18";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions20() throws Exception {
        String systemName = "regions20";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions21() throws Exception {
        String systemName = "regions21";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions22() throws Exception {
        String systemName = "regions22";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions23() throws Exception {
        String systemName = "regions23";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions25() throws Exception {
        String systemName = "regions25";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void regions26() throws Exception {
        String systemName = "regions26";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

    @Test
    public void prevayler() throws Exception {
        String systemName = "prevayler";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        Map<JavaRegion, Set<Set<String>>> result = analysis.analyze(args);
    }

    @Test
    public void jrip() throws Exception {
        String systemName = "jrip";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        Map<JavaRegion, Set<Set<String>>> result = analysis.analyze(args);
    }

    @Test
    public void kanzi() throws Exception {
        String systemName = "kanzi";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        Map<JavaRegion, Set<Set<String>>> result = analysis.analyze(args);
    }

    @Test
    public void grep() throws Exception {
        String systemName = "grep";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        Map<JavaRegion, Set<Set<String>>> result = analysis.analyze(args);
    }

    @Test
    public void sort() throws Exception {
        String systemName = "sort";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        StaticAnalysis analysis = new TaintFlowAnalysis(systemName);
        Map<JavaRegion, Set<Set<String>>> result = analysis.analyze(args);
    }

}