package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.BaseStaticAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TaintFlowAnalysis extends BaseStaticAnalysis {

    private static final String TAINTFLOW_OUTPUT_DIR = "/Users/mvelezce/Documents/Programming/Java/Projects/taintflow/src/main/resources/output";

    public TaintFlowAnalysis(String programName) {
        super(programName);
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
