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

    /**
     * Initialize a {@code TaintAnalysis}.
     */
    public TaintAnalysis() {
        ;
    }

    /**
     * Perform a taint analysis in the CFG and a map that represents, for every basic block, the variables that might
     * be tainted.
     *
     * @param cfg
     * @return
     */
    public static Map<BasicBlock, Set<PossibleTaint>> analyze(CFG cfg) {
        List<BasicBlock> entry = cfg.getSuccessors(cfg.getEntry());

        if(entry.size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        List<BasicBlock> worklist = new LinkedList<>();
        worklist.add(entry.get(0));

        Map<BasicBlock, Set<PossibleTaint>> instructionsToPossibleTaints = new LinkedHashMap<>();
        instructionsToPossibleTaints.put(entry.get(0), new HashSet<>());

        while(!worklist.isEmpty()) {
            BasicBlock instruction = worklist.remove(0);
            Set<PossibleTaint> possibleTaintsBefore = instructionsToPossibleTaints.get(instruction);
            Set<PossibleTaint> possibleTaintsAfter = transfer(possibleTaintsBefore, instruction);
            instructionsToPossibleTaints.put(instruction, possibleTaintsAfter);

            List<BasicBlock> successors = cfg.getSuccessors(instruction);

            if(successors.size() > 2) {
                throw new IllegalArgumentException("We do not support switch statements yet");
            }

            if(successors.size() == 0) {
                continue;
            }

            if(successors.size() == 2){
                successors = new ArrayList<>(successors);
                Collections.reverse(successors);
            }

            for (BasicBlock successor : successors) {
                if (successor.isSpecial()) {
                    continue;
                }

                if(!worklist.contains(successor)) {
                    worklist.add(0, successor);
                }

                Set<PossibleTaint> possibleTaintsEntry = TaintAnalysis.join(possibleTaintsAfter,
                            instructionsToPossibleTaints.get(successor));
                instructionsToPossibleTaints.put(successor, possibleTaintsEntry);
            }

        }

        return instructionsToPossibleTaints;
    }

    public static Set<PossibleTaint> transfer(Set<PossibleTaint> oldPossibleTaints, BasicBlock instruction) {
        TransferVisitor transferVisitor = new TransferVisitor(oldPossibleTaints, instruction.getConditions());
        instruction.getStatement().accept(transferVisitor);

        Set<PossibleTaint> newPossibleTaints = transferVisitor.getPossibleTaintedVariables();
        Set<PossibleTaint> killedPossibleTaints = transferVisitor.getKilledPossibleTaintedVariables();

        Set<PossibleTaint> result = new HashSet<>();

        for(PossibleTaint possibleTaint : oldPossibleTaints) {
            if(!killedPossibleTaints.contains(possibleTaint)) {
                result.add(possibleTaint);
            }
        }

        result.addAll(newPossibleTaints);

        return result;
    }

    // Can only stay in the same level of the lattice or go up
    public static Set<PossibleTaint> join(Set<PossibleTaint> possibleTaintsA, Set<PossibleTaint> possibleTaintsB) {
        Set<PossibleTaint> result = new HashSet<>();

        if(possibleTaintsA != null) {
            result.addAll(possibleTaintsA);
        }

        if(possibleTaintsB != null) {
            result.addAll(possibleTaintsB);
        }

        return result;
    }

    private static class TransferVisitor extends VisitorReturner {
        private final Set<PossibleTaint> oldPossibleTaints;
        private boolean inAssignment;
        private boolean tainting;
        private Set<ExpressionConfigurationConstant> possibleTaintingConfigurations;
        private Set<PossibleTaint> possibleTaintedVariables;
        private Set<PossibleTaint> killedPossibleTaintedVariables;
        private List<Expression> conditions;

        public TransferVisitor(Set<PossibleTaint> oldPossibleTaints, List<Expression> conditions) {
            this.oldPossibleTaints = oldPossibleTaints;
            this.inAssignment = false;
            this.tainting = false;
            this.possibleTaintingConfigurations = new HashSet<>();
            this.possibleTaintedVariables = new HashSet<>();
            this.killedPossibleTaintedVariables = new HashSet<>();
            this.conditions = conditions;
        }

        @Override
        public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
            Expression expression = super.visitExpressionConstantConfiguration(expressionConfigurationConstant);
            this.tainting = true;
            this.possibleTaintingConfigurations.add(expressionConfigurationConstant);

            return expression;
        }

        @Override
        public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
            Expression expression = super.visitExpressionVariable(expressionVariable);

            if(this.inAssignment) {
                for (PossibleTaint taintedVariable : this.oldPossibleTaints) {
                    if (taintedVariable.getVariable().equals(expressionVariable)) {
                        this.tainting = true;
                        this.possibleTaintingConfigurations.addAll(taintedVariable.getConfigurations());
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
                Set<ExpressionConfigurationConstant> configurations = new HashSet<>(this.possibleTaintingConfigurations);
                this.possibleTaintedVariables.add(new PossibleTaint(statementAssignment.getVariable(), configurations));
            }

            // Implicit flow
            if(!this.conditions.isEmpty()) {
                for(PossibleTaint oldTaintedVariable : this.oldPossibleTaints) {
                    if(this.conditions.contains(oldTaintedVariable.getVariable())) {
                        if(this.possibleTaintedVariables.isEmpty()) {
                            this.possibleTaintedVariables.add(new PossibleTaint(statementAssignment.getVariable(), oldTaintedVariable.getConfigurations()));
                        }
                        else {
                            for(PossibleTaint taintedVariable : this.possibleTaintedVariables) {
                                if(taintedVariable.getVariable().equals(statementAssignment.getVariable())) {
                                    taintedVariable.getConfigurations().addAll(oldTaintedVariable.getConfigurations());
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Can have the case where x<-[A,B] and now is x<-[A]. We must kill the former.
            for (PossibleTaint taintedVariable : this.oldPossibleTaints) {
                if(!this.possibleTaintedVariables.contains(taintedVariable) && taintedVariable.getVariable().equals(statementAssignment.getVariable())) {
                    this.killedPossibleTaintedVariables.add(taintedVariable);
                    break; // TODO can I have this here
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


        public Set<PossibleTaint> getPossibleTaintedVariables() { return this.possibleTaintedVariables; }

        public Set<PossibleTaint> getKilledPossibleTaintedVariables() {
            return this.killedPossibleTaintedVariables;
        }
    }

    /**
     * A possible tainted variable and a set of configurations that might have tainted it.
     */
    public static class PossibleTaint {
        private ExpressionVariable variable;
        private Set<ExpressionConfigurationConstant> configurations;

        public PossibleTaint(ExpressionVariable variable, Set<ExpressionConfigurationConstant> configurations) {
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

            PossibleTaint that = (PossibleTaint) o;

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
