package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.visitor.BaseVisitor;
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

    private Map<BasicBlock, Set<TaintedVariable>> instructionToTainted;
    private CFG cfg;

    public TaintAnalysis(CFG cfg) {
        this.instructionToTainted = new LinkedHashMap<>();
        this.cfg = cfg;
    }

    public Map<BasicBlock, Set<TaintedVariable>> analyze() {
        List<BasicBlock> entry = this.cfg.getSuccessors(this.cfg.getEntry());

        if(entry.size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        Queue<BasicBlock> worklist = new LinkedList<>();
        worklist.add(entry.get(0));
        this.instructionToTainted.put(entry.get(0), new HashSet<>());

        while(!worklist.isEmpty()) {
            BasicBlock instruction = worklist.remove();

// CK          taintsBefore = this.instructionToTainted.get(for all previousIstr)
            Set<TaintedVariable> taintsBefore = this.instructionToTainted.get(instruction);

// CK           S' = transfer(S, instruction)
// CK           taintedValues=transfer(taintedValues, instruction);
            Set<TaintedVariable> taintsAfter = transfer(taintsBefore, instruction);

// CK           this.instructionToTainted.put(instruction, /*taintsAfter*/currentTaintedVariables);
            this.instructionToTainted.put(instruction, taintsAfter);

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
                    taintsAfter = TaintAnalysis.join(taintsAfter, this.instructionToTainted.get(possibleInstruction));
                    this.instructionToTainted.put(possibleInstruction, taintsAfter);
//                    this.instructionToTainted.put(possibleInstruction, new HashSet<>());
                }
            }

            possibleInstruction = successors.get(0);
            if(!possibleInstruction.isSpecial()) {
                worklist.add(possibleInstruction);
                taintsAfter = TaintAnalysis.join(taintsAfter, this.instructionToTainted.get(possibleInstruction));
                this.instructionToTainted.put(possibleInstruction, taintsAfter);
//                this.instructionToTainted.put(possibleInstruction, new HashSet<>());
            }

        }

        return this.instructionToTainted;
    }

    // TODO should be private. Protected allows testing
    public Set<TaintedVariable> transfer(Set<TaintedVariable> oldTaints, BasicBlock instruction) {
        // TODO CK why do I need this?
        Set<TaintedVariable> output = new HashSet<>(oldTaints);
        TransferVisitor transferVisitor = new TransferVisitor(oldTaints);
        instruction.getStatement().accept(transferVisitor);

        Set<TaintedVariable> newTaints = transferVisitor.getTaintedValues();
        Set<TaintedVariable> killedTaints = transferVisitor.getKilledTaintedValues();

        Set<TaintedVariable> result = new HashSet<>();
        Set<TaintedVariable> merge = new HashSet<>();

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
            for(TaintedVariable possibleTaint : merge) {
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

    // TODO should be private. Protected allows testing
    // Can only stay in the same level of the lattice or go up
    protected static Set<TaintedVariable> join(Set<TaintedVariable> taintsOne, Set<TaintedVariable> taintsTwo) {
        Set<TaintedVariable> result = new HashSet<>();

        if(taintsOne != null) {
            result.addAll(taintsOne);
        }

        if(taintsTwo != null) {
            result.addAll(taintsTwo);
        }

        return result;
    }


    private class TransferVisitor extends BaseVisitor {
        private final Set<TaintedVariable> oldTaints;
        private boolean taintedAssignment;
        private Set<ExpressionConstantConfiguration> taintingConfigurations;
        private Set<TaintedVariable> taintedValues;
        private Set<TaintedVariable> killedTaintedValues;

        public TransferVisitor(Set<TaintedVariable> oldTaints) {
            this.oldTaints = oldTaints;
            this.taintedAssignment = false;
            this.taintingConfigurations = new HashSet<>();
            this.taintedValues = new HashSet<>();
            this.killedTaintedValues = new HashSet<>();
        }

        @Override
        public Expression visitExpressionConstantConfiguration(ExpressionConstantConfiguration expressionConstantConfiguration) {
            Expression expression = super.visitExpressionConstantConfiguration(expressionConstantConfiguration);
            this.taintedAssignment = true;
            this.taintingConfigurations.add(expressionConstantConfiguration);

            return expression;
        }

        @Override
        public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
            Expression expression = super.visitExpressionVariable(expressionVariable);

            for (TaintedVariable taintedVariable : this.oldTaints) {
                if (taintedVariable.getVariable().equals(expressionVariable)) {
                    this.taintedAssignment = true;
                    this.taintingConfigurations.add(taintedVariable.getConfiguration());
                }
            }

            return expression;
        }

        @Override
        public void visitStatementAssignment(StatementAssignment statementAssignment) {
            statementAssignment.getRight().accept(this);

            if(this.taintedAssignment) {
                for(ExpressionConstantConfiguration configuration : this.taintingConfigurations) {
                    this.taintedValues.add(new TaintedVariable(statementAssignment.getVariable(), configuration));
                }
            }

            for (TaintedVariable taintedVariable : this.oldTaints) {
                if (taintedVariable.getVariable().equals(statementAssignment.getVariable())) {
                    this.killedTaintedValues.add(taintedVariable);
                }
            }
        }

        @Override
        public void visitStatementIf(StatementIf statementIf) {
            statementIf.getCondition().accept(this);
        }

        public Set<TaintedVariable> getTaintedValues() { return this.taintedValues;
        }

        public Set<TaintedVariable> getKilledTaintedValues() {
            return this.killedTaintedValues;
        }
    }

    public static class TaintedVariable {
        private ExpressionVariable variable;
        private ExpressionConstantConfiguration configuration;

        public TaintedVariable(ExpressionVariable variable, ExpressionConstantConfiguration configuration) {
            this.variable = variable;
            this.configuration = configuration;
        }

        public ExpressionVariable getVariable() { return this.variable; }

        public ExpressionConstantConfiguration getConfiguration() { return this.configuration; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TaintedVariable that = (TaintedVariable) o;

            if (!variable.equals(that.variable)) return false;
            return configuration.equals(that.configuration);
        }

        @Override
        public int hashCode() {
            int result = variable.hashCode();
            result = 31 * result + configuration.hashCode();
            return result;
        }

        @Override
        public String toString() { return this.variable + "<-" + this.configuration; }
    }

    public Map<BasicBlock, Set<TaintedVariable>> getInstructionToTainted() {
        return this.instructionToTainted;
    }
}
