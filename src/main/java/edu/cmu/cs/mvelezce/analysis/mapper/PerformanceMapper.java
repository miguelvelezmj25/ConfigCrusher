package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.analysis.interpreter.Interpreter;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.analysis.visitor.BaseVisitor;
import edu.cmu.cs.mvelezce.language.ast.expression.Expression;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionVariable;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementIf;
import edu.cmu.cs.mvelezce.language.ast.statement.StatementSleep;

import java.util.*;

/**
 * TODO
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapper {
    private Set<Set<String>> allConfigurations;
    private Map<Set<String>, Integer> performanceMap;
    private List<Class<? extends Statement>> relevantStatements;

    /**
     * TODO
     */
    public PerformanceMapper() {
        this.performanceMap = new HashMap<>();
        this.allConfigurations = null;
        relevantStatements = new ArrayList<>();
        relevantStatements.add(StatementSleep.class);
        relevantStatements.add(StatementIf.class);
    }

    /**
     * TODO
     */
    public Map<Set<String>, Integer> computeAll(Statement ast, Set<String> parameters) {
        this.allConfigurations = Helper.getConfigurations(parameters);
        TaintAnalysis taintAnalysis = new TaintAnalysis();
        Interpreter interpreter = new Interpreter();

        CFGBuilder builder = new CFGBuilder();
        CFG cfg = builder.buildCFG(ast);

        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = taintAnalysis.analyze(cfg);
        Set<String> taintingConfigurations = this.getConfigurationsInRelevantStatements(instructionsToTainted);
        Set<String> nextConfiguration = null;

        while(!this.allConfigurations.isEmpty()) {
            nextConfiguration = Helper.getNextConfiguration(this.allConfigurations, taintingConfigurations);
            System.out.println(nextConfiguration);
            // TODO measure sleeps at each chunck of code that does not branch.
            // TODO record the configuration and the time it took at relevant chuncks of code
            interpreter.evaluate(ast, nextConfiguration);

            this.pruneConfigurations(taintingConfigurations, nextConfiguration);
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
            if(this.relevantStatements.contains(entry.getKey().getStatement().getClass())) {
                TaintingVisitor taintingVisitor = new TaintingVisitor(entry.getValue(), taintingConfigurations);
                entry.getKey().getStatement().accept(taintingVisitor);
            }
        }

        return taintingConfigurations;

    }


    public Set<Set<String>> getAllConfigurations() { return this.allConfigurations; }

    public Map<Set<String>, Integer> getPerformanceMap() { return this.performanceMap; }

    public void setAllConfigurations(Set<Set<String>> allConfigurations) { this.allConfigurations = allConfigurations; }

    private class TaintingVisitor extends BaseVisitor {
        private Set<TaintAnalysis.TaintedVariable> taintedVariables;
        private Set<String> taintingConfigurations;
        private boolean inRelevantStatement;

        public TaintingVisitor(Set<TaintAnalysis.TaintedVariable> taintedVariables, Set<String> taintingConfigurations) {
            this.taintedVariables = taintedVariables;
            this.taintingConfigurations = taintingConfigurations;
            this.inRelevantStatement = false;
        }

        @Override
        public Expression visitExpressionConstantConfiguration(ExpressionConfigurationConstant expressionConfigurationConstant) {
            Expression expression = super.visitExpressionConstantConfiguration(expressionConfigurationConstant);

            if(this.inRelevantStatement) {
                this.taintingConfigurations.add(expressionConfigurationConstant.getName());
            }


            return expression;
        }

        @Override
        public Expression visitExpressionVariable(ExpressionVariable expressionVariable) {
            Expression expression = super.visitExpressionVariable(expressionVariable);

            if(this.inRelevantStatement) {
                for(TaintAnalysis.TaintedVariable taintedVariable : this.taintedVariables) {
                    if (taintedVariable.getVariable().equals(expressionVariable)) {
                        for (ExpressionConfigurationConstant parameter : taintedVariable.getConfigurations()) {
                            this.taintingConfigurations.add(parameter.getName());
                        }
                    }
                }
            }

            return expression;
        }

        @Override
        public void visitStatementIf(StatementIf statementIf) {
            boolean cameFromRelevantStatement = true;
            if(!this.inRelevantStatement) {
                this.inRelevantStatement = true;
                cameFromRelevantStatement = false;
            }
            statementIf.getCondition().accept(this);

            if(!cameFromRelevantStatement) {
                this.inRelevantStatement = false;
            }
        }

        @Override
        public void visitStatementSleep(StatementSleep statementSleep) {
            boolean cameFromRelevantStatement = true;
            if(!this.inRelevantStatement) {
                this.inRelevantStatement = true;
                cameFromRelevantStatement = false;
            }
            statementSleep.getTime().accept(this);

            if(!cameFromRelevantStatement) {
                this.inRelevantStatement = false;
            }
        }
    }
}
