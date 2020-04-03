package edu.cmu.cs.mvelezce.learning.builder.generate.data;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.approaches.sampling.random.RandomSampling;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.learning.builder.BaseLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

class CSVDataGenerator {

  private static final int NOT_RANDOM_SAMPLING_COUNT = -1;

  private final String programName;
  private final List<String> options;
  private final Set<PerformanceEntry> performanceEntries;
  private final SamplingApproach samplingApproach;
  private final String measuredTime;
  private final int randomSamplingCount;

  CSVDataGenerator(
      String programName,
      List<String> options,
      Set<PerformanceEntry> performanceEntries,
      SamplingApproach samplingApproach,
      String measuredTime) {
    this.programName = programName;
    this.options = options;
    this.performanceEntries = performanceEntries;
    this.samplingApproach = samplingApproach;
    this.measuredTime = measuredTime;

    if (samplingApproach instanceof RandomSampling) {
      this.randomSamplingCount = ((RandomSampling) samplingApproach).getNumExecConfigs();
    } else {
      this.randomSamplingCount = NOT_RANDOM_SAMPLING_COUNT;
    }
  }

  void generateCSVFile() throws IOException {
    StringBuilder result = new StringBuilder();

    for (String option : this.options) {
      result.append(option);
      result.append(",");
    }

    result.append("time");
    result.append("\n");

    DecimalFormat decimalFormat = new DecimalFormat("#.###");
    int configs = 0;

    for (PerformanceEntry entry : this.performanceEntries) {
      if (this.addedRandomSamplingConfigCount(configs)) {
        break;
      }

      Set<String> configuration = entry.getConfiguration();
      configs++;

      for (String option : options) {
        if (configuration.contains(option)) {
          result.append("1");
        } else {
          result.append("0");
        }

        result.append(",");
      }

      double time =
          Double.parseDouble(entry.getRegionsToPerfHumanReadable().values().iterator().next());
      result.append(decimalFormat.format(time));
      result.append("\n");
    }

    String outputDir =
        BaseLinearLearnedModelBuilder.OUTPUT_DIR
            + "/data/java/programs/"
            + this.programName
            + "/"
            + this.measuredTime
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

  private boolean addedRandomSamplingConfigCount(int configs) {
    if (this.randomSamplingCount == NOT_RANDOM_SAMPLING_COUNT) {
      return false;
    }

    return this.randomSamplingCount == configs;
  }
}
