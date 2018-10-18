package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.DecisionAndOptions;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseStaticAnalysis implements StaticAnalysis {

    protected static final String DIRECTORY = Options.DIRECTORY + "/analysis/java/programs";
    private String programName;

    public BaseStaticAnalysis(String programName) {
        this.programName = programName;
    }

    @Override
    public Map<JavaRegion, Set<Set<String>>> analyze(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputFile = BaseStaticAnalysis.DIRECTORY + "/" + this.programName;
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            Collection<File> files = FileUtils.listFiles(file, null, true);

            if(files.size() != 1) {
                throw new RuntimeException("We expected to find 1 file in the directory, but that is not the case "
                        + outputFile);
            }

            return this.readFromFile(files.iterator().next());
        }

        Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = this.analyze();

        if(Options.checkIfSave()) {
            this.writeToFile(regionsToOptionsSet);
        }

        return regionsToOptionsSet;
    }

    @Override
    public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = BaseStaticAnalysis.DIRECTORY + "/" + this.programName + "/" + this.programName
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

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

    // TODO should this be static helper method?
    public Map<Region, Set<Set<String>>> transform(Map<? extends Region, Set<Set<String>>> regionsToOptionSet) {
        Map<Region, Set<Set<String>>> result = new HashMap<>();

        for(Map.Entry<? extends Region, Set<Set<String>>> entry : regionsToOptionSet.entrySet()) {
            Region region = new Region.Builder(entry.getKey().getRegionID()).build();
            result.put(region, entry.getValue());
        }

        return result;
    }


    public String getProgramName() {
        return this.programName;
    }
}
