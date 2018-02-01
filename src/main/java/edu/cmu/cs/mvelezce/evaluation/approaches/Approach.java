package edu.cmu.cs.mvelezce.evaluation.approaches;

import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public abstract class Approach {

    public static final String DATA_DIR = "/data";
    public static final String COEFS_FILE = "coefs.txt";
    public static final String TERMS_FILE = "terms.txt";
    public static final String FORMULA_FILE = "formula.txt";
    public static final String INTERCEPT = "(Intercept)";
    public static final String TERM_PREFIX = "x";
    public static final String TERM_DELIMETER = ":";

    private String programName;

    public Approach(String programName) {
        this.programName = programName;
    }

    public abstract void generateCSVData(Set<PerformanceEntryStatistic> performanceEntries) throws IOException;

    public abstract Map<Set<String>, Double> getLearnedModel(List<String> options) throws IOException;

    public String getProgramName() {
        return programName;
    }

    protected List<Set<String>> parseTerms(List<String> rawTerms, List<String> options) {
        List<Set<String>> result = new ArrayList<>();

        for(String raw : rawTerms) {
            String[] terms = raw.split(Approach.TERM_DELIMETER);
            Set<String> optionsInTerms = new HashSet<>();

            for(int i = 0; i < terms.length; i++) {
                String rawTerm = terms[i];

                if(rawTerm.equals(Approach.INTERCEPT)) {
                    continue;
                }

                String termIndexString = rawTerm.substring(1);
                Integer termIndex = Integer.valueOf(termIndexString);
                String term = options.get(termIndex - 1);
                optionsInTerms.add(term);
            }

            result.add(optionsInTerms);
        }

        return result;
    }

    protected List<String> parseFile(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = "";

        List<String> lines = new ArrayList<>();

        while((line = reader.readLine()) != null) {
            if(line.isEmpty()) {
                continue;
            }

            lines.add(line.trim());
        }

        return lines;
    }

    public List<String> removeTermsNoInFormula(List<String> rawTerms, String formula) {
        List<String> res = new ArrayList<>();
        String[] formulaTerms = formula.split(" ");

        for(int i = 0; i < formulaTerms.length; i++) {
            formulaTerms[i] = formulaTerms[i].trim();
        }

        for(int i = 0; i < formulaTerms.length; i++) {
            formulaTerms[i] = formulaTerms[i].replace("*", ":");
        }

        Set<String> processedFormulaTerms = new HashSet<>(Arrays.asList(formulaTerms));

        for(String rawTerm : rawTerms) {
            if(processedFormulaTerms.contains(rawTerm)) {
                res.add(rawTerm);
            }
        }

        res.add(Approach.INTERCEPT);

        return res;
    }
}
