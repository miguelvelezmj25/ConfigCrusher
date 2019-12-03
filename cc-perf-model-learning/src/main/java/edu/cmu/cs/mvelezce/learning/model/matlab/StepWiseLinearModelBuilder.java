package edu.cmu.cs.mvelezce.learning.model.matlab;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

class StepWiseLinearModelBuilder {

  private static final String OUTPUT_DIR = "../cc-perf-model-learning/" + Options.DIRECTORY;

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

  public void generateCSVFile() throws IOException {
    StringBuilder result = new StringBuilder();

    for (String option : this.options) {
      result.append(option);
      result.append(",");
    }

    result.append("time");
    result.append("\n");

    DecimalFormat decimalFormat = new DecimalFormat("#.###");

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

    String outputDir =
        OUTPUT_DIR
            + "/data/java/programs/"
            + this.programName
            + "/"
            + this.samplingApproach.getName()
            + "/"
            + this.samplingApproach.getName()
            + Options.DOT_CSV;
    File outputFile = new File(outputDir);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
  }
}
