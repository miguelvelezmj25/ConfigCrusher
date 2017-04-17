package edu.cmu.cs.mvelezce.tool.analysis.taint.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.Program;
import edu.cmu.cs.mvelezce.sleep.ast.expression.*;
import edu.cmu.cs.mvelezce.sleep.ast.statement.*;
import edu.cmu.cs.mvelezce.sleep.interpreter.TimedSleepInterpreterTest;
import edu.cmu.cs.mvelezce.sleep.lexer.Lexer;
import edu.cmu.cs.mvelezce.sleep.parser.Parser;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFG;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFGBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintAnalysisTest {
    @Test
    public void testJoin1() {
        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        Assert.assertEquals(set, TaintAnalysis.join(set, set));
    }

    @Test
    public void testJoin2() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        Assert.assertEquals(set, TaintAnalysis.join(new HashSet<>(), set));
    }

    @Test
    public void testJoin3() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        Assert.assertEquals(set, TaintAnalysis.join(set, set));
    }

    @Test
    public void testJoin4() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set2 = new HashSet<>();
        set2.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set2.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));
        Assert.assertEquals(set2, TaintAnalysis.join(set, set1));
    }

    @Test
    public void testJoin5() {
        Set<ConfigurationExpression> configurations1 = new HashSet<>();
        configurations1.add(new ConfigurationExpression("A"));
        configurations1.add(new ConfigurationExpression("B"));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations1));

        Set<ConfigurationExpression> configurations2 = new HashSet<>();
        configurations2.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set2 = new HashSet<>();
        set2.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations2));

        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("A"));
        configurations.add(new ConfigurationExpression("B"));
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations));

        Assert.assertEquals(set, TaintAnalysis.join(set1, set2));
    }

    @Test
    public void testJoin6() {
        Set<ConfigurationExpression> configurations1 = new HashSet<>();
        configurations1.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations1));

        Set<ConfigurationExpression> configurations2 = new HashSet<>();
        configurations2.add(new ConfigurationExpression("D"));

        Set<TaintAnalysis.PossibleTaint> set2 = new HashSet<>();
        set2.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations2));

        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));
        configurations.add(new ConfigurationExpression("D"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations));

        Assert.assertEquals(set, TaintAnalysis.join(set1, set2));
    }

    @Test
    public void testJoin7() {
        Set<ConfigurationExpression> configurations1 = new HashSet<>();
        configurations1.add(new ConfigurationExpression("A"));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("y"), configurations1));

        configurations1 = new HashSet<>();
        configurations1.add(new ConfigurationExpression("C"));

        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("z"), configurations1));

        configurations1 = new HashSet<>();
        configurations1.add(new ConfigurationExpression("B"));
        configurations1.add(new ConfigurationExpression("C"));

        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("w"), configurations1));

        configurations1 = new HashSet<>();
        configurations1.add(new ConfigurationExpression("C"));
        configurations1.add(new ConfigurationExpression("D"));

        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations1));

        Set<ConfigurationExpression> configurations2 = new HashSet<>();
        configurations2.add(new ConfigurationExpression("A"));
        configurations2.add(new ConfigurationExpression("B"));
        configurations2.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set2 = new HashSet<>();
        set2.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations2));


        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("A"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("y"), configurations));

        configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("z"), configurations));

        configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("B"));
        configurations.add(new ConfigurationExpression("C"));

        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("w"), configurations));

        configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("A"));
        configurations.add(new ConfigurationExpression("B"));
        configurations.add(new ConfigurationExpression("C"));
        configurations.add(new ConfigurationExpression("D"));

        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations));

        Assert.assertEquals(set, TaintAnalysis.join(set1, set2));
    }

    @Test
    public void testTransfer1() {
        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        BasicBlock basicBlock = new BasicBlock(new SleepStatement(new ConstantIntExpression(1)));

        Assert.assertEquals(set, TaintAnalysis.transfer(set, basicBlock));
    }

    @Test
    public void testTransfer2() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("a"), new ConfigurationExpression("C")));

        Assert.assertEquals(set, TaintAnalysis.transfer(new HashSet<>(), basicBlock));
    }

    @Test
    public void testTransfer3() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));
        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("a"), new ConfigurationExpression("C")));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void testTransfer4() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("a"), new ConstantIntExpression(0)));

        Assert.assertEquals(new HashSet<>(), TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void testTransfer5() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));
        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations));

        List<Statement> statements = new LinkedList<>();
        statements.add(new AssignmentStatement(new VariableExpression("x"), new ConstantIntExpression(0)));

        IfStatement statementIf = new IfStatement(new VariableExpression("a"), new BlockStatement(statements));

        List<Expression> conditions = new ArrayList<>();
        conditions.add(statementIf.getCondition());

        BasicBlock basicBlock = null;
        for (Statement trueStatement : ((BlockStatement) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock("1| " + trueStatement, trueStatement, conditions);
        }

        Assert.assertEquals(set1, TaintAnalysis.transfer(set, basicBlock));
    }

    @Test
    public void testTransfer6() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("x"), configurations));

        List<Statement> statements = new LinkedList<>();
        statements.add(new AssignmentStatement(new VariableExpression("x"), new ConstantIntExpression(0)));

        IfStatement statementIf = new IfStatement(new BinaryExpression(new VariableExpression("a"), "+", new VariableExpression("b")),
                new BlockStatement(statements));

        List<Expression> conditions = new ArrayList<>();
        conditions.add(new VariableExpression("a"));
        conditions.add(new VariableExpression("b"));

        BasicBlock basicBlock = null;
        for (Statement trueStatement : ((BlockStatement) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock("1| " + trueStatement, trueStatement, conditions);
        }

        Assert.assertEquals(set1, TaintAnalysis.transfer(set, basicBlock));
    }

    @Test
    public void testTransfer7() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("b"), new VariableExpression("a")));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void testTransfer8() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("a"),
                new BinaryExpression(new ConstantIntExpression(1), "+", new ConfigurationExpression("C"))));

        Assert.assertEquals(set, TaintAnalysis.transfer(new HashSet<>(), basicBlock));
    }

    @Test
    public void testTransfer9() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("a"),
                new BinaryExpression(new VariableExpression("b"), "+", new ConfigurationExpression("C"))));

        Assert.assertEquals(set, TaintAnalysis.transfer(new HashSet<>(), basicBlock));
    }

    @Test
    public void testTransfer10() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("d"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));

        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("d"),
                new BinaryExpression(new VariableExpression("b"), "+", new VariableExpression("a"))));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void testTransfer11() {
        Set<ConfigurationExpression> configurations = new HashSet<>();
        configurations.add(new ConfigurationExpression("C"));

        Set<TaintAnalysis.PossibleTaint> set = new HashSet<>();
        set.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));

        Set<TaintAnalysis.PossibleTaint> set1 = new HashSet<>();
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("a"), configurations));
        set1.add(new TaintAnalysis.PossibleTaint(new VariableExpression("b"), configurations));

        BasicBlock basicBlock = new BasicBlock(new AssignmentStatement(new VariableExpression("b"),
                new BinaryExpression(new VariableExpression("x"), "+", new ConstantIntExpression(2))));

        Assert.assertEquals(set, TaintAnalysis.transfer(set1, basicBlock));
    }

    @Test
    public void testAnalyze() throws FileNotFoundException {
        String programFile = edu.cmu.cs.mvelezce.sleep.Helper.loadFile(TimedSleepInterpreterTest.PROGRAMS_PATH + "program12");

        Lexer lexer = new Lexer(programFile);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(program);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = TaintAnalysis.analyze(cfg);
        System.out.println(instructionsToTainted);
    }

}