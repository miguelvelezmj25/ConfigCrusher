package edu.cmu.cs.mvelezce.tool.analysis.taint.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.AssignmentStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReturnerVisitor;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFG;
import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;

import java.util.*;

/**
 * Created by miguelvelez on 2/5/17.
 */
public class TaintAnalysis {

    /**
     * Perform a taint analysis in the CFG and a map that represents, for every simpleMerging block, the variables that might
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


        Queue<BasicBlock> worklist = new LinkedList<>();
        worklist.add(entry.get(0));

        Map<BasicBlock, Set<PossibleTaint>> instructionsToPossibleTaints = new LinkedHashMap<>();

        while(!worklist.isEmpty()) {
            BasicBlock instruction = worklist.remove();
            instructionsToPossibleTaints.put(instruction, new HashSet<>());

            List<BasicBlock> successors = cfg.getSuccessors(instruction);

            for (BasicBlock successor : successors) {
                if (successor.isSpecial()) {
                    continue;
                }

                if(!worklist.contains(successor)) {
                    worklist.add(successor);
                }
            }
        }

        worklist.add(entry.get(0));

        while(!worklist.isEmpty()) {
            BasicBlock instruction = worklist.remove();
            Set<PossibleTaint> possibleTaintsBefore = instructionsToPossibleTaints.get(instruction);
            Set<PossibleTaint> possibleTaintsAfter = transfer(possibleTaintsBefore, instruction);
//            instructionsToPossibleTaints.put(instruction, possibleTaintsAfter);
            // TODO this only works because the we do not currently support cases were the state changes
            // in the last instruction of a block that we need to time ie sleep(x=A)

            List<BasicBlock> successors = cfg.getSuccessors(instruction);

            if(successors.size() > 2) {
                throw new IllegalArgumentException("We do not support switch statements yet");
            }

            if(successors.size() == 0) {
                continue;
            }

            for(BasicBlock successor : successors) {
                if (successor.isSpecial()) {
                    continue;
                }

                if(!worklist.contains(successor)) {
                    worklist.add(successor);
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
        Map<VariableExpression, Set<ConfigurationExpression>> possibleTaintedVariablesB = new HashMap<>();

        for(PossibleTaint possibleTaintB : possibleTaintsB) {
            possibleTaintedVariablesB.put(possibleTaintB.getVariable(), possibleTaintB.getConfigurations());
        }

        for(PossibleTaint possibleTaintA : possibleTaintsA) {
            Set<ConfigurationExpression> hold = new HashSet<>(possibleTaintA.getConfigurations());

            if(possibleTaintedVariablesB.containsKey(possibleTaintA.getVariable())) {
                hold.addAll(possibleTaintedVariablesB.remove(possibleTaintA.getVariable()));
            }

            result.add(new PossibleTaint(possibleTaintA.getVariable(), hold));
        }

        for(Map.Entry<VariableExpression, Set<ConfigurationExpression>> entry : possibleTaintedVariablesB.entrySet()) {
            result.add(new PossibleTaint(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    private static class TransferVisitor extends ReturnerVisitor {
        private final Set<PossibleTaint> oldPossibleTaints;
        private boolean inAssignment;
        private boolean tainting;
        private Set<ConfigurationExpression> possibleTaintingConfigurations;
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
        public Expression visitConfigurationExpression(ConfigurationExpression expressionConfigurationConstant) {
            Expression expression = super.visitConfigurationExpression(expressionConfigurationConstant);
            this.tainting = true;
            this.possibleTaintingConfigurations.add(expressionConfigurationConstant);

            return expression;
        }

        @Override
        public Expression visitVariableExpression(VariableExpression expressionVariable) {
            Expression expression = super.visitVariableExpression(expressionVariable);

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
        public Void visitAssignmentStatement(AssignmentStatement statementAssignment) {
            this.inAssignment = true;
            statementAssignment.getRight().accept(this);

            if(this.tainting) {
                Set<ConfigurationExpression> configurations = new HashSet<>(this.possibleTaintingConfigurations);
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

        public Void visitIfStatement(IfStatement statementIf) {
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
        private VariableExpression variable;
        private Set<ConfigurationExpression> configurations;

        public PossibleTaint(VariableExpression variable, Set<ConfigurationExpression> configurations) {
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
        public VariableExpression getVariable() { return this.variable; }

        /**
         * Returns the configurations that might have tainted the variable.
         *
         * @return
         */
        public Set<ConfigurationExpression> getConfigurations() { return this.configurations; }

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
