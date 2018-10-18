package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.evaluation.approaches.ApproachPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.Featurewise;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FeaturewisePerformanceModelBuilder extends ApproachPerformanceModelBuilder {

    public static final String DIRECTORY = Options.DIRECTORY + "/performance-model/java/programs/featurewise";

    public FeaturewisePerformanceModelBuilder(String programName, Map<Set<String>, Double> learnedModel) {
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

        PerformanceModel pm = new FeaturewisePerformanceModel(baseTime, regionToConfigurationPerformance);
        return pm;
    }

    @Override
    public PerformanceModel readFromFile(File file) throws IOException {
        throw new UnsupportedOperationException("Have not implemented");
    }

    @Override
    public String getOutputDir() {
        return FeaturewisePerformanceModelBuilder.DIRECTORY;
    }
}
