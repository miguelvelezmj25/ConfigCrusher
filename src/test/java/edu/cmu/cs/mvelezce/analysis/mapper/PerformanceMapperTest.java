package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;
import edu.cmu.cs.mvelezce.language.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapperTest {
//    @Test
//    public void computeAll1() throws Exception {
//        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program10");
//        Lexer lexer = new Lexer(program);
//        Parser parser = new Parser(lexer);
//        Statement ast = parser.parse();
//
//        Set<String> parameters = new HashSet<>();
//        parameters.add("A");
//        parameters.add("B");
//        parameters.add("C");
//        parameters.add("D");
//
//        PerformanceMapper mapper = new PerformanceMapper(ast, parameters);
//
//        mapper.getPerformance();
//    }

    @Test
    public void getConfigurationsInRelevantStatements1() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        BasicBlock successor = cfg.getEntry();

        for(int i = 0; i < 3; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 3; i < 4; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        Assert.assertEquals(relevantStatementToOptions, PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted));
        System.out.println(relevantStatementToOptions);
    }

    @Test
    public void getConfigurationsInRelevantStatements2() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        BasicBlock successor = cfg.getEntry();

        for(int i = 0; i < 2; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 2; i < 3; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 3; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        Assert.assertEquals(relevantStatementToOptions, PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted));
        System.out.println(relevantStatementToOptions);
    }

    @Test
    public void getConfigurationsInRelevantStatements3() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program3");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        BasicBlock successor = cfg.getEntry();

        for(int i = 0; i < 4; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 4; i < 7; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        Assert.assertEquals(relevantStatementToOptions, PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted));
        System.out.println(relevantStatementToOptions);
    }

    @Test
    public void getConfigurationsInRelevantStatements4() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program4");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        BasicBlock successor = cfg.getEntry();

        for(int i = 0; i < 4; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 4; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 5; i < 6; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 6; i < 7; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        Assert.assertEquals(relevantStatementToOptions, PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted));
        System.out.println(relevantStatementToOptions);
    }

    @Test
    public void getConfigurationsInRelevantStatements5() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program5");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        BasicBlock successor = cfg.getEntry();

        for(int i = 0; i < 3; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        taintingConfigurations.add("B");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 3; i < 4; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 4; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 5; i < 7; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 7; i < 8; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        Assert.assertEquals(relevantStatementToOptions, PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted));
        System.out.println(relevantStatementToOptions);
    }

    @Test
    public void getConfigurationsInRelevantStatements6() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program6");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        BasicBlock successor = cfg.getEntry();

        for(int i = 0; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 5; i < 7; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 7; i < 9; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 9; i < 10; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("A");
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        Assert.assertEquals(relevantStatementToOptions, PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted));
        System.out.println(relevantStatementToOptions);
    }

    @Test
    public void getConfigurationsToExecute1() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Set<Set<String>> configurationsToExecute = new HashSet<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        options.add("B");
        configurationsToExecute.addAll(Helper.getConfigurations(options));

        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
        System.out.println(configurationsToExecute);
    }

    @Test
    public void getConfigurationsToExecute2() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Set<Set<String>> configurationsToExecute = new HashSet<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        options.add("B");
        configurationsToExecute.addAll(Helper.getConfigurations(options));

        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
        // TODO can do better
        System.out.println(configurationsToExecute);
    }

    @Test
    public void getConfigurationsToExecute3() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program3");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Set<Set<String>> configurationsToExecute = new HashSet<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        options.add("B");
        configurationsToExecute.addAll(Helper.getConfigurations(options));

        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
        System.out.println(configurationsToExecute);
    }

    @Test
    public void getConfigurationsToExecute4() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program4");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Set<Set<String>> configurationsToExecute = new HashSet<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        configurationsToExecute.addAll(Helper.getConfigurations(options));
        options = new HashSet<>();
        options.add("B");
        configurationsToExecute.addAll(Helper.getConfigurations(options));

        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
        // TODO have to combine A and B since they are independent
        System.out.println(configurationsToExecute);
    }

    @Test
    public void getConfigurationsToExecute5() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program5");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Set<Set<String>> configurationsToExecute = new HashSet<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        options.add("B");
        configurationsToExecute.addAll(Helper.getConfigurations(options));

        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
        System.out.println(configurationsToExecute);
    }

    @Test
    public void getConfigurationsToExecute6() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program6");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Set<Set<String>> configurationsToExecute = new HashSet<>();

        Set<String> options = new HashSet<>();
        options.add("A");
        options.add("B");
        configurationsToExecute.addAll(Helper.getConfigurations(options));

        options = new HashSet<>();
        options.add("C");
        configurationsToExecute.addAll(Helper.getConfigurations(options));

        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);
        Assert.assertEquals(configurationsToExecute, PerformanceMapper.getConfigurationsToExecute(statementsAndOptions));
        // TODO this is wrong, have to do, for now, brute force.
        System.out.println(configurationsToExecute);
    }


    @Test
    public void updateASTToTimeRelevantStatements1() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);

        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
    }

    @Test
    public void updateASTToTimeRelevantStatements2() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);

        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
    }

    @Test
    public void updateASTToTimeRelevantStatements3() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program3");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);

        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
    }

    @Test
    public void updateASTToTimeRelevantStatements4() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program4");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);

        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
    }

    @Test
    public void updateASTToTimeRelevantStatements5() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program5");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);

        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
    }

    @Test
    public void updateASTToTimeRelevantStatements6() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program6");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        Map<Statement, Set<String>> statementsAndOptions = PerformanceMapper.getRelevantStatementsToOptions(instructionsToTainted);

        Assert.assertNotEquals(ast, PerformanceMapper.instrumentProgramToTimeRelevantStatements(ast, statementsAndOptions.keySet()));
    }

    @Test
    public void calculatePerformance1() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program1");

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


        Assert.assertEquals(configurationToPerformance, PerformanceMapper.getPerformance(program));
    }

    @Test
    public void calculatePerformance2() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 0);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 6);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 4);


        Assert.assertEquals(configurationToPerformance, PerformanceMapper.getPerformance(program));
    }

    @Test
    public void calculatePerformance3() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program3");

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


        Assert.assertEquals(configurationToPerformance, PerformanceMapper.getPerformance(program));
    }

    @Test
    public void calculatePerformance4() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program4");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 6);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 7);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 7);

        Assert.assertEquals(configurationToPerformance, PerformanceMapper.getPerformance(program));
    }

    @Test
    public void calculatePerformance5() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program5");

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


        Assert.assertEquals(configurationToPerformance, PerformanceMapper.getPerformance(program));
    }

    @Test
    public void calculatePerformance6() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program6");

        Map<Set<String>, Integer> configurationToPerformance = new HashMap<>();
        configurationToPerformance.put(new HashSet<>(), 0);

        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("B");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("C");
        configurationToPerformance.put(configuration, 0);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurationToPerformance.put(configuration, 0);

        Assert.assertEquals(configurationToPerformance, PerformanceMapper.getPerformance(program));
    }



