package edu.cmu.cs.mvelezce.eval.java.sampling.profiler.jprofiler;

import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.eval.metrics.Metric;
import edu.cmu.cs.mvelezce.eval.metrics.error.absolute.AbsoluteError;
import edu.cmu.cs.mvelezce.eval.metrics.error.relative.RelativeError;
import edu.cmu.cs.mvelezce.java.results.processed.PerformanceEntry;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public final class JProfilerOverheadAnalysis {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/sampling/profiler/jprofiler";

  private final String programName;
  private final String measuredTime;

  public JProfilerOverheadAnalysis(String programName, String measuredTime) {
    this.programName = programName;
    this.measuredTime = measuredTime;
  }

  public void analyze(Set<PerformanceEntry> idtaPerfEntries, Set<PerformanceEntry> e2ePerfEntries)
      throws IOException {
    if (idtaPerfEntries.size() != e2ePerfEntries.size()) {
      throw new RuntimeException(
          "The perf entries lengths do not match: "
              + idtaPerfEntries.size()
              + " vs "
              + e2ePerfEntries.size());
    }

    Set<Set<String>> configs = this.getConfigs(idtaPerfEntries);
    this.checkHasConfigs(configs, idtaPerfEntries);
    this.checkHasConfigs(configs, e2ePerfEntries);

    String analysisResults = this.getAnalysisResults(configs, idtaPerfEntries, e2ePerfEntries);

    File outputFile =
        new File(
            OUTPUT_DIR
                + "/"
                + this.programName
                + "/"
                + this.measuredTime
                + "/"
                + this.programName
                + Evaluation.DOT_CSV);

    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(analysisResults);
    writer.flush();
    writer.close();
  }

  private String getAnalysisResults(
      Set<Set<String>> configs,
      Set<PerformanceEntry> idtaPerfEntries,
      Set<PerformanceEntry> e2ePerfEntries) {
    Metric<Double> absoluteErrorMetric = new AbsoluteError();
    Metric<Double> relativeErrorMetric = new RelativeError();
    double[] idtaTimes = new double[configs.size()];
    double[] e2eTimes = new double[configs.size()];
    int index = 0;

    StringBuilder result = new StringBuilder();
    result.append("config,idta time,e2e time,absolute error,relative % error");
    result.append("\n");

    for (Set<String> config : configs) {
      result.append('"');
      result.append(config);
      result.append('"');
      result.append(",");

      PerformanceEntry idtaPerfEntry = this.getPerfEntry(config, idtaPerfEntries);
      double idtaTime = this.getTime(idtaPerfEntry) / 1E9;
      idtaTimes[index] = idtaTime;
      result.append(Evaluation.DECIMAL_FORMAT.format(idtaTime));
      result.append(",");

      PerformanceEntry e2ePerfEntry = this.getPerfEntry(config, e2ePerfEntries);
      double e2eTime = this.getTime(e2ePerfEntry) / 1E9;
      e2eTimes[index] = e2eTime;
      result.append(Evaluation.DECIMAL_FORMAT.format(e2eTime));
      result.append(",");

      double absoluteError = Math.abs(idtaTime - e2eTime);
      double relativeError = absoluteError / e2eTime;
      absoluteErrorMetric.getEntries().add(absoluteError);
      relativeErrorMetric.getEntries().add(relativeError);

      result.append(Evaluation.DECIMAL_FORMAT.format(absoluteError));
      result.append(",");
      result.append(Evaluation.DECIMAL_FORMAT.format(relativeError * 100));
      result.append("\n");

      index++;
    }

    result.append("\n");
    result.append("Total: ");
    result.append(configs.size());
    result.append("\n");
    result.append("\n");
    result.append("Pearsons correlation: ");
    result.append(
        Evaluation.DECIMAL_FORMAT.format(
            new PearsonsCorrelation().correlation(idtaTimes, e2eTimes)));
    result.append("\n");
    result.append("Spearmans correlation: ");
    result.append(
        Evaluation.DECIMAL_FORMAT.format(
            new SpearmansCorrelation().correlation(idtaTimes, e2eTimes)));
    result.append("\n");
    result.append("\n");
    result.append("Min Absolute Error: ");
    result.append(Evaluation.DECIMAL_FORMAT.format(absoluteErrorMetric.getMin()) + " secs.");
    result.append("\n");
    result.append("Max Absolute Error: ");
    result.append(Evaluation.DECIMAL_FORMAT.format(absoluteErrorMetric.getMax()) + " secs.");
    result.append("\n");
    result.append("Mean Absolute Error: ");
    result.append(
        Evaluation.DECIMAL_FORMAT.format(absoluteErrorMetric.getArithmeticMean()) + " secs.");
    result.append("\n");
    result.append("Median Absolute Error: ");
    result.append(Evaluation.DECIMAL_FORMAT.format(absoluteErrorMetric.getMedian()) + " secs.");
    result.append("\n");
    result.append("\n");
    result.append("Min Relative Percent Error: ");
    result.append(Evaluation.DECIMAL_FORMAT.format(relativeErrorMetric.getMin() * 100) + "%");
    result.append("\n");
    result.append("Max Relative Percent Error: ");
    result.append(Evaluation.DECIMAL_FORMAT.format(relativeErrorMetric.getMax() * 100) + "%");
    result.append("\n");
    result.append("Mean Relative Percent Error: ");
    result.append(
        Evaluation.DECIMAL_FORMAT.format(relativeErrorMetric.getArithmeticMean() * 100) + "%");
    result.append("\n");
    result.append("Median Relative Percent Error: ");
    result.append(Evaluation.DECIMAL_FORMAT.format(relativeErrorMetric.getMedian() * 100) + "%");
    result.append("\n");
    result.append("\n");
    result.append("Mean Absolute Percentage Error: ");
    double mape = relativeErrorMetric.getArithmeticMean() * 100;
    result.append(Evaluation.DECIMAL_FORMAT.format(mape));
    result.append("%");
    result.append("\n");

    return result.toString();
  }

  private double getTime(PerformanceEntry perfEntry) {
    double time = 0.0;

    for (double entryTime : perfEntry.getRegionsToPerf().values()) {
      time += entryTime;
    }

    return time;
  }

  private PerformanceEntry getPerfEntry(Set<String> config, Set<PerformanceEntry> perfEntries) {
    for (PerformanceEntry entry : perfEntries) {
      if (entry.getConfiguration().equals(config)) {
        return entry;
      }
    }

    throw new RuntimeException("Could not find an entry with this config " + config);
  }

  private void checkHasConfigs(Set<Set<String>> configs, Set<PerformanceEntry> idtaPerfEntries) {
    for (PerformanceEntry entry : idtaPerfEntries) {
      if (!configs.contains(entry.getConfiguration())) {
        throw new RuntimeException(
            "The performance entries do no have the config " + entry.getConfiguration());
      }
    }
  }

  private Set<Set<String>> getConfigs(Set<PerformanceEntry> idtaPerfEntries) {
    Set<Set<String>> configs = new HashSet<>();

    for (PerformanceEntry entry : idtaPerfEntries) {
      configs.add(entry.getConfiguration());
    }

    return configs;
  }
}
