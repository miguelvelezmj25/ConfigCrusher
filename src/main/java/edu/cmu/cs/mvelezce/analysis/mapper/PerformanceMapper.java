package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.analysis.interpreter.Interpreter;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.analysis.visitor.VisitorReplacer;
import edu.cmu.cs.mvelezce.analysis.visitor.VisitorReturner;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementIf;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementSleep;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementTimed;

import java.util.*;

/**
 * TODO
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapper {
    private Set<Set<String>> allConfigurations;
    private Map<Set<String>, Integer> performanceMap;
    private Statement ast;
    private List<Class<? extends Statement>> relevantStatementsClasses;

    /**
     * TODO
     */
    public PerformanceMapper(Statement ast, Set<String> parameters) {
        this.allConfigurations = Helper.getConfigurations(parameters);
        this.performanceMap = new HashMap<>();
        this.ast = ast;
        relevantStatementsClasses = new ArrayList<>();
        relevantStatementsClasses.add(StatementSleep.class);
        relevantStatementsClasses.add(StatementIf.class);
    }

    /**
     * TODO
     */
    public Map<Set<String>, Integer> calculatePerformance() {
        TaintAnalysis taintAnalysis = new TaintAnalysis();
        Interpreter interpreter = new Interpreter();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(this.ast);

        Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted = taintAnalysis.analyze(cfg);
        Set<Statement> relevantStatements = new HashSet<>();
        Set<String> relevantConfigurations = this.getConfigurationsInRelevantStatements(instructionsToTainted,
                relevantStatements);
        this.updateASTToTimeRelevantStatements(relevantStatements);

        Set<String> nextConfiguration;

        while(!this.allConfigurations.isEmpty()) {
            nextConfiguration = Helper.getNextConfiguration(this.allConfigurations, relevantConfigurations);
            interpreter.evaluate(ast, nextConfiguration);

            this.pruneAndMapConfigurations(relevantConfigurations, nextConfiguration);
        }

        return this.performanceMap;
    }

    /**
     * TODO
     * @param considerParameters
     */
    protected void pruneAndMapConfigurations(Set<String> considerParameters, Set<String> lastConfiguration) {
        List<Set<String>> redundantConfigurations = new ArrayList<>();

        if(!considerParameters.isEmpty()) { // TODO check if this is needed
            for (Set<String> configuration : this.allConfigurations) {
                Set<String> possibleEquivalentConfiguration = new HashSet<>(configuration);
                possibleEquivalentConfiguration.retainAll(considerParameters);

                if(possibleEquivalentConfiguration.equals(lastConfiguration)) {
                    redundantConfigurations.add(configuration);
                }
            }

            if (!redundantConfigurations.isEmpty()) {
                for (Set<String> redundantConfiguration : redundantConfigurations) {
                    this.allConfigurations.remove(redundantConfiguration);
                }
            }
        }
    }

