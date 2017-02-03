package edu.cmu.cs.mvelezce.interpreter.cfg;

import edu.cmu.cs.mvelezce.interpreter.ast.expression.*;
import edu.cmu.cs.mvelezce.interpreter.ast.statement.*;
import edu.cmu.cs.mvelezce.interpreter.ast.value.ValueInt;
import edu.cmu.cs.mvelezce.interpreter.parser.Parser;
import edu.cmu.cs.mvelezce.interpreter.visitor.Visitor;

import java.util.List;
import java.util.Stack;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class CFGVisitor implements Visitor<ValueInt> {
    private int steps;
    private CFG cfg;
    private BasicBlock currentBasicBlock;
    private Stack<BasicBlock> expressionStack;

    public CFGVisitor(Parser parser) {
        this.steps = 0;
        this.cfg = new CFG();
        this.currentBasicBlock = this.cfg.getEntry();
        this.expressionStack = new Stack();
    }

    public CFG buildCFG(Statement ast) {
        ast.accept(this);
        this.cfg.addEdge(this.currentBasicBlock, this.cfg.getExit());
        // TODO add the current basic block to the exit and the if condition
        return this.cfg;
    }

    @Override
    public ValueInt visitExpressionBinary(ExpressionBinary expressionBinary) { return null; }

    @Override
    public ValueInt visitExpressionConfiguration(ExpressionConfiguration expressionConfiguration) {
        return null;
    }

    @Override
    public ValueInt visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return null;
    }

    @Override
    public ValueInt visitExpressionUnary(ExpressionUnary expressionUnary) {
        return null;
    }

    @Override
    public ValueInt visitExpressionVariable(ExpressionVariable varExpr) {
        return null;
    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {
        List<Statement> statements = statementBlock.getStatements();

        for(Statement statement : statements) {
            statement.accept(this);
        }
    }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        BasicBlock statement = new BasicBlock(this.steps++ + "| " + statementAssignment.toString(),
                statementAssignment);
//        this.cfg.addBasicBlock(statement);
        this.cfg.addEdge(this.currentBasicBlock, statement);
        this.checkBranching(statement);
        this.currentBasicBlock = statement;
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        BasicBlock expression = new BasicBlock(this.steps++ + "| " + statementIf.getCondition().toString(),
                statementIf.getCondition());
//        this.cfg.addBasicBlock(expression);
        this.cfg.addEdge(this.currentBasicBlock, expression);
        this.checkBranching(expression);

        this.currentBasicBlock = expression;

        statementIf.getStatementThen().accept(this);

        this.expressionStack.push(expression);
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        BasicBlock statement = new BasicBlock(this.steps++ + "| " + statementSleep.toString(), statementSleep);
//        this.cfg.addBasicBlock(statement);
        this.cfg.addEdge(this.currentBasicBlock, statement);
        this.checkBranching(statement);

        this.currentBasicBlock = statement;
    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {
        BasicBlock expression = new BasicBlock(this.steps++ + "| " + statementAssignment.getCondition().toString(),
            statementAssignment.getCondition());
//        this.cfg.addBasicBlock(expression);
        this.cfg.addEdge(this.currentBasicBlock, expression);
        this.checkBranching(expression);

        this.currentBasicBlock = expression;
    }

    private void checkBranching(BasicBlock basicBlock) {
        if(!this.expressionStack.isEmpty()) {
            this.cfg.addEdge(this.expressionStack.pop(), basicBlock);
        }
    }
}
