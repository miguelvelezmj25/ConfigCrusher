package edu.cmu.cs.mvelezce.eval.java.blackbox.perfmodel.bf;

import edu.cmu.cs.mvelezce.eval.java.blackbox.perfmodel.BlackBoxPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BruteForcePerformanceModelBuilder extends BlackBoxPerformanceModelBuilder {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/model/java/bf/programs";

  public BruteForcePerformanceModelBuilder(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  BruteForcePerformanceModelBuilder(
      String programName, List<String> options, Set<PerformanceEntry> performanceEntries) {
    super(programName, options, performanceEntries);
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR;
  }
}
