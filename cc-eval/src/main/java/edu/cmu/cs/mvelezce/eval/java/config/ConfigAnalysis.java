package edu.cmu.cs.mvelezce.eval.java.config;

import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public final class ConfigAnalysis {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/config/";

  private final String programName;
  private final List<String> options;

  public ConfigAnalysis(String programName) {
    this(programName, new ArrayList<>());
  }

  public ConfigAnalysis(String programName, List<String> options) {
    this.programName = programName;
    this.options = options;
  }

  public void compareMeasurementAndPrediction(
      String approach,
      Set<PerformanceEntry> performanceEntries,
      PerformanceModel<Partition> model,
      Set<String> config)
      throws IOException {
    Map<UUID, Double> measuredTimes = this.getMeasuredTimes(performanceEntries, config);
    Map<UUID, Double> predictedTimes = this.getPredictedTimes(model, config);

    if (!measuredTimes.keySet().equals(predictedTimes.keySet())) {
      throw new RuntimeException(
          "The regions do not match between the profiled times and the predicted times");
    }

    double thresholdToPrint = 1E6;
    StringBuilder result = new StringBuilder();
    result.append("region,measured,predicted,relative percent error");
    result.append("\n");

    for (UUID region : measuredTimes.keySet()) {
      double measuredTime = measuredTimes.get(region);
      double predictedTime = predictedTimes.get(region);

      if (measuredTime < thresholdToPrint && predictedTime < thresholdToPrint) {
        continue;
      }

      measuredTime = Math.max(measuredTime, 1E6);
      predictedTime = Math.max(predictedTime, 1E6);

      double absoluteError = Math.abs(predictedTime - measuredTime);
      double relativeError = absoluteError / measuredTime;

      result.append(region);
      result.append(",");
      result.append(Evaluation.DECIMAL_FORMAT.format(measuredTime / 1E9));
      result.append(",");
      result.append(Evaluation.DECIMAL_FORMAT.format(predictedTime / 1E9));
      result.append(",");
      result.append(Evaluation.DECIMAL_FORMAT.format(relativeError * 100));
      result.append("\n");
    }

    File outputFile =
        new File(
            OUTPUT_DIR
                + "/compare/"
                + this.programName
                + "/"
                + approach
                + "/"
                + config
                + Evaluation.DOT_CSV);

    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
  }

  private Map<UUID, Double> getPredictedTimes(
      PerformanceModel<Partition> model, Set<String> config) {
    Map<UUID, Double> predictedTimes = new HashMap<>();

    for (LocalPerformanceModel<Partition> localModel : model.getLocalModels()) {
      predictedTimes.put(localModel.getRegion(), localModel.evaluate(config, this.options));
    }

    return predictedTimes;
  }

  private Map<UUID, Double> getMeasuredTimes(
      Set<PerformanceEntry> performanceEntries, Set<String> config) {
    Map<UUID, Double> measuredTimes = new HashMap<>();

    for (PerformanceEntry entry : performanceEntries) {
      if (!entry.getConfiguration().equals(config)) {
        continue;
      }

      measuredTimes = entry.getRegionsToPerf();
    }

    return measuredTimes;
  }

  public void some(String approach, Set<PerformanceEntry> performanceEntries, Set<String> config)
      throws IOException {
    for (PerformanceEntry perfEntry : performanceEntries) {
      if (!perfEntry.getConfiguration().equals(config)) {
        continue;
      }

      StringBuilder result = new StringBuilder();
      result.append("region,measured");
      result.append("\n");

      for (Map.Entry<UUID, Double> entry : perfEntry.getRegionsToPerf().entrySet()) {
        result.append(entry.getKey());
        result.append(",");
        result.append(Evaluation.DECIMAL_FORMAT.format(entry.getValue() / 1E9));
        result.append("\n");
      }

      File outputFile =
          new File(
              OUTPUT_DIR
                  + "/measured/"
                  + approach
                  + "/"
                  + this.programName
                  + "/"
                  + config
                  + Evaluation.DOT_CSV);

      outputFile.getParentFile().mkdirs();

      if (outputFile.exists()) {
        FileUtils.forceDelete(outputFile);
      }

      PrintWriter writer = new PrintWriter(outputFile);
      writer.write(result.toString());
      writer.flush();
      writer.close();

      return;
    }
  }
}
