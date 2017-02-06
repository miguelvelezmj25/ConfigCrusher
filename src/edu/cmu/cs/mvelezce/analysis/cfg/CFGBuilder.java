package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.analysis.visitor.BaseVisitor;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

import java.util.List;
import java.util.Stack;

/**
 * Created by miguelvelez on 2/2/17.
 */
public class CFGBuilder extends BaseVisitor {
    private int steps;
    private CFG cfg;
    private BasicBlock currentBasicBlock;
    private Stack<BasicBlock> branchStack;

    public CFGBuilder() {
        this.steps = 0;
        this.cfg = new CFG();
        this.currentBasicBlock = this.cfg.getEntry();
        this.branchStack = new Stack<>();
    }

    public CFG buildCFG(Statement ast) {
        ast.accept(this);
        this.cfg.addEdge(this.currentBasicBlock, this.cfg.getExit());

        if(!this.branchStack.isEmpty()) {
            this.cfg.addEdge(this.branchStack.pop(), this.cfg.getExit());
        }

        return this.cfg;
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
        BasicBlock statement = new BasicBlock(this.steps++ + "| " + statementAssignment, statementAssignment);
        this.cfg.addEdge(this.currentBasicBlock, statement);
        this.checkBranching(statement);
        this.currentBasicBlock = statement;
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        BasicBlock expression = new BasicBlock(this.steps++ + "| " + statementIf, statementIf);
        this.cfg.addEdge(this.currentBasicBlock, expression);
        this.checkBranching(expression);

        this.currentBasicBlock = expression;

        statementIf.getStatementThen().accept(this);

        this.branchStack.push(expression);
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        BasicBlock statement = new BasicBlock(this.steps++ + "| " + statementSleep, statementSleep);
        this.cfg.addEdge(this.currentBasicBlock, statement);
        this.checkBranching(statement);

        this.currentBasicBlock = statement;
    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {
        BasicBlock expression = new BasicBlock(this.steps++ + "| " + statementAssignment, statementAssignment);
        this.cfg.addEdge(this.currentBasicBlock, expression);
        this.checkBranching(expression);

        this.currentBasicBlock = expression;
    }

    private void checkBranching(BasicBlock basicBlock) {
        if(!this.branchStack.isEmpty()) {
            this.cfg.addEdge(this.branchStack.pop(), basicBlock);
        }
    }
}
