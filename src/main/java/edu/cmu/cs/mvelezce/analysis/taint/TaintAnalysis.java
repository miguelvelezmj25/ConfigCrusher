package edu.cmu.cs.mvelezce.analysis.taint;

import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.visitor.VisitorReturner;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementAssignment;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementIf;

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

    /**
     * Initialize a {@code TaintAnalysis}.
     */
    public TaintAnalysis() {
        this.instructionToTainted = new LinkedHashMap<>();
    }

    /**
     * Performan a taint analysis in the CFG and a map that represents, for every basic block, the variables that might
     * be tainted.
     *
     * @param cfg
     * @return
     */
    public Map<BasicBlock, Set<TaintedVariable>> analyze(CFG cfg) {
        List<BasicBlock> entry = cfg.getSuccessors(cfg.getEntry());

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
// CK           taintedVariables=transfer(taintedVariables, instruction);
            Set<TaintedVariable> taintsAfter = transfer(taintsBefore, instruction);

// CK           this.instructionToTainted.put(instruction, /*taintsAfter*/currentTaintedVariables);
            this.instructionToTainted.put(instruction, taintsAfter);

            List<BasicBlock> successors = cfg.getSuccessors(instruction);

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
        TransferVisitor transferVisitor = new TransferVisitor(oldTaints, instruction.getConditions());
        instruction.getStatement().accept(transferVisitor);

        Set<TaintedVariable> newTaints = transferVisitor.getTaintedVariables();
        Set<TaintedVariable> killedTaints = transferVisitor.getKilledTaintedVariables();

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

    /**
     * Returns a map detailing, for each basic block, what variables might be tainted.
     *
     * @return
     */
    public Map<BasicBlock, Set<TaintedVariable>> getInstructionToTainted() {
        return this.instructionToTainted;
    }

    private class TransferVisitor extends VisitorReturner {
        private final Set<TaintedVariable> oldTaints;
        private boolean inAssignment;
        private boolean tainting;
        private Set<ExpressionConfigurationConstant> taintingConfigurations;
        private Set<TaintedVariable> taintedVariables;
        private Set<TaintedVariable> killedTaintedVariables;
        private List<Expression> conditions;

        public TransferVisitor(Set<TaintedVariable> oldTaints, List<Expression> conditions) {
            this.oldTaints = oldTaints;
            this.inAssignment = false;
            this.tainting = false;
            this.taintingConfigurations = new HashSet<>();
            this.taintedVariables = new HashSet<>();
            this.killedTaintedVariables = new HashSet<>();
            this.conditions = conditions;
        }

        @Override
        public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
            Expression expression = super.visitExpressionConstantConfiguration(expressionConfigurationConstant);
            this.tainting = true;
            this.taintingConfigurations.add(expressionConfigurationConstant);

            return expression;
        }

        @Override
        public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
            Expression expression = super.visitExpressionVariable(expressionVariable);

            if(this.inAssignment) {
                for (TaintedVariable taintedVariable : this.oldTaints) {
                    if (taintedVariable.getVariable().equals(expressionVariable)) {
                        this.tainting = true;
                        this.taintingConfigurations.addAll(taintedVariable.getConfigurations());
                        break;
                    }
                }
            }

            return expression;
        }

        @Override
        public Void visitStatementAssignment(StatementAssignment statementAssignment) {
            this.inAssignment = true;
            statementAssignment.getRight().accept(this);

            if(this.tainting) {
                Set<ExpressionConfigurationConstant> configurations = new HashSet<>(this.taintingConfigurations);
                this.taintedVariables.add(new TaintedVariable(statementAssignment.getVariable(), configurations));
            }

            if(!this.conditions.isEmpty()) {
                for(TaintedVariable oldTaintedVariable : this.oldTaints) {
                    if(this.conditions.contains(oldTaintedVariable.getVariable())) {
                        if(this.taintedVariables.isEmpty()) {
                            this.taintedVariables.add(new TaintedVariable(statementAssignment.getVariable(), oldTaintedVariable.getConfigurations()));
                        }
                        else {
                            for(TaintedVariable taintedVariable : this.taintedVariables) {
                                if (taintedVariable.getVariable().equals(statementAssignment.getVariable())) {
                                    taintedVariable.getConfigurations().addAll(oldTaintedVariable.getConfigurations());
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if(!this.tainting) {
                for (TaintedVariable taintedVariable : this.oldTaints) {
                    if (taintedVariable.getVariable().equals(statementAssignment.getVariable())) {
                        this.killedTaintedVariables.add(taintedVariable);
                        break;
                    }
                }
            }

            this.inAssignment = false;
            return null;
        }

        @Override
        public Void visitStatementIf(StatementIf statementIf) {
            statementIf.getCondition().accept(this);
            return null;
        }


        public Set<TaintedVariable> getTaintedVariables() { return this.taintedVariables; }

        public Set<TaintedVariable> getKilledTaintedVariables() {
            return this.killedTaintedVariables;
        }
    }

    /**
     * A possible tainted variable and a set of configurations that might have tainted it.
     */
    public static class TaintedVariable {
        private ExpressionVariable variable;
        private Set<ExpressionConfigurationConstant> configurations;

        public TaintedVariable(ExpressionVariable variable, Set<ExpressionConfigurationConstant> configurations) {
            if(variable == null) {
                throw new IllegalArgumentException("The variable cannot be null");
            }

            if(configurations == null) {
                throw new IllegalArgumentException("The configuration cannot be null");
            }

            this.variable = variable;
            this.configurations = configurations;
        }

        /**
         * Returns the variable that might be tainted.
         *
         * @return
         */
        public ExpressionVariable getVariable() { return this.variable; }

        /**
         * Returns the configurations that might have tainted the variable.
         *
         * @return
         */
        public Set<ExpressionConfigurationConstant> getConfigurations() { return this.configurations; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TaintedVariable that = (TaintedVariable) o;

            if (!variable.equals(that.variable)) return false;
            return configurations.equals(that.configurations);
        }

        @Override
        public int hashCode() {
            int result = variable.hashCode();
            result = 31 * result + configurations.hashCode();
            return result;
        }

        @Override
        public String toString() { return this.variable + "<-" + this.configurations; }
    }

}
