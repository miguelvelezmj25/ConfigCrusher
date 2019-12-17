package edu.cmu.cs.mvelezce.exhaustive.builder.bf;

import edu.cmu.cs.mvelezce.exhaustive.builder.ExhaustiveModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BruteForceExhaustiveModelBuilder extends ExhaustiveModelBuilder {

  private static final String OUTPUT_DIR =
      "../cc-perf-model-exhaustive/" + Options.DIRECTORY + "/model/java/programs/bf";

  public BruteForceExhaustiveModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  BruteForceExhaustiveModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}
