package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise;

import edu.cmu.cs.mvelezce.evaluation.Evaluation;
import edu.cmu.cs.mvelezce.evaluation.approaches.Approach;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Featurewise extends Approach {

    public Featurewise(String programName) {
        super(programName);
    }

    public Featurewise(String programName, FeatureModel fm) {
        super(programName, fm);
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
                }
                else {
                    result.append("0");
                }

                result.append(",");
            }

            double performance = statistic.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next();
            result.append(decimalFormat.format(performance));
            result.append("\n");
        }

        String outputDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + Approach.DATA_DIR + "/"
                + Evaluation.FEATURE_WISE + Evaluation.DOT_CSV;
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

    @Override
    public Map<Set<String>, Double> getLearnedModel(List<String> options) throws IOException {
        Map<Set<String>, Double> learnedModel = new HashMap<>();

        String dataDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + Approach.DATA_DIR + "/"
                + Evaluation.FEATURE_WISE;

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

        return learnedModel;
    }

    public Set<PerformanceEntryStatistic> getFeaturewiseEntries(Set<PerformanceEntryStatistic> performanceEntries) {
        Set<Set<String>> configurations = this.getConfigurations(performanceEntries);
        Set<Set<String>> featurewiseConfigurations;

        if(this.getFm() == null) {
            featurewiseConfigurations = Featurewise.getFeaturewiseConfigurations(configurations);

        }
        else {
            featurewiseConfigurations = Featurewise.getFeaturewiseConfigurations(configurations, this.getFm());

        }

        Set<PerformanceEntryStatistic> featurewiseEntries = new HashSet<>();

        for(PerformanceEntryStatistic statistic : performanceEntries) {
            Set<String> configuration = statistic.getConfiguration();

            if(!featurewiseConfigurations.contains(configuration)) {
                continue;
            }

            featurewiseEntries.add(statistic);
        }

        return featurewiseEntries;
    }

    public static Set<Set<String>> getFeaturewiseConfigurations(Set<Set<String>> configurations, FeatureModel fm) {
        Set<String> options = Featurewise.getOptions(configurations);
        Set<Set<String>> featurewiseConfigurations = new HashSet<>();

        for(String option : options) {
            if(fm.BASE.equals(option)) {
                continue;
            }

            Set<String> configuration = new HashSet<>();
            configuration.add(option);
            configuration.add(fm.BASE);

            if(!fm.isValidProduct(configuration)) {
                continue;
            }

            featurewiseConfigurations.add(configuration);
        }

        return featurewiseConfigurations;
    }

    public static Set<Set<String>> getFeaturewiseConfigurations(Set<Set<String>> configurations) {
        Set<String> options = Featurewise.getOptions(configurations);
        Set<Set<String>> featurewiseConfigurations = new HashSet<>();

        for(String option : options) {
            Set<String> configuration = new HashSet<>();
            configuration.add(option);

            featurewiseConfigurations.add(configuration);
        }

        return featurewiseConfigurations;
    }

    public static Set<Set<String>> getFeaturewiseConfigurations(Collection<String> options) {
        Set<Set<String>> featurewiseConfigurations = new HashSet<>();

        for(String option : options) {
            Set<String> configuration = new HashSet<>();
            configuration.add(option);

            featurewiseConfigurations.add(configuration);
        }

        return featurewiseConfigurations;
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