//    mapConfigurationToPerformanceAfterPruning //TODO

    /**
     * @param instructionsToTainted
     * @return
     */
    protected Set<String> getConfigurationsInRelevantStatements(
            Map<BasicBlock, Set<TaintAnalysis.PossibleTaint>> instructionsToTainted,
            Set<Statement> relevantStatements) {
        Set<String> taintingConfigurations = new HashSet<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.PossibleTaint>> entry : instructionsToTainted.entrySet()) {
            if(this.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                PerformanceVisitor taintingVisitor = new PerformanceVisitor(entry.getValue(), taintingConfigurations,
                        relevantStatements);
                entry.getKey().getStatement().accept(taintingVisitor);
            }
        }

        return taintingConfigurations;

    }

    protected void updateASTToTimeRelevantStatements(Set<Statement> relevantStatements) {
        AddTimedVisitor addTimedVisitor = new AddTimedVisitor(relevantStatements);
        this.ast = this.ast.accept(addTimedVisitor);
    }

    public Set<Set<String>> getAllConfigurations() { return this.allConfigurations; }

    public Map<Set<String>, Integer> getPerformanceMap() { return this.performanceMap; }


    private class PerformanceVisitor extends VisitorReturner {
        private Set<TaintAnalysis.PossibleTaint> taintedVariables;
        private Set<String> taintingConfigurations;
        private Set<Statement> relevantStatements;
        private boolean inPossibleRelevantStatement;
        private boolean relevantStatement;

        public PerformanceVisitor(Set<TaintAnalysis.PossibleTaint> taintedVariables,
                                  Set<String> taintingConfigurations, Set<Statement> relevantStatements) {
            this.taintedVariables = taintedVariables;
            this.taintingConfigurations = taintingConfigurations;
            this.relevantStatements = relevantStatements;
            this.inPossibleRelevantStatement = false;
            this.relevantStatement = false;
        }

        @Override
        public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
            if(this.inPossibleRelevantStatement) {
                this.taintingConfigurations.add(expressionConfigurationConstant.getName());
                this.relevantStatement = true;
            }


            return expressionConfigurationConstant;
        }

        @Override
        public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
            if(this.inPossibleRelevantStatement) {
                for(TaintAnalysis.PossibleTaint taintedVariable : this.taintedVariables) {
                    if(taintedVariable.getVariable().equals(expressionVariable)) {
                        this.relevantStatement = true;
                        for(ExpressionConfigurationConstant parameter : taintedVariable.getConfigurations()) {
                            this.taintingConfigurations.add(parameter.getName());
                        }
                    }
                }
            }

            return expressionVariable;
        }

        @Override
        public Void visitStatementIf(StatementIf statementIf) {
            boolean cameFromPossibleRelevantStatement = true;
            if(!this.inPossibleRelevantStatement) {
                this.inPossibleRelevantStatement = true;
                cameFromPossibleRelevantStatement = false;
            }

            boolean cameFromRelevantStatement = true;
            if(!this.relevantStatement) {
                cameFromRelevantStatement = false;
            }

            statementIf.getCondition().accept(this);

            if(this.relevantStatement) {
                this.relevantStatements.add(statementIf);
            }

            if(!cameFromRelevantStatement) {
                this.relevantStatement = false;
            }

            if(!cameFromPossibleRelevantStatement) {
                this.inPossibleRelevantStatement = false;
            }
            return null;
        }

        @Override
        public Void visitStatementSleep(StatementSleep statementSleep) {
            boolean cameFromRelevantStatement = true;
            if(!this.inPossibleRelevantStatement) {
                this.inPossibleRelevantStatement = true;
                cameFromRelevantStatement = false;
            }

            statementSleep.getTime().accept(this);

            if(this.relevantStatement) {
                this.relevantStatements.add(statementSleep);
            }

            if(!cameFromRelevantStatement) {
                this.inPossibleRelevantStatement = false;
            }
            return null;
        }
    }

    /**
     * Concrete visitor that replaces statements with StatementTimed for measuring time
     */
    private class AddTimedVisitor extends VisitorReplacer {
        private Set<Statement> relevantStatements;

        /**
         * Instantiate a {@code AddTimedVisitor}.
         *
         * @param relevantStatements
         */
        public AddTimedVisitor(Set<Statement> relevantStatements) {
            this.relevantStatements = relevantStatements;
        }

        /**
         * Replace the thenBlock of a StatementIf if the entire statement is relevant.
         *
         * @param statementIf
         * @return
         */
        @Override
        public Statement visitStatementIf(StatementIf statementIf) {
            if(this.relevantStatements.contains(statementIf)) {
                StatementTimed thenBlock = new StatementTimed(statementIf.getThenBlock());

                return new StatementIf(statementIf.getCondition(), thenBlock);
            }

            return super.visitStatementIf(statementIf);
        }

        /**
         * Replace the thenBlock of a StatementIf if the entire statement is relevant.
         *
         * @param statementSleep
         * @return
         */
        @Override
        public Statement visitStatementSleep(StatementSleep statementSleep) {
            if(relevantStatements.contains(statementSleep)) {
                return new StatementTimed(statementSleep);
            }

            return statementSleep;
        }

    }
}
