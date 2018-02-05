package edu.cmu.cs.mvelezce.evaluation.approaches;

import edu.cmu.cs.mvelezce.tool.performance.model.builder.BasePerformanceModelBuilder;

import java.util.Map;
import java.util.Set;

public abstract class ApproachPerformanceModelBuilder extends BasePerformanceModelBuilder {

    private Map<Set<String>, Double> learnedModel;

    public ApproachPerformanceModelBuilder(String programName, Map<Set<String>, Double> learnedModel) {
        super(programName);

        this.learnedModel = learnedModel;
    }

    public Map<Set<String>, Double> getLearnedModel() {
        return learnedModel;
    }
}
