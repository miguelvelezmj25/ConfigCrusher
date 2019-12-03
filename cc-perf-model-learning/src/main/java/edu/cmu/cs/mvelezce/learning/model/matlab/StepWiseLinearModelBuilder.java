package edu.cmu.cs.mvelezce.learning.model.matlab;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;

import java.util.List;
import java.util.Set;

public class StepWiseLinearModelBuilder {

  private final String programName;
  private final List<String> options;
  private final Set<Set<String>> configs;
  private final Set<PerformanceEntry> performanceEntries;
  private final SamplingApproach samplingApproach;

  StepWiseLinearModelBuilder(
      String programName,
      List<String> options,
      Set<Set<String>> configs,
      Set<PerformanceEntry> performanceEntries,
      SamplingApproach samplingApproach) {
    this.programName = programName;
    this.options = options;
    this.configs = configs;
    this.performanceEntries = performanceEntries;
    this.samplingApproach = samplingApproach;
  }

  public void generateCSVFile() {
    throw new UnsupportedOperationException("Implement");
    //    StringBuilder result = new StringBuilder();
    //
    //    for(String option : options) {
    //      result.append(option);
    //      result.append(",");
    //    }
    //
    //    result.append("time");
    //    result.append("\n");
    //
    //    DecimalFormat decimalFormat = new DecimalFormat("#.###");
    //
    //    for(PerformanceEntryStatistic statistic : performanceEntries) {
    //      Set<String> configuration = statistic.getConfiguration();
    //
    //      for(String option : options) {
    //        if(configuration.contains(option)) {
    //          result.append("1");
    //        }
    //        else {
    //          result.append("0");
    //        }
    //
    //        result.append(",");
    //      }
    //
    //      double performance =
    // statistic.getRegionsToProcessedPerformanceHumanReadable().values().iterator().next();
    //      result.append(decimalFormat.format(performance));
    //      result.append("\n");
    //    }
    //
    //    String outputDir = Evaluation.DIRECTORY + "/" + this.getProgramName() + Approach.DATA_DIR
    // + "/"
    //            + Evaluation.FEATURE_WISE + Evaluation.DOT_CSV;
    //    File outputFile = new File(outputDir);
    //
    //    if(outputFile.exists()) {
    //      FileUtils.forceDelete(outputFile);
    //    }
    //
    //    outputFile.getParentFile().mkdirs();
    //    FileWriter writer = new FileWriter(outputFile);
    //    writer.write(result.toString());
    //    writer.flush();
    //    writer.close();
  }
}
