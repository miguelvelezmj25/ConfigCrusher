package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementBlock;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementIf;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementSleep;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapperTest {

    private static Set<Set<ExpressionConfigurationConstant>> getOptionsSet(String string) {
        Set<Set<ExpressionConfigurationConstant>> result = new HashSet<>();
        String[] allOptions = string.split(",");

        for(String options : allOptions) {
            Set<ExpressionConfigurationConstant> newOption = new HashSet<>();
            options = options.trim();

            for(int i = 0; i < options.length(); i++) {
                ExpressionConfigurationConstant option = new ExpressionConfigurationConstant(options.charAt(i) + "");
                newOption.add(option);
            }

            result.add(newOption);
        }

        return result;
    }

    private static void getConfigurationsToExecute(Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet) {
        Collection<List<Set<ExpressionConfigurationConstant>>> permutations = CollectionUtils.permutations(relevantOptionsSet);

        for(List<Set<ExpressionConfigurationConstant>> permutation : permutations) {
            System.out.println("\nPermutation: " + permutation);
            Set<Set<ExpressionConfigurationConstant>> a = new HashSet<>(permutation);
            Set<Set<String>> results = PerformanceMapper.getConfigurationsToExecute(a);

            System.out.println(results);

            for(Set<ExpressionConfigurationConstant> relevantOptions : relevantOptionsSet) {
                Set<String> relevantOptionsConvenient = new HashSet<>();

                for(ExpressionConfigurationConstant relevantOption : relevantOptions) {
                    relevantOptionsConvenient.add(relevantOption.getName());
                }

                Set<Set<String>> powerSet = Helper.getConfigurations(relevantOptions);

                for (Set<String> configuration : powerSet) {
                    System.out.println("Want configuration: " + configuration + " from: " + relevantOptionsConvenient);
                    boolean hasConfiguration = false;

                    for (Set<String> result : results) {
                        if (PerformanceMapperTest.matches(result, configuration, relevantOptionsConvenient)) {
                            hasConfiguration = true;
                            break;
                        }
                    }

                    Assert.assertTrue(hasConfiguration);
                }
            }
        }
    }

    private static boolean matches(Set<String> result, Set<String> configuration, Set<String> relevantOptionsConvenient) {
        Set<String> hold = new HashSet<>(relevantOptionsConvenient);
        hold.retainAll(result);
        return hold.equals(configuration);
    }

    @Test
    public void testGetRelevantUniqueOptions1() throws Exception {
        Set<Set<ExpressionConfigurationConstant>> set = PerformanceMapperTest.getOptionsSet("AB, AC");

        Assert.assertEquals(set, PerformanceMapper.getUniqueRelevantOptions(set));
    }

    @Test
    public void testGetRelevantUniqueOptions2() throws Exception {
        Set<Set<ExpressionConfigurationConstant>> set = PerformanceMapperTest.getOptionsSet("ABC, ACD");

        Assert.assertEquals(set, PerformanceMapper.getUniqueRelevantOptions(set));
    }

    @Test
    public void testGetRelevantUniqueOptions3() throws Exception {
        Set<Set<ExpressionConfigurationConstant>> set = PerformanceMapperTest.getOptionsSet("AB, ABC");
        Set<Set<ExpressionConfigurationConstant>> result = PerformanceMapperTest.getOptionsSet("ABC");

        Assert.assertEquals(result, PerformanceMapper.getUniqueRelevantOptions(set));
    }

    @Test
    public void testGetRelevantUniqueOptions4() throws Exception {
        Set<Set<ExpressionConfigurationConstant>> set = PerformanceMapperTest.getOptionsSet("AB, ABC, BCD, BC, DEF");
        Set<Set<ExpressionConfigurationConstant>> result = PerformanceMapperTest.getOptionsSet("ABC, BCD, DEF");

        Assert.assertEquals(result, PerformanceMapper.getUniqueRelevantOptions(set));
    }

    @Test
    public void testGetConfigurationsInRelevantStatements1() {
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new HashMap<>();
        Set<TaintAnalysis.PossibleTaint> possibleTaints = new HashSet<>();
        Map<Statement, Set<ExpressionConfigurationConstant>> relevantStatementToOptions = new HashMap<>();

        Set<ExpressionConfigurationConstant> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ExpressionConfigurationConstant("A"));
        ExpressionVariable variable = new ExpressionVariable("a");
        TaintAnalysis.PossibleTaint possibleTaint = new TaintAnalysis.PossibleTaint(variable, taintingConfigurations);
        possibleTaints.add(possibleTaint);

        Statement statement1 = new StatementIf(new ExpressionVariable("a"), new StatementBlock(new ArrayList<>()));
        BasicBlock currentBasicBlock = new BasicBlock(statement1);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        relevantStatementToOptions.put(statement1, taintingConfigurations);

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add(new ExpressionConfigurationConstant("B"));
        taintingConfigurations.add(new ExpressionConfigurationConstant("A"));
        variable = new ExpressionVariable("b");
        possibleTaint = new TaintAnalysis.PossibleTaint(variable, taintingConfigurations);
        possibleTaints.add(possibleTaint);

        Statement statement2 = new StatementSleep(new ExpressionVariable("b"));
        currentBasicBlock = new BasicBlock(statement2);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        relevantStatementToOptions.put(statement2, taintingConfigurations);

        Statement statement3 = new StatementSleep(new ExpressionConstantInt(3));
        currentBasicBlock = new BasicBlock(statement3);
        instructionsToTainted.put(currentBasicBlock, possibleTaints);

        Assert.assertEquals(relevantStatementToOptions, PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted));
        System.out.println(relevantStatementToOptions);
    }

    @Test
    public void testUpdateASTToTimeRelevantStatements1() {
        Set<Statement> statements = new HashSet<>();
        Statement ast = new StatementSleep(new ExpressionVariable("a"));
        statements.add(ast);

        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statements));
    }

    @Test
    public void testGetConfigurationsToExecute1() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("AB, AC, AD, BE");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute2() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("ABC, BCD");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute3() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("AB, BCD");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute4() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("AB, CD");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute5() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("AB, CDE");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute6() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("AB, AC, BC");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute7() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("AB, AC, AD, BC, BD");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute8() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("AB, AC, AD, BC, CD");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

