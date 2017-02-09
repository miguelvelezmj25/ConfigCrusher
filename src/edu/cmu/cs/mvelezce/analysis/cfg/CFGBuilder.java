package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.analysis.visitor.BaseVisitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

import java.util.ArrayList;
import java.util.Collections;
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
    private Stack<Expression> conditionStack;

    public CFGBuilder() {
        this.steps = 0;
        this.cfg = new CFG();
        this.currentBasicBlock = this.cfg.getEntry();
        this.branchStack = new Stack<>();
        this.conditionStack = new Stack<>();
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
        this.checkConditions(statementAssignment);
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        BasicBlock basicBlock = this.checkConditions(statementIf);

        this.conditionStack.push(statementIf.getCondition());

        statementIf.getThenBlock().accept(this);

        this.conditionStack.pop();
        this.branchStack.push(basicBlock);
    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        this.checkConditions(statementSleep);
    }

    @Override
    public void visitStatementWhile(StatementWhile statementWhile) {
        // TODO might have to visit block
        this.checkConditions(statementWhile);
    }

    private void checkBranching(BasicBlock basicBlock) {
        if(!this.branchStack.isEmpty()) {
            this.cfg.addEdge(this.branchStack.pop(), basicBlock);
        }
    }

    private BasicBlock checkConditions(Statement statement) {
        BasicBlock basicBlock;

        if(this.conditionStack.isEmpty()) {
            basicBlock = new BasicBlock(this.steps++ + "| " + statement, statement);
        }
        else {
            List<Expression> conditions = new ArrayList<>();
            for(Expression condition : this.conditionStack) {
                conditions.add(condition);
            }

            Collections.reverse(conditions);
            basicBlock = new BasicBlock(this.steps++ + "| " + statement, statement, conditions);
        }

        this.cfg.addEdge(this.currentBasicBlock, basicBlock);
        this.checkBranching(basicBlock);
        this.currentBasicBlock = basicBlock;

        return basicBlock;
    }
}
