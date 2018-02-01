package edu.cmu.cs.mvelezce.evaluation.approaches;

import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;

import java.util.Map;
import java.util.Set;

public abstract class ApproachPerformanceModelBuilder implements PerformanceModelBuilder {

    private String programName;
    private Map<Set<String>, Double> learnedModel;

    public ApproachPerformanceModelBuilder(String programName, Map<Set<String>, Double> learnedModel) {
        this.programName = programName;
        this.learnedModel = learnedModel;
    }

    public String getProgramName() {
        return programName;
    }

    public Map<Set<String>, Double> getLearnedModel() {
        return learnedModel;
    }
}
