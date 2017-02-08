package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.language.Helper;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConstantConfiguration;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConstantInt;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementAssignment;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementSleep;
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

        Map<BasicBlock, Set<ExpressionVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<ExpressionVariable> tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

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

        Map<BasicBlock, Set<ExpressionVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<ExpressionVariable> tainted = new HashSet<>();
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

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

        Map<BasicBlock, Set<ExpressionVariable>> instructionsToTainted = new LinkedHashMap<>();

//        b=C
//        a=b
//        if(a) {
//            sleep(2)
//        }
//        b=0
//        sleep(3)

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<ExpressionVariable> tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(cfg.getSuccessors(succesor).get(0), tainted);

        succesor = cfg.getSuccessors(succesor).get(1);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

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

        Map<BasicBlock, Set<ExpressionVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<ExpressionVariable> tainted = new HashSet<>();
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

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

        Map<BasicBlock, Set<ExpressionVariable>> instructionsToTainted = new LinkedHashMap<>();

//        a=C
//        if(a) {
//            sleep(2)
//            a=0
//        }
//        if(!C) {
//            sleep(2)
//        }

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<ExpressionVariable> tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        BasicBlock firstIfSuccessor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(firstIfSuccessor, tainted);

        succesor = cfg.getSuccessors(succesor).get(1);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        tainted = new HashSet<>();
        instructionsToTainted.put(cfg.getSuccessors(firstIfSuccessor).get(0), tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

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

        Map<BasicBlock, Set<ExpressionVariable>> instructionsToTainted = new LinkedHashMap<>();

        BasicBlock succesor = cfg.getSuccessors(cfg.getEntry()).get(0);
        Set<ExpressionVariable> tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);

        succesor = cfg.getSuccessors(succesor).get(0);
        tainted = new HashSet<>();
        tainted.add(new ExpressionVariable("a"));
        tainted.add(new ExpressionVariable("b"));
        instructionsToTainted.put(succesor, tainted);


        TaintAnalysis taintAnalysis = new TaintAnalysis(cfg);
        Assert.assertEquals(instructionsToTainted ,taintAnalysis.analyze());
        System.out.println(taintAnalysis.getInstructionToTainted());
    }

    @Test
    public void join() throws Exception {
        Set<ExpressionVariable> set = new HashSet<>();
        Assert.assertEquals(set, TaintAnalysis.join(set, set));

        set.add(new ExpressionVariable("a"));
        Assert.assertEquals(set, TaintAnalysis.join(new HashSet<>(), set));

        Assert.assertEquals(set, TaintAnalysis.join(set, set));

        Set<ExpressionVariable> set1 = new HashSet<>();
        set1.add(new ExpressionVariable("b"));

        Set<ExpressionVariable> set2 = new HashSet<>();
        set2.add(new ExpressionVariable("a"));
        set2.add(new ExpressionVariable("b"));
        Assert.assertEquals(set2, TaintAnalysis.join(set, set1));
    }

    @Test
    public void transfer() throws Exception {
        TaintAnalysis taintAnalysis = new TaintAnalysis(new CFG());
        BasicBlock basicBlock = new BasicBlock("1", new StatementSleep(new ExpressionConstantInt(1)));
        Set<ExpressionVariable> set = new HashSet<>();
        Assert.assertEquals(set, taintAnalysis.transfer(set, basicBlock));

        set.add(new ExpressionVariable("a"));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantConfiguration("C")));
        Assert.assertEquals(set, taintAnalysis.transfer(new HashSet<>(), basicBlock));

        Set<ExpressionVariable> set1 = new HashSet<>();
        set1.add(new ExpressionVariable("b"));
        set.add(new ExpressionVariable("b"));
        basicBlock = new BasicBlock("1", new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantConfiguration("C")));
        Assert.assertEquals(set, taintAnalysis.transfer(set1, basicBlock));

    }

}