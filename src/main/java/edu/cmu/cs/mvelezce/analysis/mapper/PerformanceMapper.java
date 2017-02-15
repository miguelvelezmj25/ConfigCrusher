package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.analysis.interpreter.Interpreter;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
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
    public Map<Set<String>, Integer> computeAll() {
        TaintAnalysis taintAnalysis = new TaintAnalysis();
        Interpreter interpreter = new Interpreter();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(this.ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = taintAnalysis.analyze(cfg);
        Set<String> relevantConfigurations = this.getConfigurationsInRelevantStatements(instructionsToTainted);
        Set<String> nextConfiguration;

        while(!this.allConfigurations.isEmpty()) {
            nextConfiguration = Helper.getNextConfiguration(this.allConfigurations, relevantConfigurations);
            // TODO measure sleeps at each chunck of code that does not branch.
            // TODO record the configuration and the time it took at relevant chuncks of code
            interpreter.evaluate(ast, nextConfiguration);

            this.pruneConfigurations(relevantConfigurations, nextConfiguration);
        }

        return this.performanceMap;
    }

    /**
     * TODO
     * @param considerParameters
     */
    protected void pruneConfigurations(Set<String> considerParameters, Set<String> lastConfiguration) {
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
     * TODO it uses tainted variables, but can also be configurations used in relevant statements
     * @param instructionsToTainted
     * @return
     */
    // TODO should be tainting configurations that affect the execution of the program or function call
    protected Set<String> getConfigurationsInRelevantStatements(Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted) {
        Set<String> taintingConfigurations = new HashSet<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.TaintedVariable>> entry : instructionsToTainted.entrySet()) {
            if(this.relevantStatementsClasses.contains(entry.getKey().getStatement().getClass())) {
                PerformanceVisitor taintingVisitor = new PerformanceVisitor(entry.getValue(), taintingConfigurations);
                entry.getKey().getStatement().accept(taintingVisitor);
            }
        }

        return taintingConfigurations;

    }

    public Set<Set<String>> getAllConfigurations() { return this.allConfigurations; }

    public Map<Set<String>, Integer> getPerformanceMap() { return this.performanceMap; }

    private class PerformanceVisitor extends VisitorReturner {
        private Set<TaintAnalysis.TaintedVariable> taintedVariables;
        private Set<String> taintingConfigurations;
        private boolean inPossibleRelevantStatement;
        private boolean relevantStatement;

        public PerformanceVisitor(Set<TaintAnalysis.TaintedVariable> taintedVariables, Set<String> taintingConfigurations) {
            this.taintedVariables = taintedVariables;
            this.taintingConfigurations = taintingConfigurations;
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
                for(TaintAnalysis.TaintedVariable taintedVariable : this.taintedVariables) {
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
            boolean cameFromRelevantStatement = true;
            if(!this.inPossibleRelevantStatement) {
                this.inPossibleRelevantStatement = true;
                cameFromRelevantStatement = false;
            }

            statementIf.getCondition().accept(this);

            if(!cameFromRelevantStatement) {
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

            if(!cameFromRelevantStatement) {
                this.inPossibleRelevantStatement = false;
            }
            return null;
        }
    }

    private class AddTimedVisitor extends VisitorReturner {
        private Set<Statement> relevantStatements;

        public AddTimedVisitor(Set<Statement> relevantStatements) {
            this.relevantStatements = relevantStatements;
        }

        @Override
        public Void visitStatementIf(StatementIf statementIf) {
            if(this.relevantStatements.contains(statementIf)) {
                StatementTimed then = new StatementTimed(statementIf.getThenBlock());
            }
            return null;
        }

        @Override
        public Void visitStatementSleep(StatementSleep statementSleep) {

            return null;
        }

    }
}
