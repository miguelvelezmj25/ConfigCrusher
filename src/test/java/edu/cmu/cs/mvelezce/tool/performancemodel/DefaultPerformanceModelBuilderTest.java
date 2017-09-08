package edu.cmu.cs.mvelezce.tool.performancemodel;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow.TaintFlowAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.DefaultExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class DefaultPerformanceModelBuilderTest {

    @Test
    public void runningExample() throws Exception {
        String programName = "running-example";

        // Program arguments
        String[] args = new String[0];

        StaticAnalysis analysis = new TaintFlowAnalysis(programName);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = analysis.analyze(args);
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new DefaultExecutor(programName);
        Set<PerformanceEntry2> measuredPerformance = executor.execute(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new DefaultPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        builder.createModel(args);
    }

}