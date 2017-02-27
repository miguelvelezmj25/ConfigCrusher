package edu.cmu.cs.mvelezce.analysis.mapper;

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
//        mapper.calculatePerformance();
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
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
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

        for(int i = 0; i < 1; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 1; i < 3; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
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

        for(int i = 0; i < 3; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
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

        for(int i = 0; i < 3; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 3; i < 6; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
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

        for(int i = 0; i < 2; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 2; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
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

        for(int i = 0; i < 2; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 2; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        taintingConfigurations.add("D");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements7() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program7");
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
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 3; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements8() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program8");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements9() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program9");
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
        taintingConfigurations.add("B");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 3; i < 5; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements10() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program10");
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
        taintingConfigurations.add("B");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements11() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program11");
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
        taintingConfigurations.add("C");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements12() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program12");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);

        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements13() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program13");
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
        taintingConfigurations.add("C");
        taintingConfigurations.add("A");
        Map<Statement, Set<String>> relevantStatementToOptions = new HashMap<>();
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        for(int i = 3; i < 6; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

    @Test
    public void getConfigurationsInRelevantStatements14() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program14");
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

        for(int i = 4; i < 6; i++) {
            successor = cfg.getSuccessors(successor).get(0);
        }

        taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("B");
        relevantStatementToOptions.put(successor.getStatement(), taintingConfigurations);

        PerformanceMapper mapper = new PerformanceMapper();
        mapper.getRelevantStatementsAndOptions(instructionsToTainted);
        Assert.assertEquals(relevantStatementToOptions, mapper.getRelevantStatementToOptions());
    }

//    @Test
//    public void pruneConfigurations1() throws Exception {
//        PerformanceMapper mapper = new PerformanceMapper();
//
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
//        PerformanceMapper mapper = new PerformanceMapper();
//
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
//        PerformanceMapper mapper = new PerformanceMapper();
//
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
//        PerformanceMapper mapper = new PerformanceMapper();
//
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