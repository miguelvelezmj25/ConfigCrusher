package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import org.junit.Test;

public class TaintFlowAnalysisTest {
    @Test
    public void analyzeTest() throws Exception {
        String systemName = "running-example";

        // Program arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        TaintFlowAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze(args);
    }

}