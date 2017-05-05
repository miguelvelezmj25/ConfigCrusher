package edu.cmu.cs.mvelezce.tool.pipeline.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantIntExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.BlockStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.Statement;
import edu.cmu.cs.mvelezce.sleep.interpreter.TimedSleepInterpreterTest;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.region.SleepRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.TaintAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import org.junit.Assert;
import org.junit.Before;
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
//                for(Region expectedRegion : expected.getRegions()) {
//                    for(Region actualRegion : actual.getRegions()) {
//                        if(expected.getConfiguration().equals(actual.getConfiguration())) {
//                            Assert.assertEquals(actualRegion.getExecutionTime(), expectedRegion.getExecutionTime(), 0);
//                        }
//                    }
//                }
            }
        }
    }

    @Before
    public void before() {
        Regions.reset();
    }

    @Test
    public void testGetConfigurationsInRelevantRegions1() {
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new HashMap<>();

        // Possible taint
        Set<ConstantConfigurationExpression> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ConstantConfigurationExpression("A"));
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
        Map<SleepRegion, Set<ConstantConfigurationExpression>> relevantRegionToOptions = new HashMap<>();
        SleepRegion region = new SleepRegion(((IfStatement) statement1).getThenBlock());
        relevantRegionToOptions.put(region, taintingConfigurations);

        // Possible taint
        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ConstantConfigurationExpression("B"));
        taintingConfigurations.add(new ConstantConfigurationExpression("A"));
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
    public void testMeasureConfigurationPerformance1() {
////        Statement ast, Set<Set<String>> configurationsToExecute
//
//        // Statement block
//        List<Statement> statementBlock = new ArrayList<>();
//
//        // TimedStatement
//        // Sleep statement has configuration to avoid having a statement assignment
//        Statement statement = new SleepStatement(new ConstantConfigurationExpression("B"));
//        Region region = new SleepRegion(statement);
//        TimedStatement timedStatement = new TimedStatement(region.getRegionID(), statement);
//        statementBlock.add(timedStatement);
//        Regions.addRegion(region);
//
//        // Program
//        Program program = new Program(new BlockStatement(statementBlock));
//        Region programRegion = new SleepRegion(program);
//        Regions.addProgram(programRegion);
//        TimedProgram timedProgram = new TimedProgram(programRegion.getRegionID(), program);
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Set of performance entries
//        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
//
//        // Empty configuration
//        Set<String> configuration = new HashSet<>();
//        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Configuration A
//        configuration = new HashSet<>();
//        configuration.add("A");
//        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Configuration B
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Regions.getRegion(region).startTime(0);
//        Regions.getRegion(region).endTime(1);
//        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Configuration AB
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Regions.resetRegions();
//        Regions.getRegion(region).startTime(0);
//        Regions.getRegion(region).endTime(1);
//        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Assert
//        Set<PerformanceEntry> results = SleepPipeline.measureConfigurationPerformance(timedProgram, configurationsToExecute);
//        Assert.assertEquals(measuredPerformance, results);
//        SleepPipelineTest.checkExecutionTimes(measuredPerformance, results);
    }

    @Test
    public void testMeasureConfigurationPerformance2() {
//        // Statement block
//        List<Statement> statementBlock = new ArrayList<>();
//
//        // TimedStatement
//        // Sleep statement has configuration to avoid having a statement assignment
//        Statement statement = new SleepStatement(new ConstantIntExpression(2));
//        statement = new IfStatement(new ConstantConfigurationExpression("A"), statement);
//        Region region = new SleepRegion(statement);
//        Statement timedStatement = new TimedStatement(region.getRegionID(), statement);
//        statementBlock.add(timedStatement);
//        Regions.addRegion(region);
//
//        // Program
//        Program program = new Program(new BlockStatement(statementBlock));
//        Region programRegion = new SleepRegion(program);
//        Regions.addProgram(programRegion);
//        TimedProgram timedProgram = new TimedProgram(programRegion.getRegionID(), program);
//
//        // Configurations
//        Set<Set<String>> optionsSet = SimpleTest.getOptionsSet("AB");
//        Set<Set<String>> configurationsToExecute = Helper.getConfigurations(optionsSet.iterator().next());
//
//        // Set of performance entries
//        Set<PerformanceEntry> measuredPerformance = new HashSet<>();
//
//        // Empty configuration
//        Set<String> configuration = new HashSet<>();
//        PerformanceEntry performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Configuration A
//        configuration = new HashSet<>();
//        configuration.add("A");
//        Regions.getRegion(region).startTime(0);
//        Regions.getRegion(region).endTime(2);
//        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Configuration B
//        configuration = new HashSet<>();
//        configuration.add("B");
//        Regions.resetRegions();
//        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Configuration AB
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        Regions.resetRegions();
//        Regions.getRegion(region).startTime(0);
//        Regions.getRegion(region).endTime(2);
//        performanceEntry = new PerformanceEntry(configuration, Regions.getRegions(), programRegion);
//        measuredPerformance.add(performanceEntry);
//
//        // Assert
//        Set<PerformanceEntry> results = SleepPipeline.measureConfigurationPerformance(timedProgram, configurationsToExecute);
//        Assert.assertEquals(measuredPerformance, results);
//        SleepPipelineTest.checkExecutionTimes(measuredPerformance, results);
    }

    @Test
    public void testBuildPerformanceModel1() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program1");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 3;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel16() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program16");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
        System.out.println(performanceModel);

        double performance = 6;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel12() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program12");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        double performance = 2;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel13() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program13");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 2;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel14() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program14");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel15() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program15");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 2;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);


    }

    @Test
    public void testBuildPerformanceModel3() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program3");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 6;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel4() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program4");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 6;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 10;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 9;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel5() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program5");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        double performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel6() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program6");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        double performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel7() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program7");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel8() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program8");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
//        System.out.println(performanceModel);

        double performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel9() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program9");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel10() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program10");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);

        double performance = 0;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 1;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 0;
        configuration = new HashSet<>();
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 4;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 3;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 5;
        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 8;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

    @Test
    public void testBuildPerformanceModel17() throws FileNotFoundException {
        String program = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program17");
        PerformanceModel performanceModel = SleepPipeline.buildPerformanceModel(program);
        System.out.println(performanceModel);

        double performance = 2;
        Set<String> configuration = new HashSet<>();
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 7;
        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 6;
        configuration = new HashSet<>();
        configuration.add("A");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);

        performance = 2;
        configuration = new HashSet<>();
        configuration.add("B");
        Assert.assertEquals(performance, performanceModel.evaluate(configuration), 0);
    }

}