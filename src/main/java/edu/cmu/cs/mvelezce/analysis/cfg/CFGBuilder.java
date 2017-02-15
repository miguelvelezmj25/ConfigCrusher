package edu.cmu.cs.mvelezce.analysis.cfg;

import edu.cmu.cs.mvelezce.analysis.visitor.VisitorReturner;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * A Control Flow Graph builder. It implements the Visitor pattern to add each statement to the CFG.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class CFGBuilder extends VisitorReturner {
    private int steps;
    private CFG cfg;
    private BasicBlock currentBasicBlock;
    private Stack<BasicBlock> branchStack;
    private Stack<Expression> conditionStack;
    private List<Expression> currentConditions;
    private boolean checkingConditions;

    /**
     * Initializes a {@code CFGBuilder}.
     */
    public CFGBuilder() {
        this.steps = 0;
        this.cfg = new CFG();
        this.currentBasicBlock = this.cfg.getEntry();
        this.branchStack = new Stack<>();
        this.conditionStack = new Stack<>();
        this.currentConditions = null;
        this.checkingConditions = false;
    }

    /**
     * Starts building a CFG from the provided statement.
     * @param ast
     * @return
     */
    public CFG buildCFG(Statement ast) {
        if(ast == null) {
            throw new IllegalArgumentException("The statement cannot be null");
        }

        ast.accept(this);
        this.cfg.addEdge(this.currentBasicBlock, this.cfg.getExit());

        if(!this.branchStack.isEmpty()) {
            this.cfg.addEdge(this.branchStack.pop(), this.cfg.getExit());
        }

        return this.cfg;
    }

    @Override
    public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
        if(expressionConfigurationConstant == null) {
            throw new IllegalArgumentException("The expressionConfigurationConstant cannot be null");
        }

        if(this.checkingConditions) {
            this.currentConditions.add(expressionConfigurationConstant);
        }

        return expressionConfigurationConstant;
    }

    @Override
    public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
        if(expressionVariable == null) {
            throw new IllegalArgumentException("The expressionVariable cannot be null");
        }

        if(this.checkingConditions) {
            this.currentConditions.add(expressionVariable);
        }

        return expressionVariable;
    }

    @Override
    public Void visitStatementAssignment(StatementAssignment statementAssignment) {
        if(statementAssignment == null) {
            throw new IllegalArgumentException("The statementAssignment cannot be null");
        }

        this.checkConditions(statementAssignment);
        return null;
    }

    /**
     * This method pushes this StatementIf to a stack that and analyzes the Then branch. This is done so that this
     * BasicBlock also has an edge to the next BasicBlock after then Then branch.
     *
     * @param statementIf
     */
    @Override
    public Void visitStatementIf(StatementIf statementIf) {
        if(statementIf == null) {
            throw new IllegalArgumentException("The statementIf cannot be null");
        }

        BasicBlock basicBlock = this.checkConditions(statementIf);

        this.conditionStack.push(statementIf.getCondition());

        statementIf.getThenBlock().accept(this);

        this.conditionStack.pop();
        this.branchStack.push(basicBlock);
        return null;
    }

    @Override
    public Void visitStatementSleep(StatementSleep statementSleep) {
        if(statementSleep == null) {
            throw new IllegalArgumentException("The statementSleep cannot be null");
        }

        this.checkConditions(statementSleep);
        return null;
    }


    @Override
    public Void visitStatementWhile(StatementWhile statementWhile) {
        if(statementWhile == null) {
            throw new IllegalArgumentException("The statementWhile cannot be null");
        }

        // TODO might have to visit block
        this.checkConditions(statementWhile);
        return null;
    }

    /**
     * Checks if there the builder came from analyzing a StatementIf. This allows to create an edge between the
     * StatementIf and the BasicBlock after it.
     * @param basicBlock
     */
    private void checkBranching(BasicBlock basicBlock) {
        if(basicBlock == null) {
            throw new IllegalArgumentException("The basicBlock cannot be null");
        }

        if(!this.branchStack.isEmpty()) {
            this.cfg.addEdge(this.branchStack.pop(), basicBlock);
        }
    }

    /**
     * Adds the conditions that affect this statement. It does not know the exact value of the expression; rather it
     * knows what the expression(s) are.
     *
     * @param statement
     * @return
     */
    private BasicBlock checkConditions(Statement statement) {
        if(statement == null) {
            throw new IllegalArgumentException("The statement cannot be null");
        }

        this.checkingConditions = true;
        BasicBlock basicBlock;

        if(this.conditionStack.isEmpty()) {
            basicBlock = new BasicBlock(this.steps++ + "| " + statement, statement);
        }
        else {
            this.currentConditions = new ArrayList<>();
            for(Expression condition : this.conditionStack) {
                condition.accept(this);
            }

            Collections.reverse(this.currentConditions);
            basicBlock = new BasicBlock(this.steps++ + "| " + statement, statement, this.currentConditions);
        }

        this.cfg.addEdge(this.currentBasicBlock, basicBlock);
        this.checkBranching(basicBlock);
        this.currentBasicBlock = basicBlock;

        this.checkingConditions = false;
        return basicBlock;
    }
}
