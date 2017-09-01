package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.DecisionAndOptions;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseStaticAnalysis implements StaticAnalysis {

    private String programName;

    protected static final String DIRECTORY = Options.DIRECTORY + "/analysis/java/programs";

    public BaseStaticAnalysis(String programName) {
        this.programName = programName;
    }

    @Override
    public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = BaseStaticAnalysis.DIRECTORY + "/" + this.programName + Options.DOT_JSON;
        File file = new File(outputFile);

        List<DecisionAndOptions> decisionsAndOptions = new ArrayList<>();

        for(Map.Entry<JavaRegion, Set<Set<String>>> regionToOptionsSet : relevantRegionsToOptions.entrySet()) {
            DecisionAndOptions decisionAndOptions = new DecisionAndOptions(regionToOptionsSet.getKey(), regionToOptionsSet.getValue());
            decisionsAndOptions.add(decisionAndOptions);
        }

        mapper.writeValue(file, decisionsAndOptions);
    }

    @Override
    public Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<DecisionAndOptions> results = mapper.readValue(file, new TypeReference<List<DecisionAndOptions>>() {
        });
        Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

        for(DecisionAndOptions result : results) {
            regionsToOptionsSet.put(result.getRegion(), result.getOptions());
        }

        return regionsToOptionsSet;
    }

    public String getProgramName() {
        return this.programName;
    }
}
