package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.visitor.BaseVisitor;
import edu.cmu.cs.mvelezce.analysis.visitor.Visitor;
import edu.cmu.cs.mvelezce.language.ast.expression.*;
import edu.cmu.cs.mvelezce.language.ast.statement.*;

import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
// TODO should I implement the visitor pattern?
// TODO what should be the generic?
public class TaintAnalysis {

// TODO Fix point loop
// TODO Worklist algorithm from cfg
// TODO Transfer function is to check how abstraction is changed from statement to statement
// TODO Join function is to join abstractions after they have branched out
    private Map<BasicBlock, Set<ExpressionVariable>> instructionToTainted;
    private CFG cfg;

    public TaintAnalysis(CFG cfg) {
        this.instructionToTainted = new LinkedHashMap<>();
        this.cfg = cfg;
    }

    public void analyze() {
        List<BasicBlock> entry = this.cfg.getSuccessors(this.cfg.getEntry());

        if(entry.size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        Queue<BasicBlock> worklist = new LinkedList<>();
        worklist.add(entry.get(0));

        while(!worklist.isEmpty()) {
            // Get the next available instruction
            BasicBlock instruction = worklist.remove();

            // Analyze
            System.out.println(instruction);

            //taintsBefore = instructionToTainted.get(for all previousIstr)

            if(instruction.getStatement() != null) {
                instruction.getStatement().accept(this);
            }
            else {
                instruction.getExpression().accept(this);
            }

            //S' = transfer(S, instruction)
            taintedValues=transfer(taintedValues, instruction);


            List<ExpressionVariable> currentTaintedVariables = new ArrayList<>();
            currentTaintedVariables.addAll(this.taintedValues);
            this.instructionToTainted.put(instruction, /*taintsAfter*/currentTaintedVariables);

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
                    worklist.add(possibleInstruction);
                }
            }

            possibleInstruction = successors.get(0);
            if(!possibleInstruction.isSpecial()) {
                worklist.add(possibleInstruction);
            }

        }

        System.out.println(this.instructionToTainted);
    }

    private Set<ExpressionVariable> transfer(Set<ExpressionVariable> oldTaints, BasicBlock instruction) {
        Set<ExpressionVariable> output = new HashSet<>(oldTaints);
        instruction.getStatement().accept(new TransferVisitor());
    }

    private void transfer() {
        // TODO
    }

    private void join() {
        // TODO
    }


    private class TransferVisitor extends BaseVisitor {
        private boolean taintedAssignment;

        public TransferVisitor() {
            this.taintedAssignment = false;
        }

        @Override
        public Expression visitExpressionConstantConfiguration(ExpressionConstantConfiguration expressionConstantConfiguration) {
            Expression expression = super.visitExpressionConstantConfiguration(expressionConstantConfiguration);
            this.taintedAssignment = true;

            return expression;
        }

        @Override
        public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
            Expression expression = super.visitExpressionVariable(expressionVariable);
            
            if(this.taintedAssignment) {
                // TODO tainted
            }

            return expression;
        }

        @Override
        public void visitStatementAssignment(StatementAssignment statementAssignment) {
            super.visitStatementAssignment(statementAssignment);

            if(this.taintedAssignment) {
                // TODO tainted
            }
        }

    }

}
