package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantIntExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.BlockStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.ast.value.IntValue;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.TaintAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class SleepPipelineTest {

    public static final String PROGRAMS_PATH = "src/main/java/edu/cmu/cs/mvelezce/sleep/programs/";

    @Test
    public void testGetConfigurationsInRelevantRegions1() {
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>>
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new HashMap<>();

        // Possible taint
        Set<ConfigurationExpression> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ConfigurationExpression("A"));
        String variableValue = "a";
        VariableExpression variable = new VariableExpression(variableValue);
        TaintAnalysis.PossibleTaint possibleTaint = new TaintAnalysis.PossibleTaint(variable, taintingConfigurations);

        Set<TaintAnalysis.PossibleTaint> possibleTaints = new HashSet<>();
        possibleTaints.add(possibleTaint);

        // Statement
        Statement statement1 = new IfStatement(new VariableExpression(variableValue), new BlockStatement(new ArrayList<>()));
        BasicBlock currentBasicBlock = new BasicBlock(statement1);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        // Region
        Map<SleepRegion, Set<ConfigurationExpression>> relevantRegionToOptions = new HashMap<>();
        SleepRegion region = new SleepRegion(statement1);
        relevantRegionToOptions.put(region, taintingConfigurations);

        // Possible taint
        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ConfigurationExpression("B"));
        taintingConfigurations.add(new ConfigurationExpression("A"));
        variableValue = "b";
        variable = new VariableExpression(variableValue);
        possibleTaint = new TaintAnalysis.PossibleTaint(variable, taintingConfigurations);
        possibleTaints.add(possibleTaint);

        // Statement
        Statement statement2 = new SleepStatement(new VariableExpression(variableValue));
        currentBasicBlock = new BasicBlock(statement2);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        // Statement
        Statement statement3 = new SleepStatement(new ConstantIntExpression(3));
        currentBasicBlock = new BasicBlock(statement3);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        // Region
        region = new SleepRegion(statement2);
        relevantRegionToOptions.put(region, taintingConfigurations);

        Assert.assertEquals(relevantRegionToOptions, SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted));
        System.out.println(relevantRegionToOptions);
    }
