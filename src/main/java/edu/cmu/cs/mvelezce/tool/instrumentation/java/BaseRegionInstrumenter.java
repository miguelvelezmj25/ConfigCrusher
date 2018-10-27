package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.DecisionAndOptions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.graph.MethodBlock;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class BaseRegionInstrumenter extends BaseInstrumenter {

    public static final String DIRECTORY = Options.DIRECTORY + "/instrumentation/java/programs";

    private Map<JavaRegion, Set<Set<String>>> regionsToOptionSet;

    public BaseRegionInstrumenter(String programName, String classDir, Map<JavaRegion, Set<Set<String>>> regionsToOptionSet) {
        super(programName, null, classDir);
        this.regionsToOptionSet = regionsToOptionSet;
    }

    public BaseRegionInstrumenter(String programName) {
        this(programName, null, new HashMap<>());
    }

    @Override
    public void instrument(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException, InterruptedException {
        Options.getCommandLine(args);

        File outputFile = new File(BaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName());
        Options.checkIfDeleteResult(outputFile);

        if(outputFile.exists()) {
            Collection<File> files = FileUtils.listFiles(outputFile, new String[]{"json"}, false);

            if(files.size() != 1) {
                throw new RuntimeException("We expected to find 1 file in the directory, but that is not the case "
                        + outputFile);
            }

            this.regionsToOptionSet = this.readFromFile(files.iterator().next());

            return;
        }

        if(Options.checkIfDeleteResult()) {
            this.compileFromSource();
        }

        if(Options.checkIfSave()) {
            this.instrument();
            this.writeToFile(this.regionsToOptionSet);
        }
    }

    public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String outputFile = BaseRegionInstrumenter.DIRECTORY + "/" + this.getProgramName() + "/" + this.getProgramName()
                + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        List<DecisionAndOptions> decisionsAndOptions = new ArrayList<>();

        for(Map.Entry<JavaRegion, Set<Set<String>>> regionToOptionsSet : relevantRegionsToOptions.entrySet()) {
            JavaRegion oldRegion = regionToOptionsSet.getKey();

            Set<String> endBlocksIDs = new HashSet<>();

            for(MethodBlock block : oldRegion.getEndMethodBlocks()) {
                endBlocksIDs.add(block.getID());
            }

            JavaRegion newRegion = new JavaRegion(oldRegion.getRegionID(), oldRegion.getRegionPackage(),
                    oldRegion.getRegionClass(), oldRegion.getRegionMethod(), oldRegion.getStartBytecodeIndex(),
                    oldRegion.getStartMethodBlock().getID(), endBlocksIDs);
            DecisionAndOptions decisionAndOptions = new DecisionAndOptions(newRegion, regionToOptionsSet.getValue());
            decisionsAndOptions.add(decisionAndOptions);
        }

        mapper.writeValue(file, decisionsAndOptions);
    }

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


    public Map<JavaRegion, Set<Set<String>>> getRegionsToOptionSet() {
        return this.regionsToOptionSet;
    }
}
