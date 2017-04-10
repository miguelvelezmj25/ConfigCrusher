package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.PerformanceModel;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.sleep.ast.statement.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class SleepPipelineTest {

    public static final String PROGRAMS_PATH = "src/main/java/edu/cmu/cs/mvelezce/sleep/programs/";

    @Test
    public void testCreatePerformanceModel1() throws Exception {
        Map<Statement, Set<ExpressionConfigurationConstant>> relevantRegionsToOptions = new HashMap<>();

        Set<String> configuration = new HashSet<>();
        Map<Statement, Integer> blockToTime = new HashMap<>();

        Set<ExpressionConfigurationConstant> relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("A"));
        Statement timedStatement1 = new StatementSleep(new ExpressionConstantInt(3));
        relevantRegionsToOptions.put(timedStatement1, relevantOptions);

        int baseTime = 6;
        SleepPipeline.PerformanceEntry performanceEntry = new SleepPipeline.PerformanceEntry(configuration, blockToTime, baseTime);
        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();
        measuredPerformance.add(performanceEntry);

        relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("B"));
        Statement timedStatement2 = new StatementSleep(new ExpressionConstantInt(1));
        relevantRegionsToOptions.put(timedStatement2, relevantOptions);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        blockToTime = new HashMap<>();
        int executionTime = 3;
        baseTime += executionTime;
        blockToTime.put(timedStatement1, executionTime);

        relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("A"));
        relevantRegionsToOptions.put(timedStatement1, relevantOptions);

        executionTime = 1;
        baseTime += executionTime;
        blockToTime.put(timedStatement2, executionTime);
        performanceEntry = new SleepPipeline.PerformanceEntry(configuration, blockToTime, baseTime);
        measuredPerformance.add(performanceEntry);

        relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("B"));
        relevantRegionsToOptions.put(timedStatement2, relevantOptions);

        PerformanceModel performanceModel = SleepPipeline.createPerformanceModel(measuredPerformance, relevantRegionsToOptions);

        int performance = 6;
        configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

    @Test
    public void testGetConfigurationsInRelevantRegions1() {
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new HashMap<>();
        Set<TaintAnalysis.PossibleTaint> possibleTaints = new HashSet<>();
        Map<Statement, Set<ExpressionConfigurationConstant>> relevantRegionToOptions = new HashMap<>();

        Set<ExpressionConfigurationConstant> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ExpressionConfigurationConstant("A"));
        ExpressionVariable variable = new ExpressionVariable("a");
        TaintAnalysis.PossibleTaint possibleTaint = new TaintAnalysis.PossibleTaint(variable, taintingConfigurations);
        possibleTaints.add(possibleTaint);

        Statement statement1 = new StatementIf(new ExpressionVariable("a"), new StatementBlock(new ArrayList<>()));
        BasicBlock currentBasicBlock = new BasicBlock(statement1);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        relevantRegionToOptions.put(statement1, taintingConfigurations);

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ExpressionConfigurationConstant("B"));
        taintingConfigurations.add(new ExpressionConfigurationConstant("A"));
        variable = new ExpressionVariable("b");
        possibleTaint = new TaintAnalysis.PossibleTaint(variable, taintingConfigurations);
        possibleTaints.add(possibleTaint);

        Statement statement2 = new StatementSleep(new ExpressionVariable("b"));
        currentBasicBlock = new BasicBlock(statement2);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        relevantRegionToOptions.put(statement2, taintingConfigurations);

        Statement statement3 = new StatementSleep(new ExpressionConstantInt(3));
        currentBasicBlock = new BasicBlock(statement3);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        Assert.assertEquals(relevantRegionToOptions, SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted));
