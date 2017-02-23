package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.language.Helper;
import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;
import edu.cmu.cs.mvelezce.language.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintAnalysisTest {

    @Test
    public void test1() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));

    }

    @Test
    public void test2() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void test3() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program3");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(cfg.getSuccessors(successor).get(0), tainted);

        successor = cfg.getSuccessors(successor).get(1);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void test4() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program4");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void test5() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program5");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        BasicBlock firstIfSuccessor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(firstIfSuccessor, tainted);

        successor = cfg.getSuccessors(successor).get(1);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(cfg.getSuccessors(firstIfSuccessor).get(0), tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void test6() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program6");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurationsb = new HashSet<>(configurations);
        configurationsb.add(new ExpressionConfigurationConstant("D"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void test7() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program7");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("x"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("x"), configurations));
        instructionsToTainted.put(successor, tainted);

//        successor = cfg.getSuccessors(successor).get(0);
//        tainted = new HashSet<>();
//        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
//        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("x"), configurations));
//        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void test8() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program8");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurationsb = new HashSet<>();
        configurationsb.add(new ExpressionConfigurationConstant("D"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void test9() throws Exception {
        String program = Helper.loadFile(Helper.PROGRAMS_PATH + "program12");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.PossibleTaint> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("A"));
        configurations.add(new ExpressionConfigurationConstant("B"));
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("B"));
        configurations.add(new ExpressionConfigurationConstant("C"));
        tainted.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        instructionsToTainted.put(successor, tainted);

        Assert.assertEquals(instructionsToTainted, TaintAnalysis.analyze(cfg));
    }

    @Test
    public void join1() throws Exception {
        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        Assert.assertEquals(set, TaintAnalysis.join(set, set));
    }

    @Test
    public void join2() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        Assert.assertEquals(set, TaintAnalysis.join(new HashSet<>(), set));
    }

    @Test
    public void join3() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        Assert.assertEquals(set, TaintAnalysis.join(set, set));
    }

    @Test
    public void join4() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set2 = new HashSet<>();
        set2.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set2.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        Assert.assertEquals(set2, TaintAnalysis.join(set, set1));
    }

    @Test
    public void transfer1() throws Exception {
        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        BasicBlock basicBlock = new BasicBlock("1", new StatementSleep(new ExpressionConstantInt(1)));

        Assert.assertEquals(set, TaintAnalysis.transfer(set, basicBlock));
    }

    @Test
    public void transfer2() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConfigurationConstant("C")));

        Assert.assertEquals(set, TaintAnalysis.transfer(new HashSet<>(), basicBlock));
    }

    @Test
    public void transfer3() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConfigurationConstant("C")));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void transfer4() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantInt(0)));

        Assert.assertEquals(new HashSet<>(), TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void transfer5() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));
        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("x"), configurations));

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementAssignment(new ExpressionVariable("x"), "=", new ExpressionConstantInt(0)));

        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));

        List<Expression> conditions = new ArrayList<>();
        conditions.add(statementIf.getCondition());

        BasicBlock basicBlock = null;
        for (Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock("1| " + trueStatement, trueStatement, conditions);
        }

        Assert.assertEquals(set1, TaintAnalysis.transfer(set, basicBlock));
    }

    @Test
    public void transfer6() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("x"), configurations));

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementAssignment(new ExpressionVariable("x"), "=", new ExpressionConstantInt(0)));

        StatementIf statementIf = new StatementIf(new ExpressionBinary(new ExpressionVariable("a"), "+", new ExpressionVariable("b")),
                new StatementBlock(statements));

        List<Expression> conditions = new ArrayList<>();
        conditions.add(new ExpressionVariable("a"));
        conditions.add(new ExpressionVariable("b"));

        BasicBlock basicBlock = null;
        for (Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock("1| " + trueStatement, trueStatement, conditions);
        }

        Assert.assertEquals(set1, TaintAnalysis.transfer(set, basicBlock));
    }

    @Test
    public void transfer7() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("b"), "=",
                new ExpressionVariable("a")));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void transferA() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=",
                new ExpressionBinary(new ExpressionConstantInt(1), "+", new ExpressionConfigurationConstant("C"))));

        Assert.assertEquals(set, TaintAnalysis.transfer(new HashSet<>(), basicBlock));
    }

    @Test
    public void transferB() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=",
                new ExpressionBinary(new ExpressionVariable("b"), "+", new ExpressionConfigurationConstant("C"))));

        Assert.assertEquals(set, TaintAnalysis.transfer(new HashSet<>(), basicBlock));
    }

    @Test
    public void transferC() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("d"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));

        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("d"), "=",
                new ExpressionBinary(new ExpressionVariable("b"), "+", new ExpressionVariable("a"))));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void transferD() throws Exception {
        Set<ExpressionConfigurationConstant> configurations = new HashSet<>();
        configurations.add(new ExpressionConfigurationConstant("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new ExpressionVariable("b"), configurations));

        BasicBlock basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("b"), "=",
                new ExpressionBinary(new ExpressionVariable("x"), "+", new ExpressionConstantInt(2))));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

}