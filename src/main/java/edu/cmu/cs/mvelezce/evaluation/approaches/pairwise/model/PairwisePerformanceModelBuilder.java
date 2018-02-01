package edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.BasePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.performance.model.builder.PerformanceModelBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PairwisePerformanceModelBuilder implements PerformanceModelBuilder {

    public static final String PAIRWISE_DIR = "/pairwise";

    private String programName;
    private Map<Set<String>, Double> learnedModel;

    public PairwisePerformanceModelBuilder(String programName, Map<Set<String>, Double> learnedModel) {
        this.programName = programName;
        this.learnedModel = learnedModel;
    }

    @Override
    public PerformanceModel createModel() {
        Set<String> empty = new HashSet<>();
        double baseTime = this.learnedModel.get(empty);
        this.learnedModel.remove(empty);

        Map<Region, Map<Set<String>, Double>> regionToConfigurationPerformance = new HashMap<>();
        Region programRegion = new Region("program");
        regionToConfigurationPerformance.put(programRegion, this.learnedModel);

        PerformanceModel pm = new PairwisePerformanceModel(baseTime, regionToConfigurationPerformance);
        return pm;
    }

//    private Map<Set<String>, Double> parseOutput(String output) {
////        String[] parsedOutput = output.split("\n");
////
////        List<String> header = new ArrayList<>();
////        List<String> coefficients = new ArrayList<>();
////
////        for(int i = 0; i < parsedOutput.length; i++) {
////            String[] rawHeader = parsedOutput[i].split(" ");
////            List<String> rowHeaders = this.getDataFromOutput(rawHeader);
////            header.addAll(rowHeaders);
////
////            i++;
////
////            String[] rawCoefficients = parsedOutput[i].split(" ");
////            List<String> rowCoefficients = this.getDataFromOutput(rawCoefficients);
////            coefficients.addAll(rowCoefficients);
////        }
////
////        Map<Set<String>, Double> configurationToPerformance = new HashMap<>();
////
////        for(int i = 0; i < header.size(); i++) {
////            String[] terms = header.get(i).split(":");
////            String rawCoefficient = coefficients.get(i);
////            double coefficient = 0.0;
////
////            if(!rawCoefficient.equals(Pairwise.NA)) {
////                coefficient = Double.parseDouble(rawCoefficient);
////            }
////
////            Set<String> configuration = new HashSet<>();
////
////            for(int j = 0; j < terms.length; j++) {
////                String term = terms[j];
////
////                if(!term.isEmpty()) {
////                    configuration.add(term);
////                }
////            }
////
////            configurationToPerformance.put(configuration, coefficient);
////        }
////
////        return configurationToPerformance;
//        throw new UnsupportedOperationException("Implement");
//    }
//
//    private List<String> getDataFromOutput(String[] array) {
//        List<String> data = new ArrayList<>();
//
//        for(int i = 0; i < array.length; i++) {
//            String element = array[i].trim();
//
//            if(!element.equals(" ") && element.length() > 0) {
//                data.add(element);
//            }
//        }
//
//        return data;
//    }

    @Override
    public PerformanceModel createModel(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputDir = BasePerformanceModelBuilder.DIRECTORY + "/" + this.programName
                + PairwisePerformanceModelBuilder.PAIRWISE_DIR;
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
                + PairwisePerformanceModelBuilder.PAIRWISE_DIR + "/" + this.programName
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        mapper.writeValue(file, performanceModel);
    }

    @Override
    public PerformanceModel readFromFile(File file) throws IOException {
        throw new UnsupportedOperationException("Have not implemented");
    }
}
