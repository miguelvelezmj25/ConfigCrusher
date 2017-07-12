package edu.cmu.cs.mvelezce.tool.analysis.taint.sleep;

import edu.cmu.cs.mvelezce.sleep.ast.expression.ConstantConfigurationExpression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.Expression;
import edu.cmu.cs.mvelezce.sleep.ast.expression.VariableExpression;
import edu.cmu.cs.mvelezce.sleep.ast.statement.AssignmentStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.IfStatement;
import edu.cmu.cs.mvelezce.sleep.ast.statement.SleepStatement;
import edu.cmu.cs.mvelezce.sleep.interpreter.visitor.ReturnerVisitor;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.tool.analysis.taint.sleep.cfg.CFG;

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
        if(cfg.getSuccessors(cfg.getEntry()).size() > 1) {
            throw new IllegalArgumentException("The entry point of the CFG has more than 1 edge");
        }

        // Initialize all basic bloks to an empty set
        Map<BasicBlock, Set<PossibleTaint>> instructionsToPossibleTaints = new LinkedHashMap<>();

        Queue<BasicBlock> worklist = new LinkedList<>();
        worklist.add(cfg.getEntry());

        while (!worklist.isEmpty()) {
            BasicBlock instruction = worklist.remove();
            instructionsToPossibleTaints.put(instruction, new HashSet<>());

            List<BasicBlock> successors = cfg.getSuccessors(instruction);

            for(BasicBlock successor : successors) {
                if(successor.isSpecial()) {
                    continue;
                }

                if(!worklist.contains(successor)) {
                    worklist.add(successor);
                }
            }
        }

        // Loop through the call graph
        List<BasicBlock> entry = cfg.getSuccessors(cfg.getEntry());
        worklist.add(entry.get(0));

        while (!worklist.isEmpty()) {
            BasicBlock instruction = worklist.remove();

            List<BasicBlock> predecessors = cfg.getPredecessors(instruction);

            if(predecessors.isEmpty()) {
                throw new IllegalArgumentException("There should be at least one predecessor");
            }
            if(predecessors.size() > 2) {
                throw new IllegalArgumentException("We do not support switch statements yet");
            }

            // Join previous states
            Set<PossibleTaint> possibleTaintsBefore = new HashSet<>();

            if(predecessors.size() == 1) {
                possibleTaintsBefore = instructionsToPossibleTaints.get(predecessors.get(0));
            }
            else if(predecessors.size() == 2) {
                possibleTaintsBefore = TaintAnalysis.join(instructionsToPossibleTaints.get(predecessors.get(0)), instructionsToPossibleTaints.get(predecessors.get(1)));

            }
            possibleTaintsBefore = TaintAnalysis.join(instructionsToPossibleTaints.get(instruction), possibleTaintsBefore);

            // Calculate new state and save
            Set<PossibleTaint> possibleTaintsAfter = TaintAnalysis.transfer(instruction, possibleTaintsBefore);
            instructionsToPossibleTaints.put(instruction, possibleTaintsAfter);
            // TODO this only works because the we do not currently support cases were the state changes
            // in the last instruction of a block that we need to time ie sleep(x=A)

            // Add successors to worklist algorithm
            List<BasicBlock> successors = cfg.getSuccessors(instruction);

            if(successors.size() > 2) {
                throw new IllegalArgumentException("We do not support switch statements yet");
            }

            if(successors.size() == 0) {
                continue;
            }

            for(BasicBlock successor : successors) {
                if(successor.isSpecial()) {
                    continue;
                }

                if(!worklist.contains(successor)) {
                    worklist.add(successor);
                }

            }
        }

        instructionsToPossibleTaints.remove(cfg.getEntry());
        return instructionsToPossibleTaints;
    }

    public static Set<PossibleTaint> transfer(BasicBlock instruction, Set<PossibleTaint> oldPossibleTaints) {
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
        Map<VariableExpression, Set<ConstantConfigurationExpression>> possibleTaintedVariablesB = new HashMap<>();

        for(PossibleTaint possibleTaintB : possibleTaintsB) {
            possibleTaintedVariablesB.put(possibleTaintB.getVariable(), possibleTaintB.getConfigurations());
        }

        for(PossibleTaint possibleTaintA : possibleTaintsA) {
            Set<ConstantConfigurationExpression> hold = new HashSet<>(possibleTaintA.getConfigurations());

            if(possibleTaintedVariablesB.containsKey(possibleTaintA.getVariable())) {
                hold.addAll(possibleTaintedVariablesB.remove(possibleTaintA.getVariable()));
            }

            result.add(new PossibleTaint(possibleTaintA.getVariable(), hold));
        }

        for(Map.Entry<VariableExpression, Set<ConstantConfigurationExpression>> entry : possibleTaintedVariablesB.entrySet()) {
            result.add(new PossibleTaint(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    private static class TransferVisitor extends ReturnerVisitor {
        private final Set<PossibleTaint> oldPossibleTaints;
        private boolean inAssignment;
        private boolean inRelevantStatement;
        private boolean mergingOptions;
        private boolean tainting;
        private Set<ConstantConfigurationExpression> possibleTaintingConfigurations;
        private Set<PossibleTaint> possibleTaintedVariables;
        private Set<PossibleTaint> killedPossibleTaintedVariables;
        private List<Expression> conditions;

        public TransferVisitor(Set<PossibleTaint> oldPossibleTaints, List<Expression> conditions) {
            this.oldPossibleTaints = oldPossibleTaints;
            this.inAssignment = false;
            this.inRelevantStatement = false;
            this.mergingOptions = false;
            this.tainting = false;
            this.possibleTaintingConfigurations = new HashSet<>();
            this.possibleTaintedVariables = new HashSet<>();
            this.killedPossibleTaintedVariables = new HashSet<>();
            this.conditions = conditions;
        }

        /**
         * Signals that this is a configuration option an that it is possibly tainting something.
         *
         * @param expressionConfigurationConstant
         * @return
         */
        public Expression visitConstantConfigurationExpression(ConstantConfigurationExpression expressionConfigurationConstant) {
            Expression expression = super.visitConstantConfigurationExpression(expressionConfigurationConstant);
            this.tainting = true;
            this.possibleTaintingConfigurations.add(expressionConfigurationConstant);

            if(this.mergingOptions) {
                for(PossibleTaint taintedVariable : this.possibleTaintedVariables) {
                    taintedVariable.getConfigurations().add(expressionConfigurationConstant);
                }
            }

            return expression;
        }

        @Override
        public Expression visitVariableExpression(VariableExpression expressionVariable) {
            // We are merging options, but this is not an option
            if(this.mergingOptions) {
                return expressionVariable;
            }

            Expression expression = super.visitVariableExpression(expressionVariable);

            // Add all of the configurations for assignment
            if(this.inAssignment) {
                for(PossibleTaint taintedVariable : this.oldPossibleTaints) {
                    if(taintedVariable.getVariable().equals(expressionVariable)) {
                        this.tainting = true;
                        this.possibleTaintingConfigurations.addAll(taintedVariable.getConfigurations());
                        // Since we are checking for the variable, we can break, since there is only one variable in the assignment
                        break;
                    }
                }
            }

            // Conditionals for figuring out inner regions
            if(!this.conditions.isEmpty()) {
                for(PossibleTaint oldTaintedVariable : this.oldPossibleTaints) {
                    if(this.conditions.contains(oldTaintedVariable.getVariable())) {
                        if(this.possibleTaintedVariables.isEmpty()) {
                            Set<ConstantConfigurationExpression> implicitConfigurations = new HashSet<>(oldTaintedVariable.getConfigurations());
                            this.possibleTaintedVariables.add(new PossibleTaint(expressionVariable, implicitConfigurations));
                        }
                        else {
                            for(PossibleTaint taintedVariable : this.possibleTaintedVariables) {
                                if(taintedVariable.getVariable().equals(expressionVariable)) {
                                    taintedVariable.getConfigurations().addAll(oldTaintedVariable.getConfigurations());
                                    // Since we are checking for the variable, we can break, since there is only one variable in the assignment
                                    break;
                                }
                            }
                        }

                        // Since we are checking for the variable, we can break, since there is only one variable in the assignment
                        break;
                    }
                }

                // Merging conditional options
                this.mergingOptions = true;
                for(Expression conditionalExpression : this.conditions) {
                    conditionalExpression.accept(this);
                }
                this.mergingOptions = false;
            }

            // Killing and merging sleep and if statements
            if(this.inRelevantStatement && !this.possibleTaintedVariables.isEmpty()) {
                for(PossibleTaint oldPossibleTaint : this.oldPossibleTaints) {
                    if(!this.possibleTaintedVariables.contains(oldPossibleTaint) && oldPossibleTaint.getVariable().equals(expressionVariable)) {
                        this.killedPossibleTaintedVariables.add(oldPossibleTaint);

                        for(PossibleTaint possibleTaint : this.possibleTaintedVariables) {
                            if(possibleTaint.getVariable().equals(expressionVariable)) {
                                possibleTaint.getConfigurations().addAll(oldPossibleTaint.getConfigurations());
                                // Since we are checking for the variable, we can break, since there is only one variable in the assignment
                                break;
                            }
                        }
                        // Since we are checking for the variable, we can break, since there is only one variable in the assignment
                        break;
                    }
                }
            }

            return expression;
        }

        /**
         * Statement where tainting can occur. It can be x = X, x = a (where a is possibly tainted), and all previous
         * cases and implicit flow by being inside a relevant statement
         *
         * @param statementAssignment
         * @return
         */
        public Void visitAssignmentStatement(AssignmentStatement statementAssignment) {
            this.inAssignment = true;
            // Right side option or right side possibly tainted
            statementAssignment.getRight().accept(this);

            if(this.tainting) {
                Set<ConstantConfigurationExpression> configurations = new HashSet<>(this.possibleTaintingConfigurations);
                this.possibleTaintedVariables.add(new PossibleTaint(statementAssignment.getVariable(), configurations));
            }

            // Implicit flow
            statementAssignment.getVariable().accept(this);

            // Kill taints because of assignments.
            // This has to be here since we are checking for updates in the variable
            for(PossibleTaint taintedVariable : this.oldPossibleTaints) {
                if(!this.possibleTaintedVariables.contains(taintedVariable) && taintedVariable.getVariable().equals(statementAssignment.getVariable())) {
                    this.killedPossibleTaintedVariables.add(taintedVariable);
                    // Since we are checking for the variable, we can break, since there is only one variable in the assignment
                    break;
                }
            }

            this.inAssignment = false;
            return null;
        }

        /**
         * Relevant statement that can be inside a region and its condition could be tainted.
         *
         * @param statementIf
         * @return
         */
        public Void visitIfStatement(IfStatement statementIf) {
            this.inRelevantStatement = true;
            statementIf.getCondition().accept(this);
            this.inRelevantStatement = false;
            return null;
        }

        /**
         * Relevant statement that can be inside a region and its condition could be tainted.
         *
         * @param sleepStatement
         * @return
         */
        public Void visitSleepStatement(SleepStatement sleepStatement) {
            this.inRelevantStatement = true;
            sleepStatement.getTime().accept(this);
            this.inRelevantStatement = false;

            return null;
        }


        public Set<PossibleTaint> getPossibleTaintedVariables() {
            return this.possibleTaintedVariables;
        }

        public Set<PossibleTaint> getKilledPossibleTaintedVariables() {
            return this.killedPossibleTaintedVariables;
        }
    }

    /**
     * A possible tainted variable and a set of configurations that might have tainted it.
     */
    public static class PossibleTaint {
        private VariableExpression variable;
        private Set<ConstantConfigurationExpression> configurations;

        public PossibleTaint(VariableExpression variable, Set<ConstantConfigurationExpression> configurations) {
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
        public VariableExpression getVariable() {
            return this.variable;
        }

        /**
         * Returns the configurations that might have tainted the variable.
         *
         * @return
         */
        public Set<ConstantConfigurationExpression> getConfigurations() {
            return this.configurations;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }

            PossibleTaint that = (PossibleTaint) o;

            if(!variable.equals(that.variable)) {
                return false;
            }
            return configurations.equals(that.configurations);
        }

        @Override
        public int hashCode() {
            int result = variable.hashCode();
            result = 31 * result + configurations.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return this.variable + "<-" + this.configurations;
        }
    }

}
