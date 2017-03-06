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
        currentBasicBlock = new BasicBlock(statement1);
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
        Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet = new HashSet<>();
        Set<ExpressionConfigurationConstant> relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("A"));
        relevantOptions.add(new ExpressionConfigurationConstant("B"));
        relevantOptionsSet.add(relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("A"));
        relevantOptions.add(new ExpressionConfigurationConstant("C"));
        relevantOptionsSet.add(relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("A"));
        relevantOptions.add(new ExpressionConfigurationConstant("D"));
        relevantOptionsSet.add(relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("B"));
        relevantOptions.add(new ExpressionConfigurationConstant("C"));
        relevantOptionsSet.add(relevantOptions);

        relevantOptions = new HashSet<>();
        relevantOptions.add(new ExpressionConfigurationConstant("C"));
        relevantOptions.add(new ExpressionConfigurationConstant("D"));
        relevantOptionsSet.add(relevantOptions);
//
//        relevantOptions = new HashSet<>();
//        relevantOptions.add(new ExpressionConfigurationConstant("A"));
//        relevantOptions.add(new ExpressionConfigurationConstant("B"));
//        relevantOptions.add(new ExpressionConfigurationConstant("D"));
//        relevantOptionsSet.add(relevantOptions);




//        Set<Set<String>> results = new HashSet<>();
//        Set<String> hold = new HashSet<>();
//        hold.add("A");
//        results.add(hold);
//
//        Set<String> check = new HashSet<>();
//        check.add("A");
//        check.add("B");
//
//        for(Set<ExpressionConfigurationConstant> relevantOption : relevantOptionsSet) {
//            Set<String> relevantOptionConvenient = new HashSet<>();
//            for(ExpressionConfigurationConstant option : relevantOption) {
//                relevantOptionConvenient.add(option.getName());
//            }
//
//            System.out.println("Checking relevant options: " + relevantOptionConvenient);
//
//            hold = new HashSet<>(relevantOptionConvenient);
//            hold.retainAll(check);
//
//            System.out.println("Value of possible config in relevant options: " + hold);
//
//            for(Set<String> entry : results) {
//                Set<String> hold1 = new HashSet<>(relevantOptionConvenient);
//                hold1.retainAll(entry);
//
//                System.out.println("Value of result in relevant options: " + hold1);
//
//                if(hold1.equals(hold)) {
//                    throw new IllegalArgumentException("BAD");
//                }
//            }
//        }



        PerformanceMapperTest.getConfigurationsToExecute(relevantOptionsSet);