//    @Test // TODO ERROR
//    public void testGetConfigurationsToExecute9() {
//        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("ABC, CD, BD");
//        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
//    }

    @Test
    public void testGetConfigurationsToExecute10() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("ABC, CD");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute11() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("ABC, DEF");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute12() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }

    @Test
    public void testGetConfigurationsToExecute13() {
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = PerformanceMapperTest.getOptionsSet("ABCD, ADXY, ABDX");
        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);
    }
//
//    @Test
//    public void calculatePerformance1() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program1");
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
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void calculatePerformance2() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");
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
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void calculatePerformance3() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program3");
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
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void calculatePerformance4() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program4");
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
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void calculatePerformance5() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program5");
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
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void calculatePerformance6() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program6");
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
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void calculatePerformance7() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program7");
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
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//        parameters.add(new ExpressionConfigurationConstant("C"));
//        parameters.add(new ExpressionConfigurationConstant("D"));
//
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }
//
//    @Test
//    public void calculatePerformance8() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program8");
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
//        Set<ExpressionConfigurationConstant> parameters = new HashSet<>();
//        parameters.add(new ExpressionConfigurationConstant("A"));
//        parameters.add(new ExpressionConfigurationConstant("B"));
//        parameters.add(new ExpressionConfigurationConstant("C"));
//
//        Assert.assertEquals(configurationToPerformance, PerformanceMapper.measurePerformance(program, parameters));
//        System.out.println(configurationToPerformance);
//    }

}