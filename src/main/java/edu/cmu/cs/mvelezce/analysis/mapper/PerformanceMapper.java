package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
import edu.cmu.cs.mvelezce.analysis.interpreter.Interpreter;
import edu.cmu.cs.mvelezce.analysis.taint.TaintAnalysis;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;
import edu.cmu.cs.mvelezce.language.ast.statement.Statement;

import java.util.*;

/**
 * TODO
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapper {
    private Set<Set<String>> allConfigurations;
    private Map<Set<String>, Integer> performanceMap;

    /**
     * TODO
     */
    public PerformanceMapper() {
        this.performanceMap = new HashMap<>();
        this.allConfigurations = null;
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
        Set<String> taintingConfigurations = this.getTaintingConfigurations(instructionsToTainted);
        Set<String> nextConfiguration = null;

        while(!this.allConfigurations.isEmpty()) {
            nextConfiguration = Helper.getNextConfiguration(this.allConfigurations, taintingConfigurations);
            System.out.println(nextConfiguration);
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
        System.out.println(lastConfiguration);
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
     * TODO
     * @param instructionsToTainted
     * @return
     */
    // TODO should be tainting configurations that affect the execution of the program or function call
    protected Set<String> getTaintingConfigurations(Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted) {
        Set<String> taintingConfigurations = new HashSet<>();

        for(Map.Entry<BasicBlock, Set<TaintAnalysis.TaintedVariable>> entry : instructionsToTainted.entrySet()) {
            for(TaintAnalysis.TaintedVariable variable : entry.getValue()) {
                for(ExpressionConfigurationConstant parameter : variable.getConfigurations()) {
                    taintingConfigurations.add(parameter.getName());
                }
            }
        }

        return taintingConfigurations;

    }


    public Set<Set<String>> getAllConfigurations() { return this.allConfigurations; }

    public Map<Set<String>, Integer> getPerformanceMap() { return this.performanceMap; }

    public void setAllConfigurations(Set<Set<String>> allConfigurations) { this.allConfigurations = allConfigurations; }
}
