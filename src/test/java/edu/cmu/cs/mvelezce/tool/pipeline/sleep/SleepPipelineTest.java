package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.Program;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantIntExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.BlockStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.TimedSleepInterpreterTest;
import edu.cmu.cs.mvelezce.sleep.statements.TimedProgram;
import edu.cmu.cs.mvelezce.sleep.statements.TimedStatement;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.TaintAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.pipeline.PipelineTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class SleepPipelineTest {

    public static void checkExecutionTimes(Set<PerformanceEntry> expectedPerformances, Set<PerformanceEntry> actualPerformances) {
        for(PerformanceEntry expected : expectedPerformances) {
            for(PerformanceEntry actual : actualPerformances) {
                for(Region expectedRegion : expected.getRegions()) {
                    for(Region actualRegion : actual.getRegions()) {
                        if(expected.getConfiguration().equals(actual.getConfiguration())) {
                            Assert.assertEquals(actualRegion.getExecutionTime(), expectedRegion.getExecutionTime());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testGetConfigurationsInRelevantRegions1() {
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

        // Assert
        Assert.assertEquals(relevantRegionToOptions, SleepPipeline.getRelevantRegionsToOptions(instructionsToTainted));
//        System.out.println(relevantRegionToOptions);
    }

    @Test
    public void testInstrumentRelevantRegions1() {
        // Program
        Statement sleepStatement = new SleepStatement(new VariableExpression("a"));
        List<Statement> programStatements = new ArrayList<>();
        programStatements.add(sleepStatement);
        BlockStatement blockStatement = new BlockStatement(programStatements);
        Program program = new Program(blockStatement);

        // Region
        Set<SleepRegion> statements = new HashSet<>();
        statements.add(new SleepRegion(sleepStatement));
// TODO this should work
//        // Assert
//        SleepPipeline.instrumentRelevantRegions(program, statements);
//        Assert.assertNotEquals(program, SleepPipeline.instrumentRelevantRegions(program, statements));
    }

    @Test
    public void testMeasureConfigurationPerformance1() {
        Regions.reset();
//        Statement ast, Set<Set<String>> configurationsToExecute

        // Statement block
        List<Statement> statementBlock = new ArrayList<>();

        // TimedStatement
        // Sleep statement has configuration to avoid having a statement assignment
        Statement statement = new SleepStatement(new ConfigurationExpression("B"));
        TimedStatement timedStatement = new TimedStatement(statement);
        statementBlock.add(timedStatement);
        Region region = new SleepRegion(statement);
        Regions.addRegion(region);

        // Program
        Program program = new Program(new BlockStatement(statementBlock));
        Region programRegion = new SleepRegion(program);
        Regions.addProgram(programRegion);
        TimedProgram timedProgram = new TimedProgram(program);

        // Configurations
        Set<Set<String>> optionsSet = PipelineTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Set of performance entries
        Set<PerformanceEntry> measuredPerformance = new HashSet<>();

        // Empty configuration
        Set<String> configuration = new HashSet<>();
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Configuration A
        configuration = new HashSet<>();
        configuration.add("A");
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Configuration B
        configuration = new HashSet<>();
        configuration.add("B");
        region.startTime(0);
        region.endTime(1);
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Configuration AB
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Regions.resetRegions();
        region.startTime(0);
        region.endTime(1);
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Assert
        Set<PerformanceEntry> results = SleepPipeline.measureConfigurationPerformance(timedProgram, configurationsToExecute);
        Assert.assertEquals(measuredPerformance, results);
        SleepPipelineTest.checkExecutionTimes(measuredPerformance, results);
    }

    @Test
    public void testMeasureConfigurationPerformance2() {
        Regions.reset();
        // Statement block
        List<Statement> statementBlock = new ArrayList<>();

        // TimedStatement
        // Sleep statement has configuration to avoid having a statement assignment
        Statement statement = new SleepStatement(new ConstantIntExpression(2));
        statement = new IfStatement(new ConfigurationExpression("A"), statement);
        Statement timedStatement = new TimedStatement(statement);
        statementBlock.add(timedStatement);
        Region region = new SleepRegion(statement);
        Regions.reset();
        Regions.addRegion(region);

        // Program
        Program program = new Program(new BlockStatement(statementBlock));
        Region programRegion = new SleepRegion(program);
        Regions.addProgram(programRegion);
        TimedProgram timedProgram = new TimedProgram(program);

        // Configurations
        Set<Set<String>> optionsSet = PipelineTest.getOptionsSet("AB");
        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());

        // Set of performance entries
        Set<PerformanceEntry> measuredPerformance = new HashSet<>();

        // Empty configuration
        Set<String> configuration = new HashSet<>();
        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Configuration A
        configuration = new HashSet<>();
        configuration.add("A");
        region.startTime(0);
        region.endTime(2);
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Configuration B
        configuration = new HashSet<>();
        configuration.add("B");
        Regions.resetRegions();
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Configuration AB
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Regions.resetRegions();
        region.startTime(0);
        region.endTime(2);
        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
        measuredPerformance.add(performanceEntry);

        // Assert
        Set<PerformanceEntry> results = SleepPipeline.measureConfigurationPerformance(timedProgram, configurationsToExecute);
        Assert.assertEquals(measuredPerformance, results);
        SleepPipelineTest.checkExecutionTimes(measuredPerformance, results);
    }
//
//    @Test
//    public void testPredictPerformanceForAllConfigurations1() {
//        Set<ConfigurationExpression> parameters = new HashSet<>();
//        parameters.add(new ConfigurationExpression("A"));
//        parameters.add(new ConfigurationExpression("B"));
//
//        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
//
//        Statement timedStatement = new SleepStatement(new ConstantIntExpression(2));
//
//        Set<String> configurationToExecute = new HashSet<>();
//        Map<Statement, Integer> blockToTime = new HashMap<>();
//        PerformanceEntry performanceEntry =
//                new PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 2);
//        performanceEntry = new PerformanceEntry(configurationToExecute, blockToTime, 2);
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
//        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
//
//        Statement timedStatement = new SleepStatement(new ConstantIntExpression(2));
//        Statement statement = new IfStatement(new ConfigurationExpression("A"), timedStatement);
//
//        Set<String> configurationToExecute = new HashSet<>();
//        Map<Statement, Integer> blockToTime = new HashMap<>();
//        PerformanceEntry performanceEntry =
//                new PerformanceEntry(configurationToExecute, blockToTime, 0);
//        measuredPerformance.add(performanceEntry);
//
//        configurationToExecute = new HashSet<>();
//        configurationToExecute.add("A");
//        configurationToExecute.add("B");
//        blockToTime = new HashMap<>();
//        blockToTime.put(timedStatement, 2);
//        performanceEntry = new PerformanceEntry(configurationToExecute, blockToTime, 2);
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
//    public void testBuildPerformanceTable1() {
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
////    public void testBuildPerformanceTable2() {
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
//    public void testBuildPerformanceTable3() {
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
//    public void testBuildPerformanceTable4() {
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
//    public void testBuildPerformanceTable5() {
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
////    public void testBuildPerformanceTable6() {
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
//    public void testBuildPerformanceTable7() {
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
//    public void testBuildPerformanceTable8() {
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
//    public void testBuildPerformanceTable9() {
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
//    public void testBuildPerformanceTable10() {
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
    @Test
    public void testBuildPerformanceModel1() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program1");
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

    @Test
    public void testBuildPerformanceModel2() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program12");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
        System.out.println(performanceModel);

        int performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration));
    }

//    @Test
//    public void testBuildPerformanceModel3() {
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
//    public void testBuildPerformanceModel4() {
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
////    public void testBuildPerformanceModel5() {
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
////    public void testBuildPerformanceModel6() {
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
//    public void testBuildPerformanceModel7() {
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
//    public void testBuildPerformanceModel8() {
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
////    public void testBuildPerformanceModel9() {
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
//    public void testBuildPerformanceModel10() {
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