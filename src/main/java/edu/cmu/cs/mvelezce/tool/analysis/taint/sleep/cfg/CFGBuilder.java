package edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg;


import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.*;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReturnerVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A Control Flow Graph builder. It implements the Visitor pattern to add each statement to the CFG.
 *
 * @author Miguel Velez - miguelvelezmj25
 * @version 0.1.0.1
 */
public class CFGBuilder extends ReturnerVisitor {
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
        this.cfg = new CFG();
        this.currentBasicBlock = this.cfg.getEntry();
        this.branchStack = new Stack<>();
        this.conditionStack = new Stack<>();
        this.currentConditions = null;
        this.checkingConditions = false;
    }

    /**
     * Starts building a CFG from the provided statement.
     *
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
    public Expression visitConstantConfigurationExpression(ConstantConfigurationExpression expressionConfigurationConstant) {
        if(expressionConfigurationConstant == null) {
            throw new IllegalArgumentException("The expressionConfigurationConstant cannot be null");
        }

        if(this.checkingConditions) {
            this.currentConditions.add(expressionConfigurationConstant);
        }

        return expressionConfigurationConstant;
    }

    @Override
    public Expression visitVariableExpression(VariableExpression expressionVariable) {
        if(expressionVariable == null) {
            throw new IllegalArgumentException("The expressionVariable cannot be null");
        }

        if(this.checkingConditions) {
            this.currentConditions.add(expressionVariable);
        }

        return expressionVariable;
    }

    @Override
    public Void visitAssignmentStatement(AssignmentStatement statementAssignment) {
        if(statementAssignment == null) {
            throw new IllegalArgumentException("The statementAssignment cannot be null");
        }

        this.checkConditions(statementAssignment);
        return null;
    }

    /**
     * This method pushes this IfStatement to a stack that and analyzes the Then branch. This is done so that this
     * BasicBlock also has an edge to the next BasicBlock after then Then branch.
     *
     * @param statementIf
     */
    @Override
    public Void visitIfStatement(IfStatement statementIf) {
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
    public Void visitSleepStatement(SleepStatement statementSleep) {
        if(statementSleep == null) {
            throw new IllegalArgumentException("The statementSleep cannot be null");
        }

        this.checkConditions(statementSleep);
        return null;
    }


    @Override
    public Void visitWhileStatement(WhileStatement statementWhile) {
        if(statementWhile == null) {
            throw new IllegalArgumentException("The statementWhile cannot be null");
        }

        throw new RuntimeException("This method is currently not implemented");
//        We might have to visit block
//        this.checkConditions(statementWhile);
//        return null;
    }

    /**
     * Checks if there the builder came from analyzing a IfStatement. This allows to create an edge between the
     * IfStatement and the BasicBlock after it.
     *
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
            basicBlock = new BasicBlock(statement);
        }
        else {
            this.currentConditions = new ArrayList<>();
            for(Expression condition : this.conditionStack) {
                condition.accept(this);
            }

            // TODO should we reverse this list? If it is reverse, we know what is the first condition for this statement, specially in nested ifs
//            Collections.reverse(this.currentConditions);
            basicBlock = new BasicBlock(statement, this.currentConditions);
        }

        this.cfg.addEdge(this.currentBasicBlock, basicBlock);
        this.checkBranching(basicBlock);
        this.currentBasicBlock = basicBlock;

        this.checkingConditions = false;
        return basicBlock;
    }
}
