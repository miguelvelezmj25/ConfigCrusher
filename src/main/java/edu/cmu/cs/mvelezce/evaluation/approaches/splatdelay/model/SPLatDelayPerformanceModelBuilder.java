package edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.SPLatDelay;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SPLatDelayPerformanceModelBuilder implements PerformanceModelBuilder {

    public static final String SPLAT_DIR = "/splat";

    private String programName;
    private String output;

     public SPLatDelayPerformanceModelBuilder(String programName, String output) {
         this.programName = programName;
         this.output = output;
     }

    @Override
    public PerformanceModel createModel() {
        Map<Set<String>, Double> configurationToPerformance = this.parseOutput(this.output);

        Set<String> intercept = new HashSet<>();
        double baseTime = 0.0;

        for(Map.Entry<Set<String>, Double> entry : configurationToPerformance.entrySet()) {
            if(entry.getKey().contains(SPLatDelay.INTERCEPT)) {
                intercept = entry.getKey();
                baseTime = entry.getValue();

                break;
            }
        }

        configurationToPerformance.remove(intercept);

        Map<Region, Map<Set<String>, Double>> regionToConfigurationPerformance = new HashMap<>();
        Region programRegion = new Region.Builder("program").build();
        regionToConfigurationPerformance.put(programRegion, configurationToPerformance);

        PerformanceModel pm = new SPLatDelayPerformanceModel(baseTime, regionToConfigurationPerformance);
        return pm;
    }

    private Map<Set<String>, Double> parseOutput(String output) {
        String[] parsedOutput = output.split("\n");

        List<String> header = new ArrayList<>();
        List<String> coefficients = new ArrayList<>();

        for(int i = 0; i < parsedOutput.length; i++) {
            String[] rawHeader = parsedOutput[i].split(" ");
            List<String> rowHeaders = this.getDataFromOutput(rawHeader);
            header.addAll(rowHeaders);

            i++;

            String[] rawCoefficients = parsedOutput[i].split(" ");
            List<String> rowCoefficients = this.getDataFromOutput(rawCoefficients);
            coefficients.addAll(rowCoefficients);
        }

        Map<Set<String>, Double> configurationToPerformance = new HashMap<>();

        for(int i = 0; i < header.size(); i++) {
            String term = header.get(i);
            String rawCoefficient = coefficients.get(i);
            double coefficient = 0.0;

//            if(!rawCoefficient.equals(SPLatDelay.NA)) {
//                coefficient = Double.parseDouble(rawCoefficient);
//            }

            Set<String> configuration = new HashSet<>();
            configuration.add(term);

            configurationToPerformance.put(configuration, coefficient);
        }

        return configurationToPerformance;
    }

    private List<String> getDataFromOutput(String[] array) {
        List<String> data = new ArrayList<>();

        for(int i = 0; i < array.length; i++) {
            String element = array[i].trim();

            if(!element.equals(" ") && element.length() > 0) {
                data.add(element);
            }
        }

        return data;
    }

    @Override
    public PerformanceModel createModel(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputDir = BasePerformanceModelBuilder.DIRECTORY + "/" + this.programName
                + SPLatDelayPerformanceModelBuilder.SPLAT_DIR;
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
        String outputFile = BasePerformanceModelBuilder.DIRECTORY + "/" + this.programName
                + SPLatDelayPerformanceModelBuilder.SPLAT_DIR + "/" + this.programName
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
