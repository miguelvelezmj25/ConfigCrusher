package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.Featurewise;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.model.FeaturewisePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.Pairwise;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.model.PairwisePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.SPLat;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.model.SPLatPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.DefaultStaticAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.ConfigCrusherTimerRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.ConfigCrusherPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EvaluationTest {
    @Test
    public void compareRunningExample1() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareRunningExample2() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareRunningExample3() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareColorCounter1() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareGrep2() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareGrep3() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareColorCounter2() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareColorCounter3() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareOptimizer1() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareOptimizer2() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareOptimizer3() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void comparePrevayler1() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void comparePrevayler2() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void comparePrevayler3() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareKanzi2() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareKanzi3() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareRegions121() throws Exception {
        String programName = "regions12";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareRegions161() throws Exception {
        String programName = "regions16";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void runningExampleConfigCrusher() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance);
    }

    @Test
    public void runningExampleBruteForce() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void runningExampleFeaturewiseGenerateCSVData() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries);
////        String output = featurewise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries);
    }

    @Test
    public void runningExamplePairwiseGenerateCSVData() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries);
////        String output = pairwise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries);
    }

//    @Test
//    public void runningExampleSPLat() throws Exception {
//        String programName = "running-example";
//
//        // arguments
//        String[] args = new String[0];
//
//        Executor executor = new BruteForceExecutor(programName);
//        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
//
//        SPLat splat = new SPLat(programName);
//        Set<PerformanceEntryStatistic> pairwiseEntries = splat.getSPLatEntries(performanceEntries);
//        splat.generateCSVData(pairwiseEntries);
////        String output = splat.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder splatBuilder = new SPLatPerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = splatBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.SPLAT, performanceModel, pairwiseEntries);
//    }

    @Test
    public void colorCounterConfigCrusher() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance);
    }

    @Test
    public void colorCounterBruteForce() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void colorCounterFeaturewiseGenerateCSVData() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries);
//        String output = featurewise.execute(script);
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries);
    }

    @Test
    public void colorCounterPairwiseGenerateCSVData() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries);
//        String output = pairwise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries);
    }

    @Test
    public void optimizerBruteForce() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void kanziBruteForce() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void kanziFeaturewiseGenerateCSVData() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries);
//        String output = featurewise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries);
    }

    @Test
    public void kanziPairwiseGenerateCSVData() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries);
//        String output = pairwise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries);
    }

    @Test
    public void grepBruteForce() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void grepFeaturewiseGenerateCSVData() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries);
//        String output = featurewise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries);
    }

    @Test
    public void grepPairwiseGenerateCSVData() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries);
//        String output = pairwise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries);
    }

    @Test
    public void prevaylerBruteForce() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void prevaylerFeaturewiseGenerateCSVData() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries);
//        String output = featurewise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries);
    }

    @Test
    public void prevaylerPairwiseGenerateCSVData() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries);
//        String output = pairwise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries);
    }

    @Test
    public void optimizerConfigCrusher() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance);
    }

    @Test
    public void optimizerFeaturewiseGenerateCSVData() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries);
//        String output = featurewise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries);
    }

    @Test
    public void optimizerPairwiseGenerateCSVData() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries);
//        String output = pairwise.execute(script);
//
//        args = new String[2];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//
//        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, output);
//        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);
//
//        Evaluation eval = new Evaluation(programName);
//        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries);
    }

    @Test
    public void prevaylerConfigCrusher() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance);
    }

    @Test
    public void regions12ConfigCrusher() throws Exception {
        String programName = "regions12";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance);
    }

    @Test
    public void regions16ConfigCrusher() throws Exception {
        String programName = "regions16";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance);
    }

    @Test
    public void regions16BruteForce() throws Exception {
        String programName = "regions16";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void regions12BruteForce() throws Exception {
        String programName = "regions12";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

}