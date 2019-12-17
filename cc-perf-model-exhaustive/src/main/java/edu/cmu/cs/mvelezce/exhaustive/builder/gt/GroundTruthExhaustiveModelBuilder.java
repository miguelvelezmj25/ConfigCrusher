package edu.cmu.cs.mvelezce.exhaustive.builder.gt;

import edu.cmu.cs.mvelezce.exhaustive.builder.ExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroundTruthExhaustiveModelBuilder extends ExhaustiveModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-perf-model-exhaustive/" + Options.DIRECTORY + "/model/java/programs/gt";

  public GroundTruthExhaustiveModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  GroundTruthExhaustiveModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}
