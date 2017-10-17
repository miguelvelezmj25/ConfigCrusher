package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import org.junit.Test;

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
        String systemName = "regions14";

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
        analysis.analyze(args);
    }

}