//        System.out.println(relevantRegionToOptions);
    }

    @Test
    public void testUpdateASTToTimeRelevantRegions1() {
        Set<Statement> statements = new HashSet<>();
        Statement ast = new StatementSleep(new ExpressionVariable("a"));
        statements.add(ast);

        Assert.assertNotEquals(ast, SleepPipeline.instrumentProgramToTimeRelevantRegions(ast, statements));
    }

    @Test
    public void testMeasureConfigurationPerformance1() throws Exception {
        List<Statement> statementBlock = new ArrayList<>();

        Statement timedStatement = new StatementSleep(new ExpressionConfigurationConstant("B"));
        StatementTimed statement = new StatementTimed(timedStatement);
        statementBlock.add(statement);

        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = SleepPipeline.setOfStringSetsToSetOfSleepConfigurationSets(PipelineTest.getOptionsSet("AB"));
        Set<Set<String>> configurationsToExecute = SleepPipeline.getConfigurationsToExecute(SleepPipeline.setOfSleepConfigurationSetsToSetOfStringSets(relevantOptionsSet));

        Statement ast = new StatementBlock(statementBlock);

        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();
        Set<String> configurationToExecute = new HashSet<>();
        Map<Statement, Integer> blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 0);
        SleepPipeline.PerformanceEntry performanceEntry =
                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("A");
        blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 0);
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("B");
        blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 1);
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 1);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("A");
        configurationToExecute.add("B");
        blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 1);
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 1);
        measuredPerformance.add(performanceEntry);

        Assert.assertEquals(measuredPerformance, SleepPipeline.measureConfigurationPerformance(ast, configurationsToExecute));
    }

    @Test
    public void testMeasureConfigurationPerformance2() throws Exception {
        List<Statement> statementBlock = new ArrayList<>();

        Statement timedStatement = new StatementSleep(new ExpressionConstantInt(2));
        Statement statement = new StatementTimed(timedStatement);
        statement = new StatementIf(new ExpressionConfigurationConstant("A"), statement);
        statementBlock.add(statement);

        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = SleepPipeline.setOfStringSetsToSetOfSleepConfigurationSets(PipelineTest.getOptionsSet("AB"));
        Set<Set<String>> configurationsToExecute = Pipeline.getConfigurationsToExecute(SleepPipeline.setOfSleepConfigurationSetsToSetOfStringSets(relevantOptionsSet));

        Statement ast = new StatementBlock(statementBlock);

        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();
        Set<String> configurationToExecute = new HashSet<>();
        Map<Statement, Integer> blockToTime = new HashMap<>();
        SleepPipeline.PerformanceEntry performanceEntry =
                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("A");
        blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 2);
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("B");
        blockToTime = new HashMap<>();
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("A");
        configurationToExecute.add("B");
        blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 2);
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
        measuredPerformance.add(performanceEntry);

        Assert.assertEquals(measuredPerformance, SleepPipeline.measureConfigurationPerformance(ast, configurationsToExecute));
    }

    @Test
    public void testPredictPerformanceForAllConfigurations1() {
        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));

        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();

        Statement timedStatement = new StatementSleep(new ExpressionConstantInt(2));

        Set<String> configurationToExecute = new HashSet<>();
        Map<Statement, Integer> blockToTime = new HashMap<>();
        SleepPipeline.PerformanceEntry performanceEntry =
                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("A");
        configurationToExecute.add("B");
        blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 2);
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
        measuredPerformance.add(performanceEntry);

        Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementsToOptions = new HashMap<>();
        Set<ExpressionConfigurationConstant> relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("A"));
        relevantStatementsToOptions.put(timedStatement, relevantOptions);

        Map<Set<String>, Integer> result = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        result.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        result.put(configuration, 2);

        configuration = new HashSet<>();
        configuration.add("B");
        result.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        result.put(configuration, 2);

        Assert.assertEquals(result, SleepPipeline.predictPerformanceForAllConfigurations(parameters, measuredPerformance, relevantStatementsToOptions));
    }

    @Test
    public void testPredictPerformanceForAllConfigurations2() {
        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));

        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();

        Statement timedStatement = new StatementSleep(new ExpressionConstantInt(2));
        Statement statement = new StatementIf(new ExpressionConfigurationConstant("A"), timedStatement);

        Set<String> configurationToExecute = new HashSet<>();
        Map<Statement, Integer> blockToTime = new HashMap<>();
        SleepPipeline.PerformanceEntry performanceEntry =
                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
        measuredPerformance.add(performanceEntry);

        configurationToExecute = new HashSet<>();
        configurationToExecute.add("A");
        configurationToExecute.add("B");
        blockToTime = new HashMap<>();
        blockToTime.put(timedStatement, 2);
        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
        measuredPerformance.add(performanceEntry);

        Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementsToOptions = new HashMap<>();
        Set<ExpressionConfigurationConstant> relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("A"));
        relevantStatementsToOptions.put(statement, relevantOptions);

        Map<Set<String>, Integer> result = new HashMap<>();
        Set<String> configuration = new HashSet<>();
        result.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        result.put(configuration, 2);

        configuration = new HashSet<>();
        configuration.add("B");
        result.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        result.put(configuration, 2);

        Assert.assertEquals(result, SleepPipeline.predictPerformanceForAllConfigurations(parameters, measuredPerformance, relevantStatementsToOptions));
    }

    @Test
    public void testBuildPerformanceTable1() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program1");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 3);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 4);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 6);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

