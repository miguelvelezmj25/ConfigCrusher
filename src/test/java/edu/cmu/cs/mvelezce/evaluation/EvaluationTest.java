package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.StaticAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow.TaintFlowAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.DefaultExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.performancemodel.DefaultPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceEntry2;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceModelBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class EvaluationTest {
    @Test
    public void compareApproaches1() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.APPROACH, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void runningExampleApproach() throws Exception {
        String programName = "running-example";

        // arguments
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
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.APPROACH, performanceModel);
    }

    @Test
    public void runningExampleBruteForce() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntry2> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

}