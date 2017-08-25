package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.DecisionAndOptions;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TaintFlowAnalysis {

    private String programName;

    private static final String TAINTFLOW_OUTPUT_DIR = "/Users/mvelezce/Documents/Programming/Java/Projects/taint-analysis/src/main/resources/output";
    private static final String DIRECTORY = Options.DIRECTORY + "/analysis/java/programs";

    public TaintFlowAnalysis(String programName) {
        this.programName = programName;
    }

    public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException {
        String outputFile = TaintFlowAnalysis.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        return this.readFromFile(file);

//        List<ControlFlowResult> results = this.readTaintFlowResults();
//        Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();
//
//        for(ControlFlowResult result : results) {
//            JavaRegion region = new JavaRegion(result.getPackageName(), result.getClassName(),
//                    result.getMethodSignature(), result.getBytecodeIndex());
//
//            // TODO with the current implementation of taintflow, we only have 1 set of options
//            Set<Set<String>> optionsSet = new HashSet<>();
//            optionsSet.add(result.getOptions());
//
//            regionsToOptionsSet.put(region, optionsSet);
//        }
//
//        this.writeToFile(regionsToOptionsSet);
//        return regionsToOptionsSet;
    }

    private List<ControlFlowResult> readTaintFlowResults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File(TaintFlowAnalysis.TAINTFLOW_OUTPUT_DIR + "/" + this.programName + "/"
                + this.programName + ".json");
        List<ControlFlowResult> results = mapper.readValue(inputFile, new TypeReference<List<ControlFlowResult>>() {
        });

        return results;
    }

    private void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = TaintFlowAnalysis.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

        List<DecisionAndOptions> decisionsAndOptions = new ArrayList<>();

        for(Map.Entry<JavaRegion, Set<Set<String>>> regionToOptionsSet : relevantRegionsToOptions.entrySet()) {
            DecisionAndOptions decisionAndOptions = new DecisionAndOptions(regionToOptionsSet.getKey(), regionToOptionsSet.getValue());
            decisionsAndOptions.add(decisionAndOptions);
        }

        mapper.writeValue(file, decisionsAndOptions);
    }

    private Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<DecisionAndOptions> results = mapper.readValue(file, new TypeReference<List<DecisionAndOptions>>() {
        });
        Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

        for(DecisionAndOptions result : results) {
            regionsToOptionsSet.put(result.getRegion(), result.getOptions());
        }

        return regionsToOptionsSet;
    }

}