//       Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<ExpressionConfigurationConstant>> statementsAndOptions = new HashSet<>()
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//        System.out.println(configurationsToExecute);
    }

    private static void getConfigurationsToExecute(Set<Set<ExpressionConfigurationConstant>> relevantOptionsSet) {
        Collection<List<Set<ExpressionConfigurationConstant>>> permutations = CollectionUtils.permutations(relevantOptionsSet);

        for(List<Set<ExpressionConfigurationConstant>> permutation : permutations) {
//            System.out.println("Permutation: " + permutation);
            Set<Set<ExpressionConfigurationConstant>> a = new HashSet<>(permutation);
            Set<Set<String>> results = PerformanceMapper.getConfigurationsToExecute(a);
//        Set<Set<String>> results = new HashSet<>();
//        Set<String> hold = new HashSet<>();
////        hold.add("A");
//        results.add(hold);
//        hold = new HashSet<>();
//        hold.add("A");
//        hold.add("B");
//        results.add(hold);
//        hold = new HashSet<>();
//        hold.add("B");
//        hold.add("C");
//        hold.add("D");
//        results.add(hold);
//        hold = new HashSet<>();
//        hold.add("A");
//        hold.add("C");
//        hold.add("D");
//        results.add(hold);

            System.out.println(results);

            for (Set<ExpressionConfigurationConstant> relevantOptions : relevantOptionsSet) {
                Set<String> relevantOptionsConvenient = new HashSet<>();

                for (ExpressionConfigurationConstant relevantOption : relevantOptions) {
                    relevantOptionsConvenient.add(relevantOption.getName());
                }

                Set<Set<String>> powerSet = Helper.getConfigurations(relevantOptions);

                for (Set<String> configuration : powerSet) {
//                    System.out.println("Want configuration: " + configuration + " from: " + relevantOptionsConvenient);
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
            return ;
        }
    }

    private static boolean matches(Set<String> result, Set<String> configuration, Set<String> relevantOptionsConvenient) {
        Set<String> hold = new HashSet<>(relevantOptionsConvenient);
        hold.retainAll(result);
        return  hold.equals(configuration);
    }

//
//    @Test
//    def test() testGetConfig("AB, AX, ABC, A")
//
//    def testGetConfig(Set<Set<ConfigOption>> o)
//    {
//        assert(sameSize(for (x in allPerMutationsOf(o)) PerformanceMapper.getConfigurationsToExecute(x).getSize))
//
//        result = PerformanceMapper.getConfigurationsToExecute(o)
//                ;
//
//        for (x in o)
//            for (config in powerSetOf(x)) {
//                foundConfig = false
//                        ;
//                for (r in results)
//                    if (r matches config of x)
//                        foundConfig=true
//            }
//     }
//     def matchesOf(r, config, setOfAllOptions)
//         for (x in config)
//             if (r containsNot x) return false
//         for (x in (setOfAllOptions - config))
//             if (r contains x) return false
//        return true

//    @Test
//    public void getConfigurationsToExecute2() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<String>> configurationsToExecute = new HashSet<>();
//        Set<String> configuration = new HashSet<>();
//        configurationsToExecute.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configurationsToExecute.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurationsToExecute.add(configuration);
//
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//
//        System.out.println(configurationsToExecute);
//    }
//
//    @Test
//    public void getConfigurationsToExecute3() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program3");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<String>> configurationsToExecute = new HashSet<>();
//        Set<ExpressionConfigurationConstant> options = new HashSet<>();
//        options.add(new ExpressionConfigurationConstant("A"));
//        options.add(new ExpressionConfigurationConstant("B"));
//        configurationsToExecute.addAll(Helper.getConfigurations(options));
//
//        // TODO can it be improved to not run all?
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//        System.out.println(configurationsToExecute);
//    }
//
//    @Test
//    public void getConfigurationsToExecute4() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program4");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<String>> configurationsToExecute = new HashSet<>();
//        configurationsToExecute.add(new HashSet<>());
//        Set<String> options = new HashSet<>();
//        options.add("A");
//        options.add("B");
//        configurationsToExecute.add(options);
//
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//        System.out.println(PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//    }
//
//    @Test
//    public void getConfigurationsToExecute5() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program5");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<String>> configurationsToExecute = new HashSet<>();
//        Set<ExpressionConfigurationConstant> options = new HashSet<>();
//        options.add(new ExpressionConfigurationConstant("A"));
//        options.add(new ExpressionConfigurationConstant("B"));
//        configurationsToExecute.addAll(Helper.getConfigurations(options));
//
//        // TODO can it be improved to not run all?
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//        System.out.println(configurationsToExecute);
//    }
//
//    @Test
//    public void getConfigurationsToExecute6() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program6");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<String>> configurationsToExecute = new HashSet<>();
//        Set<ExpressionConfigurationConstant> options = new HashSet<>();
//        options.add(new ExpressionConfigurationConstant("A"));
//        options.add(new ExpressionConfigurationConstant("B"));
//        options.add(new ExpressionConfigurationConstant("C"));
//        configurationsToExecute.addAll(Helper.getConfigurations(options));
//
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//        System.out.println(PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//    }
//
//    @Test
//    public void getConfigurationsToExecute7() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program7");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<String>> configurationsToExecute = new HashSet<>();
//
//        Set<String> options = new HashSet<>();
//        configurationsToExecute.add(options);
//
//        options = new HashSet<>();
//        options.add("D");
//        configurationsToExecute.add(options);
//
//        options = new HashSet<>();
//        options.add("C");
//        options.add("D");
//        configurationsToExecute.add(options);
//
//        options = new HashSet<>();
//        options.add("A");
//        options.add("B");
//        options.add("C");
//        configurationsToExecute.add(options);
//
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//        System.out.println(PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//    }
//
//    @Test
//    public void getConfigurationsToExecute8() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program8");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//
//        Set<Set<String>> configurationsToExecute = new HashSet<>();
//
//        Set<String> options = new HashSet<>();
//        configurationsToExecute.add(options);
//
//        options = new HashSet<>();
//        options.add("A");
//        configurationsToExecute.add(options);
//
//        options = new HashSet<>();
//        options.add("B");
//        options.add("C");
//        configurationsToExecute.add(options);
//
//        options = new HashSet<>();
//        options.add("A");
//        options.add("B");
//        options.add("C");
//        configurationsToExecute.add(options);
//
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//        Assert.assertEquals(configurationsToExecute.size(), PerformanceMapper.getConfigurationsToExecute(statementsAndOptions).size());
//        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//        System.out.println(PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
//    }
//
//

//
//    @Test
//    public void updateASTToTimeRelevantStatements2() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
//
//    @Test
//    public void updateASTToTimeRelevantStatements3() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program3");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
//
//    @Test
//    public void updateASTToTimeRelevantStatements4() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program4");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
//
//    @Test
//    public void updateASTToTimeRelevantStatements5() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program5");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
//
//    @Test
//    public void updateASTToTimeRelevantStatements6() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program6");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
//
//    @Test
//    public void updateASTToTimeRelevantStatements7() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program7");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
//
//    @Test
//    public void updateASTToTimeRelevantStatements8() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program8");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
//
//    @Test
//    public void updateASTToTimeRelevantStatements9() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program9");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        CFGBuilder builder = new CFGBuilder();
//        CFG cfg = builder.buildCFG(ast);
//
//        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
//        Map<Statement, Set<ExpressionConfigurationConstant>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
//
//        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
//    }
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