package edu.cmu.cs.mvelezce.eval.java.accuracy.partition;

import edu.cmu.cs.mvelezce.eval.java.accuracy.AccuracyEvaluation;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.ArrayList;
import java.util.List;

public class AccuracyPartitionEvaluation extends AccuracyEvaluation<Partition> {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/partition";

  public AccuracyPartitionEvaluation(String programName) {
    this(programName, new ArrayList<>());
  }

  public AccuracyPartitionEvaluation(String programName, List<String> options) {
    super(programName, options);
  }

  @Override
  protected String getOutputDir() {
    return OUTPUT_DIR;
  }
}
