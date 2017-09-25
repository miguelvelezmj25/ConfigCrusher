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