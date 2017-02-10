package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.language.Helper;
import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;
import edu.cmu.cs.mvelezce.language.ast.value.ValueInt;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;
import edu.cmu.cs.mvelezce.language.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintAnalysisTest {
    private static final String PATH = "src/edu/cmu/cs/mvelezce/language/programs/";

    @Test
    public void test1() throws Exception {
        String program = Helper.loadFile(PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void test2() throws Exception {
        String program = Helper.loadFile(PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void test3() throws Exception {
        String program = Helper.loadFile(PATH + "program3");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(cfg.getSuccessors(successor).get(0), tainted);

        successor = cfg.getSuccessors(successor).get(1);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void test4() throws Exception {
        String program = Helper.loadFile(PATH + "program4");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        instructionsToTainted.put(successor, tainted);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void test5() throws Exception {
        String program = Helper.loadFile(PATH + "program5");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        BasicBlock firstIfSuccessor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(firstIfSuccessor, tainted);

        successor = cfg.getSuccessors(successor).get(1);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        tainted = new HashSet<>();
        instructionsToTainted.put(cfg.getSuccessors(firstIfSuccessor).get(0), tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void test6() throws Exception {
        String program = Helper.loadFile(PATH + "program6");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock successor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurationsb = new HashSet<>(configurations);
        configurationsb.add(new ExpressionConstantConfiguration("D"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);

        successor = cfg.getSuccessors(successor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(successor, tainted);


        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void test7() throws Exception {
        String program = Helper.loadFile(PATH + "program7");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("x"), configurations));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("x"), configurations));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("x"), configurations));
        instructionsToTainted.put(succesor, tainted);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void test8() throws Exception {
        String program = Helper.loadFile(PATH + "program8");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<TaintAnalysis.TaintedVariable> tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurationsb = new HashSet<>();
        configurationsb.add(new ExpressionConstantConfiguration("D"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurationsb));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        Set<ExpressionConstantConfiguration> configurationsx = new HashSet<>();
        configurationsx.add(new ExpressionConstantConfiguration("C"));
        configurationsx.add(new ExpressionConstantConfiguration("D"));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurationsb));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("x"), configurationsx));
        tainted.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("x"), configurationsx));
        instructionsToTainted.put(succesor, tainted);

        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void join() throws Exception {
        Set<TaintAnalysis.TaintedVariable> set = new HashSet<>();
        Assert.assertEquals(set, TaintAnalysis.join(set, set));

        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        Assert.assertEquals(set, TaintAnalysis.join(new HashSet<>(), set));

        Assert.assertEquals(set, TaintAnalysis.join(set, set));

        Set<TaintAnalysis.TaintedVariable> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));

        Set<TaintAnalysis.TaintedVariable> set2 = new HashSet<>();
        set2.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        set2.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        Assert.assertEquals(set2, TaintAnalysis.join(set, set1));
    }

    @Test
    public void transfer() throws Exception {
        // RULE 1
        TaintAnalysis taintAnalysis = new TaintAnalysis(new CFG());
        BasicBlock basicBlock = new BasicBlock("1", new StatementSleep(new ExpressionConstantInt(1)));
        Set<TaintAnalysis.TaintedVariable> set = new HashSet<>();

        Assert.assertEquals(set, taintAnalysis.transfer(set, basicBlock));

        // RULE 2
        Set<ExpressionConstantConfiguration> configurations = new HashSet<>();
        configurations.add(new ExpressionConstantConfiguration("C"));
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantConfiguration("C")));

        Assert.assertEquals(set, taintAnalysis.transfer(new HashSet<>(), basicBlock));

        // RULE 3
        Set<TaintAnalysis.TaintedVariable> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantConfiguration("C")));

        Assert.assertEquals(set, taintAnalysis.transfer(set1, basicBlock));

        // RULE 4
        set1 = new HashSet<>();
        set1.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantInt(0)));

        Assert.assertEquals(new HashSet<>(), taintAnalysis.transfer(set1, basicBlock));

        // RULE 5
        set = new HashSet<>();
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        set1 = new HashSet<>();
        set1.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        set1.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("x"), configurations));

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementAssignment(new ExpressionVariable("x"), "=", new ExpressionConstantInt(0)));

        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));

        List<Expression> conditions = new ArrayList<>();
        conditions.add(statementIf.getCondition());
        for(Statement trueStatement : statementIf.getThenBlock().getStatements()) {
            basicBlock = new BasicBlock("1| " + trueStatement, trueStatement, conditions);
        }

        Assert.assertEquals(set1, taintAnalysis.transfer(set, basicBlock));

        // RULE 7
        set = new HashSet<>();
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("b"), configurations));
        set1 = new HashSet<>();
        set1.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("b"), "=", new ExpressionVariable("a")));

        Assert.assertEquals(set, taintAnalysis.transfer(set1, basicBlock));

        // RULE A
        set = new HashSet<>();
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=",
                new ExpressionBinary(new ExpressionConstantInt(1), "+", new ExpressionConstantConfiguration("C"))));

        Assert.assertEquals(set, taintAnalysis.transfer(new HashSet<>(), basicBlock));

        // RULE B
        set = new HashSet<>();
        set.add(new TaintAnalysis.TaintedVariable(new ExpressionVariable("a"), configurations));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=",
                new ExpressionBinary(new ExpressionVariable("b"), "+", new ExpressionConstantConfiguration("C"))));

        Assert.assertEquals(set, taintAnalysis.transfer(new HashSet<>(), basicBlock));
    }

}