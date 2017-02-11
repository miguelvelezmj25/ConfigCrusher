package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.analysis.cfg.BasicBlock;
import edu.cmu.cs.mvelezce.analysis.cfg.CFG;
import edu.cmu.cs.mvelezce.analysis.cfg.CFGBuilder;
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
    private Statement ast;
    private CFG cfg;
    private TaintAnalysis taintAnalysis;

    /**
     * TODO
     * @param parameters
     */
    public PerformanceMapper(Statement ast, Set<String> parameters) {
        this.allConfigurations = Helper.getConfigurations(parameters);
        this.ast = ast;

        CFGBuilder builder = new CFGBuilder();
        this.cfg = builder.buildCFG(ast);

        this.taintAnalysis = new TaintAnalysis(this.cfg);
    }

    /**
     * TODO
     */
    private void computeAll() {
        // TODO
        // TODO if nothing is tainted, pick configurations at random
    }

    /**
     * TODO
     */
    public void evaluate() {
        Map<BasicBlock, Set<TaintAnalysis.TaintedVariable>> instructionsToTainted = this.taintAnalysis.analyze();
        this.getTaintingConfigurations(instructionsToTainted);
    }

    /**
     * TODO
     * @param considerParameters
     */
    protected void pruneConfigurations(Set<String> considerParameters) {
        List<Set<String>> redundantConfigurations = new ArrayList<>();

        if(!considerParameters.isEmpty()) {
            for (Set<String> configuration : allConfigurations) {
                boolean contains = false;

                for (String considerParameter : considerParameters) {
                    if (configuration.contains(considerParameter)) {
                        contains = true;
                        break;
                    } else {
                        contains = false;
                    }
                }

                if (!contains) {
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

    public Statement getAst() { return this.ast; }

    public CFG getCfg() { return this.cfg; }

    public TaintAnalysis getTaintAnalysis() { return this.taintAnalysis; }
}
