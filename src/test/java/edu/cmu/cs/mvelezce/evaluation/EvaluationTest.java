package edu.cmu.cs.mvelezce.evaluation;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.UsesBFExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.groundtruth.execute.GroundTruthEvaluationExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.elevator.ElevatorFM;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.email.EmailFM;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.model.FamilyModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.Featurewise;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.execute.FeaturewiseExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.model.FeaturewisePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.Pairwise;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.execute.PairwiseExecutor;
import edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.model.PairwisePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.Coverage;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.SPLat;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.SPLatExecutor;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.DefaultStaticAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.colorCounter.ColorCounterAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.density.DensityAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.email.EmailAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.grep.GrepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.kanzi.KanziAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.optimizer.OptimizerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler.PrevaylerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions16.Regions16Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sort.SortAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.ConfigCrusherTimerRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.ConfigCrusherPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EvaluationTest {

    private Set<Set<String>> getConfigs(Set<PerformanceEntryStatistic> performanceEntries) {
        Set<Set<String>> configs = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            configs.add(entry.getConfiguration());
        }

        return configs;
    }

    @Test
    public void compareRunningExample0() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareRunningExample1() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareRunningExample2() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareRunningExample3() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareRunningExample4() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareColorCounter0() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareColorCounter1() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareElevator0() throws Exception {
        String programName = "elevator";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareElevator2() throws Exception {
        String programName = "elevator";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareElevator3() throws Exception {
        String programName = "elevator";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareElevator4() throws Exception {
        String programName = "elevator";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareElevator5() throws Exception {
        String programName = "elevator";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FAMILY, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareEmail0() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareEmail1() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareEmail2() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareEmail3() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareEmail5() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FAMILY, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareGrep0() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareGrep1() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareGrep2() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareGrep3() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareGrep4() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareSort0() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareSort1() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareSort2() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareSort3() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareSort4() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareDensity0() throws Exception {
        String programName = "density";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareDensity1() throws Exception {
        String programName = "density";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareDensity2() throws Exception {
        String programName = "density";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareDensity3() throws Exception {
        String programName = "density";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareColorCounter2() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareColorCounter3() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareColorCounter4() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareOptimizer0() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareOptimizer1() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareOptimizer2() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareOptimizer3() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareOptimizer4() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void comparePrevayler0() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void comparePrevayler1() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void comparePrevayler2() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void comparePrevayler3() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void comparePrevayler4() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareKanzi0() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.BRUTE_FORCE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareKanzi1() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareKanzi2() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.FEATURE_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareKanzi3() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.PAIR_WISE, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareKanzi4() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.SPLAT, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareRegions121() throws Exception {
        String programName = "regions12";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void compareRegions161() throws Exception {
        String programName = "regions16";

        Evaluation eval = new Evaluation(programName);
        eval.compareApproaches(Evaluation.CONFIG_CRUSHER, Evaluation.GROUND_TRUTH);
    }

    @Test
    public void runningExampleBruteForce() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
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

        Set<String> conf = new HashSet<>();
        conf.add("J");

        double res = performanceModel.evaluate(conf);


        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
    }

    @Test
    public void runningExampleGroundTruth() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void runningExampleGroundTruthSamplingTime() throws Exception {
        String programName = "running-example";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
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

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void runningExamplePairwiseSamplingTime() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void runningExampleSPLatSamplingTime() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            if(!splatConfigurations.contains(entry.getConfiguration())) {
                continue;
            }

            splatEntries.add(entry);
        }

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(splatEntries));
    }

    @Test
    public void runningExampleFeaturewiseGenerateCSVData() throws Exception {
        String programName = "running-example";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void runningExampleFeaturewiseModel() throws Exception {
        String programName = "running-example";

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void runningExamplePairwiseModel() throws Exception {
        String programName = "running-example";

        List<String> options = RunningExampleAdapter.getRunningExampleOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();
        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void emailGroundTruth() throws Exception {
        String programName = "email";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void emailGroundTruthSamplingTime() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
    }

    @Test
    public void emailBruteForce() throws Exception {
        String programName = "email";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void emailBruteForceSamplingTime() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void emailConfigCrusher() throws Exception {
        String programName = "email";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<Set<String>> configurations = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            configurations.add(entry.getConfiguration());
        }

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
    public void emailConfigCrusherSamplingTime() throws Exception {
        String programName = "email";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void emailFamily() throws IOException, InterruptedException {
        String programName = "email";

        // arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        FeatureModel fm = new EmailFM();
        PerformanceModelBuilder builder = new FamilyModelBuilder(programName, fm);
        PerformanceModel performanceModel = builder.createModel(args);

        List<String> options = EmailAdapter.getEmailOptions();
        Set<String> optionsSet = new HashSet<>(options);

        Set<PerformanceEntryStatistic> entries = new HashSet<>();
        PerformanceEntryStatistic entry = new PerformanceEntryStatistic(true, optionsSet);
        entries.add(entry);

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<Set<String>> configurations = new HashSet<>();

        for(PerformanceEntryStatistic e : performanceEntries) {
            configurations.add(e.getConfiguration());
        }

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.FAMILY, performanceModel, entries, configurations);
    }

    @Test
    public void emailFeaturewiseGenerateCSVData() throws Exception {
        String programName = "email";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new EmailFM();
        Featurewise featurewise = new Featurewise(programName, fm);

        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        List<String> options = EmailAdapter.getEmailOptions();
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void emailFeaturewiseModel() throws Exception {
        String programName = "email";

        FeatureModel fm = new EmailFM();
        Featurewise featurewise = new Featurewise(programName, fm);
        List<String> options = EmailAdapter.getEmailOptions();
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void emailFeaturewiseSamplingTime() throws Exception {
        String programName = "email";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new EmailFM();
        Featurewise featurewise = new Featurewise(programName, fm);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void emailPairwiseGenerateCSVData() throws Exception {
        String programName = "email";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new EmailFM();
        Pairwise pairwise = new Pairwise(programName, fm);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        List<String> options = EmailAdapter.getEmailOptions();
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void emailPairwiseModel() throws Exception {
        String programName = "email";

        FeatureModel fm = new EmailFM();
        Pairwise pairwise = new Pairwise(programName, fm);
        List<String> options = EmailAdapter.getEmailOptions();
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void emailPairwiseSamplingTime() throws Exception {
        String programName = "email";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new EmailFM();
        Pairwise pairwise = new Pairwise(programName, fm);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void elevatorGroundTruth() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void elevatorGroundTruthSamplingTime() throws Exception {
        String programName = "elevator";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
    }

    @Test
    public void elevatorBruteForce() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void elevatorBruteForceSamplingTime() throws Exception {
        String programName = "elevator";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void elevatorFamily() throws IOException, InterruptedException {
        String programName = "elevator";

        // arguments
        String[] args = new String[2];
        args[0] = "-delres";
        args[1] = "-saveres";

        FeatureModel fm = new ElevatorFM();
        PerformanceModelBuilder builder = new FamilyModelBuilder(programName, fm);
        PerformanceModel performanceModel = builder.createModel(args);

        List<String> options = ElevatorAdapter.getElevatorOptions();
        Set<String> optionsSet = new HashSet<>(options);

        Set<PerformanceEntryStatistic> entries = new HashSet<>();
        PerformanceEntryStatistic entry = new PerformanceEntryStatistic(true, optionsSet);
        entries.add(entry);

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Set<Set<String>> configurations = new HashSet<>();

        for(PerformanceEntryStatistic e : performanceEntries) {
            configurations.add(e.getConfiguration());
        }

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.FAMILY, performanceModel, entries, configurations);
    }

    @Test
    public void elevatorFeaturewiseGenerateCSVData() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new ElevatorFM();
        Featurewise featurewise = new Featurewise(programName, fm);

        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        List<String> options = ElevatorAdapter.getElevatorOptions();
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void elevatorFeaturewiseModel() throws Exception {
        String programName = "elevator";

        FeatureModel fm = new ElevatorFM();
        Featurewise featurewise = new Featurewise(programName, fm);
        List<String> options = ElevatorAdapter.getElevatorOptions();
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void elevatorPairwiseGenerateCSVData() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new ElevatorFM();
        Pairwise pairwise = new Pairwise(programName, fm);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        List<String> options = ElevatorAdapter.getElevatorOptions();
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void elevatorPairwiseModel() throws Exception {
        String programName = "elevator";

        FeatureModel fm = new ElevatorFM();
        Pairwise pairwise = new Pairwise(programName, fm);
        List<String> options = ElevatorAdapter.getElevatorOptions();
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void elevatorFeaturewiseSamplingTime() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new ElevatorFM();
        Featurewise featurewise = new Featurewise(programName, fm);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void elevatorPairwiseSamplingTime() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        FeatureModel fm = new ElevatorFM();
        Pairwise pairwise = new Pairwise(programName, fm);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void elevatorSPLat() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void elevatorSPLatSamplingTime() throws Exception {
        String programName = "elevator";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(performanceEntries));
    }

    @Test
    public void colorCounterBruteForce() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void colorCounterBruteForceSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
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

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void colorCounterGroundTruth() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void colorCounterFeaturewiseGenerateCSVData() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = ColorCounterAdapter.getColorCounterOptions();

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void colorCounterFeaturewiseModel() throws Exception {
        String programName = "pngtasticColorCounter";

        List<String> options = ColorCounterAdapter.getColorCounterOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = ColorCounterAdapter.getColorCounterOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void colorCounterPairwiseModel() throws Exception {
        String programName = "pngtasticColorCounter";

        List<String> options = ColorCounterAdapter.getColorCounterOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void colorCounterPairwiseSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void colorCounterSPLatSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            if(!splatConfigurations.contains(entry.getConfiguration())) {
                continue;
            }

            splatEntries.add(entry);
        }

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(splatEntries));
    }

    @Test
    public void colorCounterGroundTruthSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
    }

    @Test
    public void colorCounterConfigCrusherSamplingTime() throws Exception {
        String programName = "pngtasticColorCounter";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void optimizerGroundTruth() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void kanziGroundTruth() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void kanziBruteForce() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void kanziBruteForceSamplingTime() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
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

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = KanziAdapter.getKanziOptions();

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void kanziFeaturewiseModel() throws Exception {
        String programName = "kanzi";

        List<String> options = KanziAdapter.getKanziOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void kanziPairwiseGenerateCSVData() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = KanziAdapter.getKanziOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void kanziPairwiseModel() throws Exception {
        String programName = "kanzi";

        List<String> options = KanziAdapter.getKanziOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void kanziGroundTruthSamplingTime() throws Exception {
        String programName = "kanzi";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
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

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void kanziPairwiseSamplingTime() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void kanziSPLat() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void kanziSPLatSamplingTime() throws Exception {
        String programName = "kanzi";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            if(!splatConfigurations.contains(entry.getConfiguration())) {
                continue;
            }

            splatEntries.add(entry);
        }

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(splatEntries));
    }

    @Test
    public void findGroundTruth() throws Exception {
        String programName = "find";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void grepGroundTruth() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void sortGroundTruth() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void sortGroundTruthSamplingTime() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
    }

    @Test
    public void sortBruteForce() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void sortBruteForceSamplingTime() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void sortConfigCrusher() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(programName);
        instrumenter.instrument(args);
        Map<JavaRegion, Set<Set<String>>> javaRegionsToOptionSet = instrumenter.getRegionsToOptionSet();

        Analysis analysis = new DefaultStaticAnalysis();
        Map<Region, Set<Set<String>>> regionsToOptionSet = analysis.transform(javaRegionsToOptionSet);

        Executor executor = new ConfigCrusherExecutor(programName);
        Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);

        executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<Set<String>> configurations = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            configurations.add(entry.getConfiguration());
        }

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
    public void sortConfigCrusherSamplingTime() throws Exception {
        String programName = "sort";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void sortFeaturewiseGenerateCSVData() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = SortAdapter.getSortOptions();

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void sortFeaturewiseModel() throws Exception {
        String programName = "sort";

        List<String> options = SortAdapter.getSortOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void sortFeaturewiseSamplingTime() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void sortPairwiseGenerateCSVData() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = SortAdapter.getSortOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void sortPairwiseModel() throws Exception {
        String programName = "sort";

        List<String> options = SortAdapter.getSortOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void sortPairwiseSamplingTime() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void sortSPLat() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void sortSPLatSamplingTime() throws Exception {
        String programName = "sort";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            if(!splatConfigurations.contains(entry.getConfiguration())) {
                continue;
            }

            splatEntries.add(entry);
        }

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(splatEntries));
    }

    @Test
    public void grepBruteForce() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void grepBruteForceSamplingTime() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
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

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = GrepAdapter.getGrepOptions();

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void grepFeaturewiseModel() throws Exception {
        String programName = "grep";

        List<String> options = GrepAdapter.getGrepOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = GrepAdapter.getGrepOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void grepPairwiseModel() throws Exception {
        String programName = "grep";

        List<String> options = GrepAdapter.getGrepOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void grepGroundTruthSamplingTime() throws Exception {
        String programName = "grep";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
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

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void grepPairwiseSamplingTime() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void grepSPLat() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void grepSPLatSamplingTime() throws Exception {
        String programName = "grep";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            if(!splatConfigurations.contains(entry.getConfiguration())) {
                continue;
            }

            splatEntries.add(entry);
        }

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(splatEntries));
    }

    @Test
    public void prevaylerGroundTruth() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void prevaylerGroundTruthSamplingTime() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
    }

    @Test
    public void prevaylerFeaturewiseGenerateCSVData() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = PrevaylerAdapter.getPrevaylerOptions();

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void prevaylerFeaturewiseModel() throws Exception {
        String programName = "prevayler";

        List<String> options = PrevaylerAdapter.getPrevaylerOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void prevaylerFeaturewiseSamplingTime() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void prevaylerPairwiseGenerateCSVData() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = PrevaylerAdapter.getPrevaylerOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void prevaylerPairwiseModel() throws Exception {
        String programName = "prevayler";

        List<String> options = PrevaylerAdapter.getPrevaylerOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void prevaylerPairwiseSamplingTime() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void prevaylerSPLat() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        List<Coverage> coverageList = splat.readFileCoverage();

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.SPLAT, coverageList, performanceEntries);
    }

    @Test
    public void prevaylerSPLatSamplingTime() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            if(!splatConfigurations.contains(entry.getConfiguration())) {
                continue;
            }

            splatEntries.add(entry);
        }

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(splatEntries));
    }

    @Test
    public void optimizerBruteForce() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void optimizerBruteForceSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
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

        Executor executor = new SPLatExecutor(programName);
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

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = OptimizerAdapter.getOptimizerOptions();

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void optimizerFeaturewiseModel() throws Exception {
        String programName = "pngtasticOptimizer";

        List<String> options = OptimizerAdapter.getOptimizerOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = OptimizerAdapter.getOptimizerOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void optimizerPairwiseModel() throws Exception {
        String programName = "pngtasticOptimizer";

        List<String> options = OptimizerAdapter.getOptimizerOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void optimizerPairwiseSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

    @Test
    public void optimizerSPLatSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        // arguments
        String[] args = new String[0];

        Executor executor = new SPLatExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        SPLat splat = new SPLat(programName);
        Set<Set<String>> splatConfigurations = splat.getSPLatConfigurations();

        Set<PerformanceEntryStatistic> splatEntries = new HashSet<>();

        for(PerformanceEntryStatistic entry : performanceEntries) {
            if(!splatConfigurations.contains(entry.getConfiguration())) {
                continue;
            }

            splatEntries.add(entry);
        }

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(splatEntries));
    }

    @Test
    public void optimizerGroundTruthSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
    }

    @Test
    public void optimizerConfigCrusherSamplingTime() throws Exception {
        String programName = "pngtasticOptimizer";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void prevaylerBruteForce() throws Exception {
        String programName = "prevayler";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void prevaylerBruteForceSamplingTime() throws Exception {
        String programName = "prevayler";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
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
    public void regions16GroundTruth() throws Exception {
        String programName = "regions16";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void regions12GroundTruth() throws Exception {
        String programName = "regions12";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void densityGroundTruth() throws Exception {
        String programName = "density";

        // arguments
        String[] args = new String[0];

        Executor executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.GROUND_TRUTH, performanceEntries);
    }

    @Test
    public void densityGroundTruthSamplingTime() throws Exception {
        String programName = "density";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.GROUND_TRUTH));
    }

    @Test
    public void densityBruteForce() throws Exception {
        String programName = "density";

        // arguments
        String[] args = new String[0];

        Executor executor = new UsesBFExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.BRUTE_FORCE, performanceEntries);
    }

    @Test
    public void densityBruteForceSamplingTime() throws Exception {
        String programName = "density";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.BRUTE_FORCE));
    }

    @Test
    public void densityConfigCrusher() throws Exception {
        String programName = "density";

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

        args = new String[0];

        executor = new GroundTruthEvaluationExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        eval.writeConfigurationToPerformance(Evaluation.CONFIG_CRUSHER, performanceModel, measuredPerformance, configurations);
    }

    @Test
    public void densityConfigCrusherSamplingTime() throws Exception {
        String programName = "density";

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(Evaluation.CONFIG_CRUSHER));
    }

    @Test
    public void densityFeaturewiseGenerateCSVData() throws Exception {
        String programName = "density";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = DensityAdapter.getDensityOptions();

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);
        featurewise.generateCSVData(featurewiseEntries, options);
    }

    @Test
    public void densityFeaturewiseModel() throws Exception {
        String programName = "density";

        List<String> options = DensityAdapter.getDensityOptions();
        Featurewise featurewise = new Featurewise(programName);
        Map<Set<String>, Double> learnedModel = featurewise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void densityPairwiseGenerateCSVData() throws Exception {
        String programName = "density";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        List<String> options = DensityAdapter.getDensityOptions();

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);
        pairwise.generateCSVData(pairwiseEntries, options);
    }

    @Test
    public void densityPairwiseModel() throws Exception {
        String programName = "density";

        List<String> options = DensityAdapter.getDensityOptions();
        Pairwise pairwise = new Pairwise(programName);
        Map<Set<String>, Double> learnedModel = pairwise.getLearnedModel(options);

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Set<Set<String>> configurations = this.getConfigs(performanceEntries);

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
    public void densityFeaturewiseSamplingTime() throws Exception {
        String programName = "density";

        // arguments
        String[] args = new String[0];

        Executor executor = new FeaturewiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Featurewise featurewise = new Featurewise(programName);
        Set<PerformanceEntryStatistic> featurewiseEntries = featurewise.getFeaturewiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(featurewiseEntries));
    }

    @Test
    public void densityPairwiseSamplingTime() throws Exception {
        String programName = "density";

        // arguments
        String[] args = new String[0];

        Executor executor = new PairwiseExecutor(programName);
        Set<PerformanceEntryStatistic> performanceEntries = executor.execute(args);

        Pairwise pairwise = new Pairwise(programName);
        Set<PerformanceEntryStatistic> pairwiseEntries = pairwise.getPairwiseEntries(performanceEntries);

        Evaluation eval = new Evaluation(programName);
        System.out.println(eval.getTotalSamplingTime(pairwiseEntries));
    }

}