//    @Test
//    public void pruneConfigurations1() throws Exception {
///
//        Set<String> parameters = new HashSet<>();
//        parameters.add("A");
//        parameters.add("B");
//        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
//        mapper.setAllConfigurations(allConfigurations);
//
//        Set<String> consider = new HashSet<>();
//        consider.add("A");
//
//        mapper.pruneAndMapConfigurations(consider, new HashSet<>());
//
//        Set<Set<String>> configurations = new HashSet<>();
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurations.add(configuration);
//
//        Assert.assertEquals(configurations, mapper.getAllConfigurations());
//    }
//
//    @Test
//    public void pruneConfigurations2() throws Exception {
///
//        Set<String> parameters = new HashSet<>();
//        parameters.add("A");
//        parameters.add("B");
//        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
//        mapper.setAllConfigurations(allConfigurations);
//
//        Set<String> consider = new HashSet<>();
//        consider.add("A");
//
//        Set<String> configuration = new HashSet<>();
//        mapper.pruneAndMapConfigurations(consider, configuration);
//        configuration.add("A");
//        mapper.pruneAndMapConfigurations(consider, configuration);
//
//        Set<Set<String>> configurations = new HashSet<>();
//
//        Assert.assertEquals(configurations, mapper.getAllConfigurations());
//    }
//
//    @Test
//    public void pruneConfigurations3() throws Exception {
///
//        Set<String> parameters = new HashSet<>();
//        parameters.add("A");
//        parameters.add("B");
//        parameters.add("C");
//        parameters.add("D");
//        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
//        mapper.setAllConfigurations(allConfigurations);
//
//        Set<String> consider = new HashSet<>();
//        consider.add("A");
//        consider.add("B");
//
//        mapper.pruneAndMapConfigurations(consider, new HashSet<>());
//
//        Set<Set<String>> configurations = new HashSet<>();
//        Set<String> configuration = new HashSet<>();
//        configuration.add("A");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("D");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("D");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("D");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("C");
//        configuration.add("D");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("D");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("B");
//        configuration.add("C");
//        configuration.add("D");
//        configurations.add(configuration);
//
//        configuration = new HashSet<>();
//        configuration.add("A");
//        configuration.add("B");
//        configuration.add("C");
//        configuration.add("D");
//        configurations.add(configuration);
//
//        Assert.assertEquals(configurations, mapper.getAllConfigurations());
//    }
//
//    @Test
//    public void pruneConfigurations4() throws Exception {
///
//        Set<String> parameters = new HashSet<>();
//        parameters.add("A");
//        parameters.add("B");
//        parameters.add("C");
//        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
//        mapper.setAllConfigurations(allConfigurations);
//
//        Set<String> consider = new HashSet<>();
//        consider.add("A");
//        consider.add("B");
//
//        Set<String> configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
//        mapper.pruneAndMapConfigurations(consider, configuration);
//
//        configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
//        mapper.pruneAndMapConfigurations(consider, configuration);
//
//        configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
//        mapper.pruneAndMapConfigurations(consider, configuration);
//
//        configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
//        mapper.pruneAndMapConfigurations(consider, configuration);
//
//        Set<Set<String>> configurations = new HashSet<>();
//
//        Assert.assertEquals(configurations, mapper.getAllConfigurations());
//    }

}