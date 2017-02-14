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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapperTest {
    @Test
    public void computeAll1() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program10");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");

        PerformanceMapper mapper = new PerformanceMapper();

        mapper.computeAll(ast, parameters);
    }

    @Test
    public void getTaintingConfigurations1() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        TaintAnalysis taintAnalysis = new TaintAnalysis();

        PerformanceMapper mapper = new PerformanceMapper();
        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = taintAnalysis.analyze(cfg);

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");

        Assert.assertEquals(taintingConfigurations, mapper.getConfigurationsInRelevantStatements(instructionsToTainted));
    }

    @Test
    public void getTaintingConfigurations2() throws Exception {
        String program = edu.cmu.cs.mvelezce.language.Helper.loadFile(edu.cmu.cs.mvelezce.language.Helper.PROGRAMS_PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);
        TaintAnalysis taintAnalysis = new TaintAnalysis();

        PerformanceMapper mapper = new PerformanceMapper();
        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = taintAnalysis.analyze(cfg);
        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        taintingConfigurations.add("D");

        Assert.assertEquals(taintingConfigurations, mapper.getConfigurationsInRelevantStatements(instructionsToTainted));
    }

    @Test
    public void pruneConfigurations1() throws Exception {
        PerformanceMapper mapper = new PerformanceMapper();

        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");
        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
        mapper.setAllConfigurations(allConfigurations);

        Set<String> consider = new HashSet<>();
        consider.add("A");

        mapper.pruneConfigurations(consider, new HashSet<>());

        Set<Set<String>> configurations = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurations.add(configuration);

        Assert.assertEquals(configurations, mapper.getAllConfigurations());
    }

    @Test
    public void pruneConfigurations2() throws Exception {
        PerformanceMapper mapper = new PerformanceMapper();

        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");
        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
        mapper.setAllConfigurations(allConfigurations);

        Set<String> consider = new HashSet<>();
        consider.add("A");

        Set<String> configuration = new HashSet<>();
        mapper.pruneConfigurations(consider, configuration);
        configuration.add("A");
        mapper.pruneConfigurations(consider, configuration);

        Set<Set<String>> configurations = new HashSet<>();

        Assert.assertEquals(configurations, mapper.getAllConfigurations());
    }

    @Test
    public void pruneConfigurations3() throws Exception {
        PerformanceMapper mapper = new PerformanceMapper();

        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");
        parameters.add("C");
        parameters.add("D");
        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
        mapper.setAllConfigurations(allConfigurations);

        Set<String> consider = new HashSet<>();
        consider.add("A");
        consider.add("B");

        mapper.pruneConfigurations(consider, new HashSet<>());

        Set<Set<String>> configurations = new HashSet<>();
        Set<String> configuration = new HashSet<>();
        configuration.add("A");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("C");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        configurations.add(configuration);

        configuration = new HashSet<>();
        configuration.add("A");
        configuration.add("B");
        configuration.add("C");
        configuration.add("D");
        configurations.add(configuration);

        Assert.assertEquals(configurations, mapper.getAllConfigurations());
    }

    @Test
    public void pruneConfigurations4() throws Exception {
        PerformanceMapper mapper = new PerformanceMapper();

        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");
        parameters.add("C");
        Set<Set<String>> allConfigurations = edu.cmu.cs.mvelezce.analysis.Helper.getConfigurations(parameters);
        mapper.setAllConfigurations(allConfigurations);

        Set<String> consider = new HashSet<>();
        consider.add("A");
        consider.add("B");

        Set<String> configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
        mapper.pruneConfigurations(consider, configuration);

        configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
        mapper.pruneConfigurations(consider, configuration);

        configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
        mapper.pruneConfigurations(consider, configuration);

        configuration = edu.cmu.cs.mvelezce.analysis.Helper.getNextConfiguration(allConfigurations, consider);
        mapper.pruneConfigurations(consider, configuration);

        Set<Set<String>> configurations = new HashSet<>();

        Assert.assertEquals(configurations, mapper.getAllConfigurations());
    }

}