package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.Helper;
import edu.cmu.cs.mvelezce.language.ast.expression.ExpressionConfigurationConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TODO
 * Created by miguelvelez on 2/11/17.
 */
public class PerformanceMapper {
    private Set<Set<String>> allConfigurations;

    /**
     * TODO
     * @param parameters
     */
    public PerformanceMapper(Set<String> parameters) {
        this.allConfigurations = Helper.getConfigurations(parameters);
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
     * @param considerParameters
     */
    protected void pruneConfigurations(Set<ExpressionConfigurationConstant> considerParameters) {
        List<Set<String>> redundantConfigurations = new ArrayList<>();

        if(!considerParameters.isEmpty()) {
            for (Set<String> configuration : allConfigurations) {
                boolean contains = false;

                for (ExpressionConfigurationConstant considerParameter : considerParameters) {
                    if (configuration.contains(considerParameter.getName())) {
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

//    protected void getTaintingConfigurations(taintedValueSet)


    public Set<Set<String>> getAllConfigurations() { return this.allConfigurations; }
}
