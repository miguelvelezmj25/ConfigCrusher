package edu.cmu.cs.mvelezce.evaluation.approaches.pairwise;

import edu.cmu.cs.mvelezce.Feature;
import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.evaluation.approaches.Approach;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.util.Combinations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Pairwise extends Approach {

    public Pairwise(String programName) {
        super(programName);
    }

    public Pairwise(String programName, FeatureModel fm) {
        super(programName, fm);
    }

    @Override
    public Map<Set<String>, Double> getLearnedModel(List<String> options) throws IOException {
        Map<Set<String>, Double> learnedModel = new HashMap<>();

        String dataDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + Approach.DATA_DIR + "/"
                + Evaluation.PAIR_WISE;

        File termsFile = new File(dataDir + "/" + Approach.TERMS_FILE);
        List<String> rawTerms = this.parseFile(termsFile);

        File coefsFile = new File(dataDir + "/" + Approach.COEFS_FILE);
        List<String> coefs = this.parseFile(coefsFile);

        if(rawTerms.size() != coefs.size()) {
            throw new RuntimeException("The terms and coefs files are not the same length");
        }

        File pValueFile = new File(dataDir + "/" + Approach.PVALUES_FILE);
        List<String> pValues = this.parseFile(pValueFile);
        List<String> significantTerms = this.removeTermsWithPValueGreaterThan(rawTerms, pValues, 0.05);

        List<Set<String>> terms = this.parseTerms(significantTerms, options);

        for(int i = 0; i < terms.size(); i++) {
            Set<String> term = terms.get(i);
            String rawTerm = significantTerms.get(i);
            int index = rawTerms.indexOf(rawTerm);
            String coef = coefs.get(index);
            learnedModel.put(term, Double.valueOf(coef));
        }

        Set<String> empty = new HashSet<>();

        if(!learnedModel.containsKey(empty)) {
            learnedModel.put(empty, 0.0);
        }

        return learnedModel;
    }

    @Override
    public void generateCSVData(Set<PerformanceEntryStatistic> performanceEntries, List<String> options) throws IOException {
        StringBuilder result = new StringBuilder();

        for(String option : options) {
            result.append(option);
            result.append(",");
        }

        result.append("time");
        result.append("\n");

        DecimalFormat decimalFormat = new DecimalFormat("#.###");

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            for(String option : options) {
                if(configuration.contains(option)) {
                    result.append("1");
                } else {
                    result.append("0");
                }

                result.append(",");
            }

            double performance = statistic.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next();
            result.append(decimalFormat.format(performance));
            result.append("\n");
        }

        String outputDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + Approach.DATA_DIR + "/"
                + Evaluation.PAIR_WISE + Evaluation.DOT_CSV;
        File outputFile = new File(outputDir);

        if(outputFile.exists()) {
            FileUtils.forceDelete(outputFile);
        }

        outputFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

    public Set<PerformanceEntryStatistic> getPairwiseEntries(Set<PerformanceEntryStatistic> performanceEntries) {
        Set<Set<String>> allConfigurations = this.getConfigurations(performanceEntries);
        Set<Set<String>> pairwiseConfigurations;

        if(this.getFm() == null) {
         pairwiseConfigurations = Pairwise.getPairwiseConfigurations(allConfigurations);
        }
        else {
            pairwiseConfigurations = Pairwise.getPairwiseConfigurations(allConfigurations, this.getFm());
        }

        Set<PerformanceEntryStatistic> pairwiseEntries = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            if(!pairwiseConfigurations.contains(configuration)) {
                continue;
            }

            pairwiseEntries.add(statistic);
        }

        return pairwiseEntries;
    }

    public static Set<Set<String>> getPairwiseConfigurations(Set<Set<String>> allConfigurations) {
        Set<String> options = Pairwise.getOptions(allConfigurations);
        Set<List<String>> pairs = Pairwise.getPairs(options);

        Set<Set<String>> pairwiseConfigurations = new HashSet<>();

        for(List<String> pair : pairs) {
            Set<Set<String>> pairConfigurations = Pairwise.getConfigurations(pair);
            pairwiseConfigurations.addAll(pairConfigurations);
        }

        return pairwiseConfigurations;
    }

    public static Set<Set<String>> getPairwiseConfigurations(Set<Set<String>> allConfigurations, FeatureModel fm) {
        Set<String> options = Pairwise.getOptions(allConfigurations);
        Set<List<String>> pairs = Pairwise.getPairs(options);

        Set<Set<String>> pairwiseConfigurations = new HashSet<>();

        for(List<String> pair : pairs) {
            Set<Set<String>> pairConfigurations = Pairwise.getConfigurations(pair);

            for(Set<String> config : pairConfigurations) {
                if(config.contains(fm.BASE)) {
                    continue;
                }

                config.add(fm.BASE);

                if(!fm.isValidProduct(config)) {
                    continue;
                }

                pairwiseConfigurations.add(config);
            }

        }

        return pairwiseConfigurations;
    }

    public static Set<Set<String>> getPairwiseConfigurations(Collection<String> options) {
        Set<List<String>> pairs = Pairwise.getPairs(options);

        Set<Set<String>> pairwiseConfigurations = new HashSet<>();

        for(List<String> pair : pairs) {
            Set<Set<String>> pairConfigurations = Pairwise.getConfigurations(pair);
            pairwiseConfigurations.addAll(pairConfigurations);
        }

        return pairwiseConfigurations;
    }


    private static Set<Set<String>> getConfigurations(List<String> pair) {
        Set<Set<String>> configurations = new HashSet<>();

        Set<String> configuration = new HashSet<>();
        configurations.add(configuration);

        for(int i = 1; i <= pair.size(); i++) {
            Combinations combinations = new Combinations(pair.size(), i);

            for(int[] combination : combinations) {
                configuration = new HashSet<>();

                for(int j = 0; j < combination.length; j++) {
                    int index = combination[j];
                    configuration.add(pair.get(index));
                }

                configurations.add(configuration);
            }
        }

        return configurations;
    }

    private static Set<List<String>> getPairs(Collection<String> options) {
        List<String> optionsList = new ArrayList<>(options);

        Combinations combinations = new Combinations(options.size(), 2);
        Set<List<String>> pairs = new HashSet<>();

        for(int[] combination : combinations) {
            List<String> pair = new ArrayList<>();

            for(int i = 0; i < combination.length; i++) {
                int index = combination[i];
                pair.add(optionsList.get(index));
            }

            pairs.add(pair);
        }

        return pairs;
    }

    private static Set<String> getOptions(Set<Set<String>> configurations) {
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        return options;
    }

    private Set<Set<String>> getConfigurations(Set<PerformanceEntryStatistic> performanceEntryStatistics) {
        Set<Set<String>> configurations = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntryStatistics) {
            Set<String> configuration = statistic.getConfiguration();
            configurations.add(configuration);
        }

        return configurations;
    }

}
