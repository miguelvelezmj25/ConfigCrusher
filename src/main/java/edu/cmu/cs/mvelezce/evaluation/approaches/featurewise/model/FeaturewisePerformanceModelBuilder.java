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

    public static final String FEATUREWISE_DIR = "/featurewise";

    public FeaturewisePerformanceModelBuilder(String programName, Map<Set<String>, Double> learnedModel) {
        super(programName, learnedModel);
    }

    @Override
    public PerformanceModel createModel() {
        Set<String> empty = new HashSet<>();
        double baseTime = this.getLearnedModel().get(empty);
        this.getLearnedModel().remove(empty);

        Map<Region, Map<Set<String>, Double>> regionToConfigurationPerformance = new HashMap<>();
        Region programRegion = new Region("program");
        regionToConfigurationPerformance.put(programRegion, this.getLearnedModel());

        PerformanceModel pm = new FeaturewisePerformanceModel(baseTime, regionToConfigurationPerformance);
        return pm;
    }

    @Override
    public PerformanceModel createModel(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputDir = BasePerformanceModelBuilder.DIRECTORY + "/" + this.getProgramName()
                + FeaturewisePerformanceModelBuilder.FEATUREWISE_DIR;
        File outputFile = new File(outputDir);

        Options.checkIfDeleteResult(outputFile);

        if(outputFile.exists()) {
            Collection<File> files = FileUtils.listFiles(outputFile, null, true);

            if(files.size() != 1) {
                throw new RuntimeException("We expected to find 1 file in the directory, but that is not the case "
                        + outputFile);
            }

            return this.readFromFile(files.iterator().next());
        }

        PerformanceModel performanceModel = this.createModel();

        if(Options.checkIfSave()) {
            this.writeToFile(performanceModel);
        }

        return performanceModel;
    }

    @Override
    public void writeToFile(PerformanceModel performanceModel) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = BasePerformanceModelBuilder.DIRECTORY + "/" + this.getProgramName()
                + FeaturewisePerformanceModelBuilder.FEATUREWISE_DIR + "/" + this.getProgramName()
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        mapper.writeValue(file, performanceModel);
    }

    @Override
    public PerformanceModel readFromFile(File file) throws IOException {
        throw new UnsupportedOperationException("Have not implemented");
    }

    @Override
    public String getOutputDir() {
        throw new UnsupportedOperationException("Implement");
    }
}
