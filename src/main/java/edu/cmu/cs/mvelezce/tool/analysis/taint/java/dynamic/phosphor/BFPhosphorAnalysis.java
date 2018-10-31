package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class BFPhosphorAnalysis extends PhosphorAnalysis {

  BFPhosphorAnalysis(String programName, Set<String> options) {
    super(programName, options, new HashSet<>());
  }

  public BFPhosphorAnalysis(String programName) {
    super(programName);
  }

  @Override
  protected void runDynamicAnalysis() throws IOException, InterruptedException {

    Set<Set<String>> configs = Helper.getConfigurations(this.getOptions());

    for (Set<String> config : configs) {
      // ST := run_taint_analysis(P’, c)
      this.runPhosphorAnalysis(config);
      Pair<Map<String, Set<String>>, Map<String, Set<String>>> sinksToTaintsResults = this
          .analyzePhosphorResults();

      // CFA := get_constraints_from_analysis(ST)
      this.getConstraintsFromAnalysis(sinksToTaintsResults, config);
    }
  }

  @Override
  public String outputDir() {
    return BaseDynamicAnalysis.DIRECTORY + "/" + this.getProgramName() + "/bf/";
  }
}
