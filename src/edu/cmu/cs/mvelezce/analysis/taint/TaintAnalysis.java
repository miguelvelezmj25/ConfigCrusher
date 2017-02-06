package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;
import edu.cmu.cs.mvelezce.language.ast.value.ValueInt;

import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
// TODO should I implement the visitor pattern?
// TODO what should be the generic?
public class TaintAnalysis implements Visitor<Expression> {

// TODO Fix point loop
// TODO Worklist algorithm from cfg
// TODO Transfer function is to check how abstraction is changed from statement to statement
// TODO Join function is to join abstractions after they have branched out


    private Map<BasicBlock, List<ExpressionVariable>> instructionToTainted;
    private Set<ExpressionVariable> taintedValues;
    private CFG cfg;
    private Queue<BasicBlock> worklist;
    private boolean taintedStatement;

    public TaintAnalysis(CFG cfg) {
        this.instructionToTainted = new HashMap<>();
        this.taintedValues = new HashSet<>();
        this.cfg = cfg;
        this.worklist = new LinkedList<>();
        this.taintedStatement = false;
    }

    public void analyze() {
        List<BasicBlock> entry = this.cfg.getSuccessors(this.cfg.getEntry());

        if(entry.size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        this.worklist.add(entry.get(0));

        while(!this.worklist.isEmpty()) {
            // Get the next available instruction
            BasicBlock instruction = this.worklist.remove();

            // Analyze
            System.out.println(instruction);

            if(instruction.getStatement() != null) {
                instruction.getStatement().accept(this);
            }
            else {
                instruction.getExpression().accept(this);
            }

//            if(instruction.getStatement() != null) {
//                Statement statement = instruction.getStatement();
//
//                if(statement instanceof StatementAssignment) {
//                    StatementAssignment assignment = (StatementAssignment) statement;
//
//                    // Adding tainted values
//                    if(assignment.getRight() instanceof ExpressionConfiguration) {
//                        this.taintedValues.add(assignment.getVariable());
////                        this.taintedValues.add((ExpressionVariable) assignment.getRight());
//                    }
//                    else if(assignment.getVariable() instanceof ExpressionConfiguration) {
//
//                    }
//                    else if(this.taintedValues.contains(assignment.getRight())) {
//                        this.taintedValues.add(assignment.getVariable());
//                    }
//
//                    // TODO Removing tainted values
////                    if(this.taintedValues.contains(assignment.getRight()))
//                }
//            }

            // Add next instructions to process
            List<BasicBlock> successors = this.cfg.getSuccessors(instruction);

            if(successors.size() > 2) {
                throw new IllegalArgumentException("We do not support switch statements yet");
            }

            if(successors.size() == 0) {
                continue;
            }

            BasicBlock possibleInstruction;

            if(successors.size() == 2) {
                // TRUE branch is in the 0 position
                possibleInstruction = successors.remove(0);

                if(!possibleInstruction.isSpecial()) {
                    this.worklist.add(possibleInstruction);
                }
            }

            possibleInstruction = successors.get(0);
            if(!possibleInstruction.isSpecial()) {
                this.worklist.add(possibleInstruction);
            }

            this.taintedStatement = false;
        }

        System.out.println(this.taintedValues);
    }

    private void transfer() {
        // TODO
    }

    private void join() {
        // TODO
    }


    @Override
    public Expression visitExpressionBinary(ExpressionBinary expressionBinary) {
        expressionBinary.getRight().accept(this);
        expressionBinary.getLeft().accept(this);

        return expressionBinary;
    }

    @Override
    public Expression visitExpressionConfiguration(ExpressionConfiguration expressionConfiguration) {
        this.taintedStatement = true;
        this.taintedValues.add(expressionConfiguration);
        return expressionConfiguration;
    }

    @Override
    public Expression visitExpressionConstantInt(ExpressionConstantInt expressionConstantInt) {
        return expressionConstantInt;
    }

    @Override
    public Expression visitExpressionUnary(ExpressionUnary expressionUnary) {
        expressionUnary.getExpression().accept(this);
        return expressionUnary;
    }

    @Override
    public Expression visitExpressionVariable(ExpressionVariable varExpr) {
        return varExpr;
    }

    @Override
    public void visitStatementAssignment(StatementAssignment statementAssignment) {
        statementAssignment.getRight().accept(this);
        statementAssignment.getVariable().accept(this);

        if(this.taintedStatement) {
            this.taintedValues.add(statementAssignment.getVariable());
        }
    }

    @Override
    public void visitStatementBlock(StatementBlock statementBlock) {
        List<Statement> statements = statementBlock.getStatements();

        for(Statement statement : statements) {
            statement.accept(this);
        }
    }

    @Override
    public void visitStatementIf(StatementIf statementIf) {
        statementIf.getCondition().accept(this);

        // TODO should I analyze?
        statementIf.getStatementThen().accept(this);

    }

    @Override
    public void visitStatementSleep(StatementSleep statementSleep) {
        statementSleep.getTime().accept(this);
    }

    @Override
    public void visitStatementWhile(StatementWhile statementAssignment) {

    }
}