//    @Test
//    public void testBuildPerformanceTable2() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program2");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 6);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 4);
//
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//
//        // TODO must calculate B
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
//    }

    @Test
    public void testBuildPerformanceTable3() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program3");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 6);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 10);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 7);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 10);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

    @Test
    public void testBuildPerformanceTable4() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program4");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 6);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 10);

        configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 9);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 7);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

    @Test
    public void testBuildPerformanceTable5() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program5");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 0);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 2);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 4);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

//    @Test
//    public void testBuildPerformanceTable6() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program6");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 5);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 6);
//
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//        parameters.add(new ExpressionConfigurationConstant("C"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
//    }

    @Test
    public void testBuildPerformanceTable7() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program7");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 0);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 2);

        configuration = new HashSet<>();
        configuration.add("C");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("D");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("D");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configurationToPerformance.put(configuration, 2);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("D");
        configurationToPerformance.put(configuration, 2);

        configuration = new HashSet<>();
        configuration.add("C");
        configuration.add("D");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("D");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configuration.add("D");
        configurationToPerformance.put(configuration, 4);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        configurationToPerformance.put(configuration, 5);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        configurationToPerformance.put(configuration, 6);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));
        parameters.add(new ExpressionConfigurationConstant("C"));
        parameters.add(new ExpressionConfigurationConstant("D"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

    @Test
    public void testBuildPerformanceTable8() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program8");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 0);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("C");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configurationToPerformance.put(configuration, 4);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configurationToPerformance.put(configuration, 4);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));
        parameters.add(new ExpressionConfigurationConstant("C"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

    @Test
    public void testBuildPerformanceTable9() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program9");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 0);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 3);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

    @Test
    public void testBuildPerformanceTable10() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program10");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 0);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 1);

        configuration = new HashSet<>();
        configuration.add("C");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 4);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configurationToPerformance.put(configuration, 3);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configurationToPerformance.put(configuration, 5);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configurationToPerformance.put(configuration, 8);

        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
        parameters.add(new ExpressionConfigurationConstant("A"));
        parameters.add(new ExpressionConfigurationConstant("B"));
        parameters.add(new ExpressionConfigurationConstant("C"));

        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
    }

    @Test
    public void testBuildPerformanceModel1() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program1");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        int performance = 3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

//    @Test
//    public void testBuildPerformanceModel2() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program2");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 6);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 4);
//
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//
//        // TODO must calculate B
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
//    }

    @Test
    public void testBuildPerformanceModel3() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program3");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        int performance = 6;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

    @Test
    public void testBuildPerformanceModel4() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program4");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        int performance = 6;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

//    @Test
//    public void testBuildPerformanceModel5() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program5");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);
//
//        int performance = 0;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 4;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 2;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//    }
//
//    @Test
//    public void testBuildPerformanceModel6() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program6");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 5);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 6);
//
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//        parameters.add(new ExpressionConfigurationConstant("C"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
//        System.out.println(configurationToPerformance);
//    }

    @Test
    public void testBuildPerformanceModel7() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program7");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        int performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

    @Test
    public void testBuildPerformanceModel8() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program8");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        int performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

//    @Test
//    public void testBuildPerformanceModel9() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program9");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);
//
//        int performance = 0;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 1;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 1;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 0;
//        configuration = new HashSet<>();
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 1;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 4;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 1;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 4;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//    }

    @Test
    public void testBuildPerformanceModel10() throws Exception {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program10");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        int performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 8;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }
}