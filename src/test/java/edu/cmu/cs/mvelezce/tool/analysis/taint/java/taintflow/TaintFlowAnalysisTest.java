package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import org.junit.Test;

public class TaintFlowAnalysisTest {
    @Test
    public void analyzeTest() throws Exception {
        String systemName = "running-example";

        TaintFlowAnalysis analysis = new TaintFlowAnalysis(systemName);
        analysis.analyze();
    }

}