//
//    @Test
//    public void testUpdateASTToTimeRelevantRegions1() {
//        Set<Statement> statements = new HashSet<>();
//        Statement ast = new SleepStatement(new VariableExpression("a"));
//        statements.add(ast);
//
//        Assert.assertNotEquals(ast, SleepPipeline.instrumentProgramToTimeRelevantRegions(ast, statements));
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformance1() throws Exception {
//        List<Statement> statementBlock = new ArrayList<>();
//
//        Statement timedStatement = new SleepStatement(new ConfigurationExpression("B"));
//        StatementTimed statement = new StatementTimed(timedStatement);
//        statementBlock.add(statement);
//
//        Set<Set<ConfigurationExpression>> relevantOptionsSet = SleepPipeline.setOfStringSetsToSetOfSleepConfigurationSets(JavaPipelineTest.getOptionsSet("AB"));
//        Set<Set<String>> configurationsToExecute = SleepPipeline.getConfigurationsToExecute(SleepPipeline.setOfSleepConfigurationSetsToSetOfStringSets(relevantOptionsSet));
//
//        Statement ast = new BlockStatement(statementBlock);
//
//        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();
//        Set<String> configurationToExecute = new HashSet<>();
//        Map<Statement, Integer> blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 0);
//        SleepPipeline.PerformanceEntry performanceEntry =
//                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 0);
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 1);
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 1);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 1);
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 1);
//        measuredPerformance.add(performanceEntry);
//
//        Assert.assertEquals(measuredPerformance, SleepPipeline.measureConfigurationPerformance(ast, configurationsToExecute));
//    }
//
//    @Test
//    public void testMeasureConfigurationPerformance2() throws Exception {
//        List<Statement> statementBlock = new ArrayList<>();
//
//        Statement timedStatement = new SleepStatement(new ConstantIntExpression(2));
//        Statement statement = new StatementTimed(timedStatement);
//        statement = new IfStatement(new ConfigurationExpression("A"), statement);
//        statementBlock.add(statement);
//
//        Set<Set<ConfigurationExpression>> relevantOptionsSet = SleepPipeline.setOfStringSetsToSetOfSleepConfigurationSets(JavaPipelineTest.getOptionsSet("AB"));
//        Set<Set<String>> configurationsToExecute = JavaPipeline.getConfigurationsToExecute(SleepPipeline.setOfSleepConfigurationSetsToSetOfStringSets(relevantOptionsSet));
//
//        Statement ast = new BlockStatement(statementBlock);
//
//        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();
//        Set<String> configurationToExecute = new HashSet<>();
//        Map<Statement, Integer> blockToTime = new HashMap<>();
//        SleepPipeline.PerformanceEntry performanceEntry =
//                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 2);
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 2);
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
//        measuredPerformance.add(performanceEntry);
//
//        Assert.assertEquals(measuredPerformance, SleepPipeline.measureConfigurationPerformance(ast, configurationsToExecute));
//    }
//
//    @Test
//    public void testPredictPerformanceForAllConfigurations1() {
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//
//        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();
//
//        Statement timedStatement = new SleepStatement(new ConstantIntExpression(2));
//
//        Set<String> configurationToExecute = new HashSet<>();
//        Map<Statement, Integer> blockToTime = new HashMap<>();
//        SleepPipeline.PerformanceEntry performanceEntry =
//                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 2);
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
//        measuredPerformance.add(performanceEntry);
//
//        Map<Statement, Set<ConfigurationExpression>> relevantStatementsToOptions = new HashMap<>();
//        Set<ConfigurationExpression> relevantOptions = new HashSet<>();
//        relevantOptions.add(new ConfigurationExpression("A"));
//        relevantStatementsToOptions.put(timedStatement, relevantOptions);
//
//        Map<Set<String>, Integer> result = new HashMap<>();
//        Set<String> configuration = new HashSet<>();
//        result.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        result.put(configuration, 2);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        result.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        result.put(configuration, 2);
//
//        Assert.assertEquals(result, SleepPipeline.predictPerformanceForAllConfigurations(parameters, measuredPerformance, relevantStatementsToOptions));
//    }
//
//    @Test
//    public void testPredictPerformanceForAllConfigurations2() {
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//
//        Set<SleepPipeline.PerformanceEntry> measuredPerformance = new HashSet<>();
//
//        Statement timedStatement = new SleepStatement(new ConstantIntExpression(2));
//        Statement statement = new IfStatement(new ConfigurationExpression("A"), timedStatement);
//
//        Set<String> configurationToExecute = new HashSet<>();
//        Map<Statement, Integer> blockToTime = new HashMap<>();
//        SleepPipeline.PerformanceEntry performanceEntry =
//                new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 2);
//        performanceEntry = new SleepPipeline.PerformanceEntry(configurationToExecute, blockToTime, 2);
//        measuredPerformance.add(performanceEntry);
//
//        Map<Statement, Set<ConfigurationExpression>> relevantStatementsToOptions = new HashMap<>();
//        Set<ConfigurationExpression> relevantOptions = new HashSet<>();
//        relevantOptions.add(new ConfigurationExpression("A"));
//        relevantStatementsToOptions.put(statement, relevantOptions);
//
//        Map<Set<String>, Integer> result = new HashMap<>();
//        Set<String> configuration = new HashSet<>();
//        result.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        result.put(configuration, 2);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        result.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        result.put(configuration, 2);
//
//        Assert.assertEquals(result, SleepPipeline.predictPerformanceForAllConfigurations(parameters, measuredPerformance, relevantStatementsToOptions));
//    }
//
//    @Test
//    public void testBuildPerformanceTable1() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program1");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 3);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 4);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 6);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
////    @Test
////    public void testBuildPerformanceTable2() throws Exception {
////        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program2");
////
////        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
////        configurationToPerformance.put(new HashSet<>(), 0);
////
////        Set<String> configuration = new HashSet<>();
////        configuration.add("A");
////        configurationToPerformance.put(configuration, 6);
////
////        configuration = new HashSet<>();
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 4);
////
////        Set<ConfigurationExpression> parameters = new HashSet<>();
////        parameters.add(new ConfigurationExpression("A"));
////        parameters.add(new ConfigurationExpression("B"));
////
////        // TODO must calculate B
////        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
////    }
//
//    @Test
//    public void testBuildPerformanceTable3() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program3");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 6);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 10);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 7);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 10);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void testBuildPerformanceTable4() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program4");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 6);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 10);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 9);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 7);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void testBuildPerformanceTable5() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program5");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 2);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 4);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
////    @Test
////    public void testBuildPerformanceTable6() throws Exception {
////        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program6");
////
////        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
////        configurationToPerformance.put(new HashSet<>(), 0);
////
////        Set<String> configuration = new HashSet<>();
////        configuration.add("A");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 5);
////
////        configuration = new HashSet<>();
////        configuration.add("B");
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 1);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 6);
////
////        Set<ConfigurationExpression> parameters = new HashSet<>();
////        parameters.add(new ConfigurationExpression("A"));
////        parameters.add(new ConfigurationExpression("B"));
////        parameters.add(new ConfigurationExpression("C"));
////
////        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
////    }
//
//    @Test
//    public void testBuildPerformanceTable7() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program7");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 2);
//
//        configuration = new HashSet<>();
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 2);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 2);
//
//        configuration = new HashSet<>();
//        configuration.add("C");
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 4);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 5);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configuration.add("D");
//        configurationToPerformance.put(configuration, 6);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//        parameters.add(new ConfigurationExpression("C"));
//        parameters.add(new ConfigurationExpression("D"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void testBuildPerformanceTable8() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program8");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 4);
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
//        configurationToPerformance.put(configuration, 4);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//        parameters.add(new ConfigurationExpression("C"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void testBuildPerformanceTable9() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program9");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 3);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void testBuildPerformanceTable10() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program10");
//
//        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
//        configurationToPerformance.put(new HashSet<>(), 0);
//
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 1);
//
//        configuration = new HashSet<>();
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 0);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationToPerformance.put(configuration, 4);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 3);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 5);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configurationToPerformance.put(configuration, 8);
//
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//        parameters.add(new ConfigurationExpression("C"));
//
//        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void testBuildPerformanceModel1() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program1");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//
//        int performance = 3;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 6;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 4;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//    }
//
////    @Test
////    public void testBuildPerformanceModel2() throws Exception {
////        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program2");
////
////        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
////        configurationToPerformance.put(new HashSet<>(), 0);
////
////        Set<String> configuration = new HashSet<>();
////        configuration.add("A");
////        configurationToPerformance.put(configuration, 6);
////
////        configuration = new HashSet<>();
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 4);
////
////        Set<ConfigurationExpression> parameters = new HashSet<>();
////        parameters.add(new ConfigurationExpression("A"));
////        parameters.add(new ConfigurationExpression("B"));
////
////        // TODO must calculate B
////        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
////    }
//
//    @Test
//    public void testBuildPerformanceModel3() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program3");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
////        System.out.println(performanceModel);
//
//        int performance = 6;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 10;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 10;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 7;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//    }
//
//    @Test
//    public void testBuildPerformanceModel4() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program4");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//
//        int performance = 6;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 10;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 9;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 7;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//    }
//
////    @Test
////    public void testBuildPerformanceModel5() throws Exception {
////        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program5");
////        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
////        System.out.println(performanceModel);
////
////        int performance = 0;
////        Set<String> configuration = new HashSet<>();
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 4;
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 3;
////        configuration = new HashSet<>();
////        configuration.add("A");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 2;
////        configuration = new HashSet<>();
////        configuration.add("B");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////    }
////
////    @Test
////    public void testBuildPerformanceModel6() throws Exception {
////        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program6");
////
////        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
////        configurationToPerformance.put(new HashSet<>(), 0);
////
////        Set<String> configuration = new HashSet<>();
////        configuration.add("A");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        configurationToPerformance.put(configuration, 0);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 5);
////
////        configuration = new HashSet<>();
////        configuration.add("B");
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 1);
////
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        configuration.add("C");
////        configurationToPerformance.put(configuration, 6);
////
////        Set<ConfigurationExpression> parameters = new HashSet<>();
////        parameters.add(new ConfigurationExpression("A"));
////        parameters.add(new ConfigurationExpression("B"));
////        parameters.add(new ConfigurationExpression("C"));
////
////        Assert.assertEquals(configurationToPerformance, SleepPipeline.buildPerformanceTable(program, parameters));
////        System.out.println(configurationToPerformance);
////    }
//
//    @Test
//    public void testBuildPerformanceModel7() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program7");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
////        System.out.println(performanceModel);
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
//        performance = 2;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 0;
//        configuration = new HashSet<>();
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 0;
//        configuration = new HashSet<>();
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 1;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 1;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 2;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 2;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
//        configuration = new HashSet<>();
//        configuration.add("C");
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 4;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 5;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 6;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configuration.add("D");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//    }
//
//    @Test
//    public void testBuildPerformanceModel8() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program8");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
////        System.out.println(performanceModel);
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
//
////    @Test
////    public void testBuildPerformanceModel9() throws Exception {
////        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program9");
////        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
////        System.out.println(performanceModel);
////
////        int performance = 0;
////        Set<String> configuration = new HashSet<>();
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 1;
////        configuration = new HashSet<>();
////        configuration.add("A");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 1;
////        configuration = new HashSet<>();
////        configuration.add("B");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 0;
////        configuration = new HashSet<>();
////        configuration.add("C");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 1;
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 4;
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("C");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 1;
////        configuration = new HashSet<>();
////        configuration.add("B");
////        configuration.add("C");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////
////        performance = 4;
////        configuration = new HashSet<>();
////        configuration.add("A");
////        configuration.add("B");
////        configuration.add("C");
////        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
////    }
//
//    @Test
//    public void testBuildPerformanceModel10() throws Exception {
//        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(SleepPipelineTest.PROGRAMS_PATH + "program10");
//        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
////        System.out.println(performanceModel);
//
//        int performance = 0;
//        Set<String> configuration = new HashSet<>();
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
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
//        performance = 4;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 3;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 5;
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//
//        performance = 8;
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
//    }

}