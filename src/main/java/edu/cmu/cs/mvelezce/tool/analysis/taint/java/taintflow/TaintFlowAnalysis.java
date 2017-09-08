package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.BaseStaticAnalysis;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TaintFlowAnalysis extends BaseStaticAnalysis {

    private static final String TAINTFLOW_OUTPUT_DIR = "/Users/mvelezce/Documents/Programming/Java/Projects/taintflow/src/main/resources/output";

    public TaintFlowAnalysis(String programName) {
        super(programName);
    }

    @Override
    public Map<JavaRegion, Set<Set<String>>> analyze(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputFile = BaseStaticAnalysis.DIRECTORY + "/" + this.getProgramName();
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
    public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException {
        List<ControlFlowResult> results = this.readTaintFlowResults();
        Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

        for(ControlFlowResult result : results) {
            JavaRegion region = new JavaRegion(result.getPackageName(), result.getClassName(),
                    result.getMethodSignature(), result.getBytecodeIndex());

            // TODO with the current implementation of taintflow, we only have 1 set of options
            Set<Set<String>> optionsSet = new HashSet<>();
            optionsSet.add(result.getOptions());

            regionsToOptionsSet.put(region, optionsSet);
        }

        return regionsToOptionsSet;
    }

    private List<ControlFlowResult> readTaintFlowResults() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File inputFile = new File(TaintFlowAnalysis.TAINTFLOW_OUTPUT_DIR + "/" + this.getProgramName() + "/"
                + this.getProgramName() + ".json");
        List<ControlFlowResult> results = mapper.readValue(inputFile, new TypeReference<List<ControlFlowResult>>() {
        });

        return results;
    }

}
