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
// TODO Fix point loop
// TODO Transfer function is to check how abstraction is changed from statement to statement
// TODO Join function is to join abstractions after they have branched out
// TODO should I implement the visitor pattern?
// TODO what should be the generic?
public class TaintAnalysis {

    private Map<BasicBlock, Set<ExpressionVariable>> instructionToTainted;
    private CFG cfg;

    public TaintAnalysis(CFG cfg) {
        this.instructionToTainted = new LinkedHashMap<>();
        this.cfg = cfg;
    }

    public Map<BasicBlock, Set<ExpressionVariable>> analyze() {
        List<BasicBlock> entry = this.cfg.getSuccessors(this.cfg.getEntry());

        if(entry.size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        Queue<BasicBlock> worklist = new LinkedList<>();
        worklist.add(entry.get(0));
        this.instructionToTainted.put(entry.get(0), new HashSet<>());

        while(!worklist.isEmpty()) {
            // Get the next available instruction
            BasicBlock instruction = worklist.remove();

            // Analyze
// CK          taintsBefore = this.instructionToTainted.get(for all previousIstr)
            Set<ExpressionVariable> taintsBefore = this.instructionToTainted.get(instruction);

// CK           S' = transfer(S, instruction)
// CK           taintedValues=transfer(taintedValues, instruction);
            Set<ExpressionVariable> taintsAfter = transfer(taintsBefore, instruction);


//            List<ExpressionVariable> currentTaintedVariables = new ArrayList<>();
//            currentTaintedVariables.addAll(this.taintedValues);

// CK           this.instructionToTainted.put(instruction, /*taintsAfter*/currentTaintedVariables);
            this.instructionToTainted.put(instruction, taintsAfter);

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
                    taintsAfter = this.join(taintsAfter, this.instructionToTainted.get(possibleInstruction));
                    this.instructionToTainted.put(possibleInstruction, taintsAfter);
//                    this.instructionToTainted.put(possibleInstruction, new HashSet<>());
                }
            }

            possibleInstruction = successors.get(0);
            if(!possibleInstruction.isSpecial()) {
                worklist.add(possibleInstruction);
                taintsAfter = this.join(taintsAfter, this.instructionToTainted.get(possibleInstruction));
                this.instructionToTainted.put(possibleInstruction, taintsAfter);
//                this.instructionToTainted.put(possibleInstruction, new HashSet<>());
            }

        }

        return this.instructionToTainted;
    }

    private Set<ExpressionVariable> transfer(Set<ExpressionVariable> oldTaints, BasicBlock instruction) {
        Set<ExpressionVariable> output = new HashSet<>(oldTaints);
        TransferVisitor transferVisitor = new TransferVisitor(oldTaints);
        instruction.getStatement().accept(transferVisitor);

        Set<ExpressionVariable> newTaints = transferVisitor.getTaintedValues();
        Set<ExpressionVariable> killedTaints = transferVisitor.getKilledTaintedValues();

        Set<ExpressionVariable> result = new HashSet<>();
        Set<ExpressionVariable> merge = new HashSet<>();

        if(oldTaints.isEmpty()) {
            merge.addAll(newTaints);
        }
        else if(newTaints.isEmpty()) {
            merge.addAll(oldTaints);
        }
        else {
            merge.addAll(oldTaints);
            merge.addAll(newTaints);
        }

        if(!killedTaints.isEmpty()) {
            for(ExpressionVariable possibleTaint : merge) {
                if(!killedTaints.contains(possibleTaint)) {
                    result.add(possibleTaint);
                }
            }
        }
        else {
            result.addAll(merge);
        }

        return result;
    }

    private Set<ExpressionVariable> join(Set<ExpressionVariable> taintsOne, Set<ExpressionVariable> taintsTwo) {
        Set<ExpressionVariable> result = new HashSet<>();

        if(taintsOne != null) {
            result.addAll(taintsOne);
        }

        if(taintsTwo != null) {
            result.addAll(taintsTwo);
        }

        return result;
    }


    private class TransferVisitor extends BaseVisitor {
        private final Set<ExpressionVariable> oldTaints;
        private boolean taintedAssignment;
        private Set<ExpressionVariable> taintedValues;
        private Set<ExpressionVariable> killedTaintedValues;

        public TransferVisitor(Set<ExpressionVariable> oldTaints) {
            this.oldTaints = oldTaints;
            this.taintedAssignment = false;
            this.taintedValues = new HashSet<>();
            this.killedTaintedValues = new HashSet<>();
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
                this.taintedValues.add((ExpressionVariable) expression);
            }

            if(this.oldTaints.contains(expressionVariable)) {
                this.taintedAssignment = true;
            }

            return expression;
        }

        @Override
        public void visitStatementAssignment(StatementAssignment statementAssignment) {
            statementAssignment.getRight().accept(this);

            if(this.taintedAssignment) {
                this.taintedValues.add(statementAssignment.getVariable());
            }
            else if(this.oldTaints.contains(statementAssignment.getVariable())) {
                this.killedTaintedValues.add(statementAssignment.getVariable());
            }
        }

        @Override
        public void visitStatementIf(StatementIf statementIf) {
            statementIf.getCondition().accept(this);
        }

        public Set<ExpressionVariable> getTaintedValues() {
            return this.taintedValues;
        }

        public Set<ExpressionVariable> getKilledTaintedValues() {
            return this.killedTaintedValues;
        }
    }

    public Map<BasicBlock, Set<ExpressionVariable>> getInstructionToTainted() {
        return this.instructionToTainted;
    }
}
