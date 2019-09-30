package edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.model;

import edu.cmu.cs.mvelezce.evaluation.approaches.ApproachPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PairwisePerformanceModelBuilder extends ApproachPerformanceModelBuilder {

    public static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs/pairwise";

    public PairwisePerformanceModelBuilder(String programName, Map<Set<String>, Double> learnedModel) {
        super(programName, learnedModel);
    }

    @Override
    public PerformanceModel createModel() {
        Set<String> empty = new HashSet<>();
        double baseTime = this.getLearnedModel().get(empty);
        this.getLearnedModel().remove(empty);

        Map<Region, Map<Set<String>, Double>> regionToConfigurationPerformance = new HashMap<>();
        Region programRegion = new Region.Builder("program").build();
        regionToConfigurationPerformance.put(programRegion, this.getLearnedModel());

        PerformanceModel pm = new PairwisePerformanceModel(baseTime, regionToConfigurationPerformance);
        return pm;
    }


    @Override
    public PerformanceModel readFromFile(File file) throws IOException {
        throw new UnsupportedOperationException("Have not implemented");
    }

    @Override
    public String getOutputDir() {
        return PairwisePerformanceModelBuilder.DIRECTORY;
    }
}
