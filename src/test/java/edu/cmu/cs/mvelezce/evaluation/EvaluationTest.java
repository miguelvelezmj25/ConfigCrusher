package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.Featurewise;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.model.FeaturewisePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.Pairwise;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.model.PairwisePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.Coverage;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.SPLat;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.DefaultStaticAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.colorCounter.ColorCounterAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.grep.GrepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.kanzi.KanziAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.optimizer.OptimizerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler.PrevaylerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions16.Regions16Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.ConfigCrusherTimerRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.ConfigCrusherPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
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
    public void compareRunningExample4() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareColorCounter1() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
    }

    @Test
    public void compareGrep1() throws Exception {
        String programName = "grep";

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
    public void compareGrep4() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.BRUTE_FORCE);
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
    public void compareColorCounter4() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.BRUTE_FORCE);
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
    public void compareOptimizer4() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.BRUTE_FORCE);
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
    public void compareKanzi1() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.BRUTE_FORCE);
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
    public void compareKanzi4() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.BRUTE_FORCE);
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

        Set<String> options = new HashSet<>(RunningExampleAdapter.getRunningExampleOptions());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
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
    public void runningExampleBruteForceSamplingTime() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void runningExampleConfigCrusherSamplingTime() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void runningExampleFeaturewiseSamplingTime() throws Exception {
        String programName = "running-example";

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> featurewiseConfigurations = Featurewise.getFeaturewiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.FEATURE_WISE, featurewiseConfigurations));
    }

    @Test
    public void runningExamplePairwiseSamplingTime() throws Exception {
        String programName = "running-example";

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> pairwiseConfigurations = Pairwise.getPairwiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.PAIR_WISE, pairwiseConfigurations));
    }

    @Test
    public void runningExampleSPLatSamplingTime() throws Exception {
        String programName = "running-example";

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.SPLAT, splatConfigurations));
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
    }

    @Test
    public void runningExampleFeaturewiseModel() throws Exception {
        String programName = "running-example";

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries, configurations);
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
    }

    @Test
    public void runningExamplePairwiseModel() throws Exception {
        String programName = "running-example";

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries, configurations);
    }

    @Test
    public void runningExampleSPLat() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();
        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

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

        Set<String> options = new HashSet<>(ColorCounterAdapter.getColorCounterOptions());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
    }

    @Test
    public void colorCounterSPLat() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
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
    }

    @Test
    public void colorCounterFeaturewiseModel() throws Exception {
        String programName = "pngtasticColorCounter";

        List<String> options = ColorCounterAdapter.getColorCounterOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries, configurations);
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
    }

    @Test
    public void colorCounterPairwiseModel() throws Exception {
        String programName = "pngtasticColorCounter";

        List<String> options = ColorCounterAdapter.getColorCounterOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries, configurations);
    }

    @Test
    public void colorCounterFeaturewiseSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        List<String> options = ColorCounterAdapter.getColorCounterOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> featurewiseConfigurations = Featurewise.getFeaturewiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.FEATURE_WISE, featurewiseConfigurations));
    }

    @Test
    public void colorCounterPairwiseSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        List<String> options = ColorCounterAdapter.getColorCounterOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> pairwiseConfigurations = Pairwise.getPairwiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.PAIR_WISE, pairwiseConfigurations));
    }

    @Test
    public void colorCounterSPLatSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.SPLAT, splatConfigurations));
    }

    @Test
    public void colorCounterBruteForceSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void colorCounterConfigCrusherSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
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
    public void kanziConfigCrusher() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        Set<String> options = new HashSet<>(KanziAdapter.getKanziOptions());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
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
    public void kanziBruteForceSamplingTime() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void kanziConfigCrusherSamplingTime() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void kanziFeaturewiseSamplingTime() throws Exception {
        String programName = "kanzi";

        List<String> options = KanziAdapter.getKanziOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> featurewiseConfigurations = Featurewise.getFeaturewiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.FEATURE_WISE, featurewiseConfigurations));
    }

    @Test
    public void kanziPairwiseSamplingTime() throws Exception {
        String programName = "kanzi";

        List<String> options = KanziAdapter.getKanziOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> pairwiseConfigurations = Pairwise.getPairwiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.PAIR_WISE, pairwiseConfigurations));
    }

    @Test
    public void kanziSPLat() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void kanziSPLatSamplingTime() throws Exception {
        String programName = "kanzi";

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.SPLAT, splatConfigurations));
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
    public void grepConfigCrusher() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        Set<String> options = new HashSet<>(GrepAdapter.getGrepOptions());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
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
    }

    @Test
    public void grepFeaturewiseModel() throws Exception {
        String programName = "grep";

        List<String> options = GrepAdapter.getGrepOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries, configurations);
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
    }

    @Test
    public void grepPairwiseModel() throws Exception {
        String programName = "grep";

        List<String> options = GrepAdapter.getGrepOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries, configurations);
    }

    @Test
    public void grepBruteForceSamplingTime() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void grepConfigCrusherSamplingTime() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void grepFeaturewiseSamplingTime() throws Exception {
        String programName = "grep";

        List<String> options = GrepAdapter.getGrepOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> featurewiseConfigurations = Featurewise.getFeaturewiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.FEATURE_WISE, featurewiseConfigurations));
    }

    @Test
    public void grepPairwiseSamplingTime() throws Exception {
        String programName = "grep";

        List<String> options = GrepAdapter.getGrepOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> pairwiseConfigurations = Pairwise.getPairwiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.PAIR_WISE, pairwiseConfigurations));
    }

    @Test
    public void grepSPLat() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void grepSPLatSamplingTime() throws Exception {
        String programName = "grep";

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.SPLAT, splatConfigurations));
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

        Set<String> options = new HashSet<>(OptimizerAdapter.getOptimizerOptions());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
    }

    @Test
    public void optimizerSPLat() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
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
    }

    @Test
    public void optimizerFeaturewiseModel() throws Exception {
        String programName = "pngtasticOptimizer";

        List<String> options = OptimizerAdapter.getOptimizerOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder featurewiseBuilder = new FeaturewisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = featurewiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.FEATURE_WISE, performanceModel, featurewiseEntries, configurations);
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
    }

    @Test
    public void optimizerPairwiseModel() throws Exception {
        String programName = "pngtasticOptimizer";

        List<String> options = OptimizerAdapter.getOptimizerOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new BruteForceExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(new HashSet<>(options));

        // arguments
        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder pairwiseBuilder = new PairwisePerformanceModelBuilder(programName, learnedModel);
        PerformanceModel performanceModel = pairwiseBuilder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.PAIR_WISE, performanceModel, pairwiseEntries, configurations);
    }

    @Test
    public void optimizerFeaturewiseSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        List<String> options = OptimizerAdapter.getOptimizerOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> featurewiseConfigurations = Featurewise.getFeaturewiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.FEATURE_WISE, featurewiseConfigurations));
    }

    @Test
    public void optimizerPairwiseSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        List<String> options = OptimizerAdapter.getOptimizerOptions();
        Set<Set<String>> configurations = Helper.getConfigurations(new HashSet<>(options));
        Set<Set<String>> pairwiseConfigurations = Pairwise.getPairwiseConfigurations(configurations);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.PAIR_WISE, pairwiseConfigurations));
    }

    @Test
    public void optimizerSPLatSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.SPLAT, splatConfigurations));
    }

    @Test
    public void optimizerBruteForceSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void optimizerConfigCrusherSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
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

        Set<String> options = new HashSet<>(PrevaylerAdapter.getPrevaylerOptions());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
    }

    @Test
    public void prevaylerConfigCrusherSamplingTime() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
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

        Set<String> options = new HashSet<>(Regions12Adapter.getRegions12Options());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
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

        Set<String> options = new HashSet<>(Regions16Adapter.getRegions16Options());
        Set<Set<String>> configurations = BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

        args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        PerformanceModelBuilder builder = new ConfigCrusherPerformanceModelBuilder(programName, measuredPerformance,
                regionsToOptionSet);
        PerformanceModel performanceModel = builder.createModel(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
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
