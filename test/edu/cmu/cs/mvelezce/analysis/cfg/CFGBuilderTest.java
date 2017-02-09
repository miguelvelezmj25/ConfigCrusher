package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.language.Helper;
import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;
import edu.cmu.cs.mvelezce.language.lexer.Lexer;
import edu.cmu.cs.mvelezce.language.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by mvelezce on 2/6/17.
 */
public class CFGBuilderTest {
    private static final String PATH = "src/edu/cmu/cs/mvelezce/language/programs/";

    @Test
    public void test1() throws Exception {
        String program = Helper.loadFile(PATH + "program1");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();

        int steps = 0;
        CFG cfg = new CFG();
        Stack<BasicBlock> branchStack = new Stack<>();

        Statement statement = new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantConfiguration("C"));
        BasicBlock basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(cfg.getEntry(), basicBlock);
        BasicBlock currentBasicBlock = basicBlock;

        statement = new StatementAssignment(new ExpressionVariable("b"), "=", new ExpressionVariable("a"));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantInt(0)));
        statements.add(new StatementSleep(new ExpressionConstantInt(2)));

        StatementIf statementIf = new StatementIf(new ExpressionVariable("b"), new StatementBlock(statements));
        basicBlock = new BasicBlock(steps++ + "| " + statementIf, statementIf);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;
        branchStack.push(currentBasicBlock);

        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement);
            cfg.addEdge(currentBasicBlock, basicBlock);
            currentBasicBlock = basicBlock;
        }

        cfg.addEdge(currentBasicBlock, cfg.getExit());
        cfg.addEdge(branchStack.pop(), cfg.getExit());

        Assert.assertEquals(cfg, builder.buildCFG(ast));
    }

    @Test
    public void test2() throws Exception {
        String program = Helper.loadFile(PATH + "program2");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();

        int steps = 0;
        CFG cfg = new CFG();
        Stack<BasicBlock> branchStack = new Stack<>();

        Statement statement = new StatementSleep(new ExpressionConstantConfiguration("C"));
        BasicBlock basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(cfg.getEntry(), basicBlock);
        BasicBlock currentBasicBlock = basicBlock;

        statement = new StatementAssignment(new ExpressionVariable("a"), "=",
                new ExpressionBinary(new ExpressionConstantConfiguration("C"), "-", new ExpressionConstantInt(1)));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        statement = new StatementSleep(new ExpressionVariable("a"));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        cfg.addEdge(currentBasicBlock, cfg.getExit());

        Assert.assertEquals(cfg, builder.buildCFG(ast));
    }

    @Test
    public void test3() throws Exception {
        String program = Helper.loadFile(PATH + "program3");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();

        int steps = 0;
        CFG cfg = new CFG();
        Stack<BasicBlock> branchStack = new Stack<>();

        Statement statement = new StatementAssignment(new ExpressionVariable("b"), "=", new ExpressionConstantConfiguration("C"));
        BasicBlock basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(cfg.getEntry(), basicBlock);
        BasicBlock currentBasicBlock = basicBlock;

        statement = new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionVariable("b"));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementSleep(new ExpressionConstantInt(2)));

        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));
        basicBlock = new BasicBlock(steps++ + "| " + statementIf, statementIf);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;
        branchStack.push(currentBasicBlock);

        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement);
            cfg.addEdge(currentBasicBlock, basicBlock);
            currentBasicBlock = basicBlock;
        }

        statement = new StatementAssignment(new ExpressionVariable("b"), "=", new ExpressionConstantInt(0));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        cfg.addEdge(branchStack.pop(), currentBasicBlock);

        statement = new StatementSleep(new ExpressionConstantInt(3));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        cfg.addEdge(currentBasicBlock, cfg.getExit());

        Assert.assertEquals(cfg, builder.buildCFG(ast));
    }

    @Test
    public void test4() throws Exception {
        String program = Helper.loadFile(PATH + "program4");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();

        int steps = 0;
        CFG cfg = new CFG();
        Stack<BasicBlock> branchStack = new Stack<>();

        Statement statement = new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantInt(0));
        BasicBlock basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(cfg.getEntry(), basicBlock);
        BasicBlock currentBasicBlock = basicBlock;

        statement = new StatementAssignment(new ExpressionVariable("b"), "=",
                new ExpressionBinary(new ExpressionConstantConfiguration("C"), "+", new ExpressionVariable("a")));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementSleep(new ExpressionConstantInt(2)));
        statements.add(new StatementAssignment(new ExpressionVariable("b"), "=", new ExpressionConstantInt(2)));

        StatementIf statementIf = new StatementIf(new ExpressionVariable("b"), new StatementBlock(statements));
        basicBlock = new BasicBlock(steps++ + "| " + statementIf, statementIf);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;
        branchStack.push(currentBasicBlock);

        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement);
            cfg.addEdge(currentBasicBlock, basicBlock);
            currentBasicBlock = basicBlock;
        }

        statement = new StatementSleep(new ExpressionConstantConfiguration("C"));
        basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;

        cfg.addEdge(branchStack.pop(), currentBasicBlock);
        cfg.addEdge(currentBasicBlock, cfg.getExit());

        Assert.assertEquals(cfg, builder.buildCFG(ast));
    }

    @Test
    public void test5() throws Exception {
        String program = Helper.loadFile(PATH + "program5");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();

        int steps = 0;
        CFG cfg = new CFG();
        Stack<BasicBlock> branchStack = new Stack<>();

        Statement statement = new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantConfiguration("C"));
        BasicBlock basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(cfg.getEntry(), basicBlock);
        BasicBlock currentBasicBlock = basicBlock;

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementSleep(new ExpressionConstantInt(2)));
        statements.add(new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantInt(0)));

        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));
        basicBlock = new BasicBlock(steps++ + "| " + statementIf, statementIf);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;
        branchStack.push(currentBasicBlock);

        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement);
            cfg.addEdge(currentBasicBlock, basicBlock);
            currentBasicBlock = basicBlock;
        }

        statements = new LinkedList<>();
        statements.add(new StatementSleep(new ExpressionConstantInt(2)));

        statementIf = new StatementIf(new ExpressionUnary("!", new ExpressionConstantConfiguration("C")), new StatementBlock(statements));
        basicBlock = new BasicBlock(steps++ + "| " + statementIf, statementIf);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;
        cfg.addEdge(branchStack.pop(), currentBasicBlock);
        branchStack.push(currentBasicBlock);

        for(Statement trueStatements : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock(steps++ + "| " + trueStatements, trueStatements);
            cfg.addEdge(currentBasicBlock, basicBlock);
            currentBasicBlock = basicBlock;
        }

        cfg.addEdge(branchStack.pop(), cfg.getExit());
        cfg.addEdge(currentBasicBlock, cfg.getExit());

        Assert.assertEquals(cfg, builder.buildCFG(ast));
    }

    @Test
    public void test6() throws Exception {
        String program = Helper.loadFile(PATH + "program6");
        Lexer lexer = new Lexer(program);
        Parser parser = new Parser(lexer);
        Statement ast = parser.parse();
        CFGBuilder builder = new CFGBuilder();

        int steps = 0;
        CFG cfg = new CFG();
        Stack<BasicBlock> branchStack = new Stack<>();
        boolean cameFromInner = false;

        Statement statement = new StatementAssignment(new ExpressionVariable("a"), "=", new ExpressionConstantConfiguration("C"));
        BasicBlock basicBlock = new BasicBlock(steps++ + "| " + statement, statement);
        cfg.addEdge(cfg.getEntry(), basicBlock);
        BasicBlock currentBasicBlock = basicBlock;

        List<Statement> statementsInner = new LinkedList<>();
        statementsInner.add(new StatementSleep(new ExpressionConstantInt(2)));

        StatementIf statementIfInner = new StatementIf(new ExpressionUnary("!", new ExpressionVariable("b")),
                new StatementBlock(statementsInner));

        List<Statement> statements = new LinkedList<>();
        statements.add(new StatementSleep(new ExpressionConstantInt(1)));
        statements.add(new StatementAssignment(new ExpressionVariable("b"), "=", new ExpressionConstantConfiguration("D")));
        statements.add(statementIfInner);
        statements.add(new StatementSleep(new ExpressionConstantInt(3)));

        StatementIf statementIf = new StatementIf(new ExpressionVariable("a"), new StatementBlock(statements));
        basicBlock = new BasicBlock(steps++ + "| " + statementIf, statementIf);
        cfg.addEdge(currentBasicBlock, basicBlock);
        currentBasicBlock = basicBlock;
        branchStack.push(currentBasicBlock);

        for(Statement trueStatement : ((StatementBlock) statementIf.getThenBlock()).getStatements()) {
            basicBlock = new BasicBlock(steps++ + "| " + trueStatement, trueStatement);
            cfg.addEdge(currentBasicBlock, basicBlock);
            currentBasicBlock = basicBlock;

            if(cameFromInner) {
                cfg.addEdge(branchStack.pop(), currentBasicBlock);
                cameFromInner = false;
            }

            if(trueStatement instanceof StatementIf) {
                cameFromInner = true;
                branchStack.push(currentBasicBlock);

                for(Statement trueStatementInner : ((StatementBlock) ((StatementIf) trueStatement).getThenBlock()).getStatements()) {
                    basicBlock = new BasicBlock(steps++ + "| " + trueStatementInner, trueStatementInner);
                    cfg.addEdge(currentBasicBlock, basicBlock);
                    currentBasicBlock = basicBlock;
                }
            }

        }

        cfg.addEdge(branchStack.pop(), cfg.getExit());
        cfg.addEdge(currentBasicBlock, cfg.getExit());

        Assert.assertEquals(cfg, builder.buildCFG(ast));
    }

}