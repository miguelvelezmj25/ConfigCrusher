package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.language.Helper;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementSleep;
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
    public void getTaintingConfigurations1() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        PerformanceMapper mapper = new PerformanceMapper(ast, new HashSet<String>());
        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = mapper.getTaintAnalysis().analyze();

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");

        Assert.assertEquals(taintingConfigurations, mapper.getTaintingConfigurations(instructionsToTainted));
    }

    @Test
    public void getTaintingConfigurations2() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program6");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();

        PerformanceMapper mapper = new PerformanceMapper(ast, new HashSet<String>());
        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = mapper.getTaintAnalysis().analyze();

        Set<String> taintingConfigurations = new HashSet<>();
        taintingConfigurations.add("C");
        taintingConfigurations.add("D");

        Assert.assertEquals(taintingConfigurations, mapper.getTaintingConfigurations(instructionsToTainted));
    }

    @Test
    public void pruneConfigurations1() throws Exception {
        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");

        PerformanceMapper mapper = new PerformanceMapper(new StatementSleep(new ExpressionConstantInt(1)), parameters);

        Set<String> consider = new HashSet<>();
        consider.add("A");

        mapper.pruneConfigurations(consider);

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
        Set<String> parameters = new HashSet<>();
        parameters.add("A");
        parameters.add("B");
        parameters.add("C");
        parameters.add("D");

        PerformanceMapper mapper = new PerformanceMapper(new StatementSleep(new ExpressionConstantInt(1)), parameters);

        Set<String> consider = new HashSet<>();
        consider.add("A");
        consider.add("B");

        mapper.pruneConfigurations(consider);

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

}