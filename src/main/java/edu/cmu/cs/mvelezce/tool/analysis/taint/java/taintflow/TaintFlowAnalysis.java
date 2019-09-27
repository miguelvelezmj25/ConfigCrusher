package edu.cmu.cs.mvelezce.tool.analysis.taint.java.taintflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.BaseStaticAnalysis;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaintFlowAnalysis extends BaseStaticAnalysis {

  private static final String TAINTFLOW_OUTPUT_DIR = BaseAdapter.USER_HOME
      + "/Documents/Programming/Java/Projects/taintflow/src/main/resources/output";

  public TaintFlowAnalysis(String programName) {
    super(programName);
  }

  @Override
  public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException {
    List<ControlFlowResult> results = this.readTaintFlowResults();
    Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

    for (ControlFlowResult result : results) {
      JavaRegion region = new JavaRegion.Builder(result.getPackageName(), result.getClassName(),
          result.getMethodSignature()).startBytecodeIndex(result.getBytecodeIndex()).build();

      // TODO with the current implementation of taintflow, we only have 1 set of options
      Set<Set<String>> optionsSet = new HashSet<>();
      optionsSet.add(result.getOptions());

      regionsToOptionsSet.put(region, optionsSet);
    }

    return regionsToOptionsSet;
  }

  private List<ControlFlowResult> readTaintFlowResults() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File inputFile = new File(
        TaintFlowAnalysis.TAINTFLOW_OUTPUT_DIR + "/" + this.getProgramName() + "/"
            + this.getProgramName() + ".json");

    return mapper
        .readValue(inputFile, new TypeReference<List<ControlFlowResult>>() {
        });
  }

}
