package edu.cmu.cs.mvelezce.eval.java.accuracy;

import edu.cmu.cs.mvelezce.eval.java.Evaluation;
import edu.cmu.cs.mvelezce.eval.metrics.Metric;
import edu.cmu.cs.mvelezce.eval.metrics.error.absolute.AbsoluteError;
import edu.cmu.cs.mvelezce.eval.metrics.error.relative.RelativeError;
import edu.cmu.cs.mvelezce.eval.metrics.error.squared.MeanSquaredError;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import java.io.*;
import java.util.*;

public abstract class AccuracyEvaluation<T> {

  public static final String FULL_DIR = "/full";
  public static final String COMPARISON_DIR = "/comparison";
  private final String programName;
  private final List<String> options;

  public AccuracyEvaluation(String programName, List<String> options) {
    this.programName = programName;
    this.options = options;
  }

  public void saveConfigsToPerformanceExhaustive(
      String approach, String measuredTime, Set<Set<String>> configs, PerformanceModel<T> model)
      throws IOException {
    File outputFile = this.getApproachOutputFile(approach, measuredTime);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    Map<Set<String>, Long> configsToTime = this.getEmptyConfigsToTime(configs);
    Map<Set<String>, Double> configsToVariance = this.getEmptyConfigsToVariance(configs);
    Map<Set<String>, List<Double>> configsToCI = this.getEmptyConfigsToCI(configs);

    for (Set<String> config : configs) {
      long time = model.evaluate(config, this.options);
      configsToTime.put(config, time);

      for (LocalPerformanceModel<T> localModel : model.getLocalModels()) {
        double variance = localModel.evaluateVariance(config, this.options);
        configsToVariance.put(config, variance);

        List<Double> ci = localModel.evaluateConfidenceInterval(config, this.options);
        configsToCI.put(config, ci);
      }
    }

    StringBuilder result = new StringBuilder();
    result.append("configuration,measured,performance,variance,minci,maxci,ciwindow");
    result.append("\n");

    for (Set<String> config : configsToTime.keySet()) {
      result.append('"');
      result.append(config);
      result.append('"');
      result.append(",");
      result.append("true");
      result.append(",");
      double performance = configsToTime.get(config) / 1E9;
      result.append(Evaluation.DECIMAL_FORMAT.format(performance));
      result.append(",");
      double variance = configsToVariance.get(config) / 1E18;
      result.append(Evaluation.DECIMAL_FORMAT.format(variance));
      result.append(",");
      double minci = configsToCI.get(config).get(0) / 1E9;
      result.append(Evaluation.DECIMAL_FORMAT.format(minci));
      result.append(",");
      double maxci = configsToCI.get(config).get(1) / 1E9;
      result.append(Evaluation.DECIMAL_FORMAT.format(maxci));
      result.append(",");
      double ciwindow = (maxci - minci);
      result.append(Evaluation.DECIMAL_FORMAT.format(ciwindow));
      result.append("\n");
    }

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
  }

  public void saveConfigsToPerformance(
      String approach,
      String measuredTime,
      Set<Set<String>> executedConfigs,
      Set<Set<String>> configsToPredict,
      PerformanceModel<T> model)
      throws IOException {
    File outputFile = this.getApproachOutputFile(approach, measuredTime);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    Map<Set<String>, Long> configsToTime = this.getEmptyConfigsToTime(configsToPredict);

    for (Set<String> config : configsToPredict) {
      long currentTime = configsToTime.get(config);
      currentTime += model.evaluate(config, this.options);
      configsToTime.put(config, currentTime);
    }

    StringBuilder result = new StringBuilder();
    result.append("configuration,measured,performance");
    result.append("\n");

    for (Map.Entry<Set<String>, Long> entry : configsToTime.entrySet()) {
      Set<String> config = entry.getKey();
      result.append('"');
      result.append(config);
      result.append('"');
      result.append(",");
      result.append(executedConfigs.contains(config));
      result.append(",");
      double performance = entry.getValue() / 1E9;
      result.append(Evaluation.DECIMAL_FORMAT.format(performance));
      result.append("\n");
    }

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
  }

  public void compareApproaches(String approach1, String gt, String measuredTime)
      throws IOException {
    this.compareApproaches(approach1, measuredTime, gt, measuredTime);
  }

  public void compareApproaches(
      String approach1, String approach1MeasuredTime, String gt, String gtMeasuredTime)
      throws IOException {
    File outputFile =
        new File(
            this.getOutputDir()
                + "/"
                + this.programName
                + "/"
                + approach1MeasuredTime
                + "/"
                + COMPARISON_DIR
                + "/"
                + approach1
                + "_"
                + gt
                + Evaluation.DOT_CSV);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    File outputFile1 = this.getApproachOutputFile(approach1, approach1MeasuredTime);
    Map<Set<String>, List<String>> data1 = this.getData(outputFile1);

    File outputFileGT = this.getApproachOutputFile(gt, gtMeasuredTime);
    Map<Set<String>, List<String>> dataGT = this.getData(outputFileGT);

    if (data1.size() != dataGT.size()) {
      throw new RuntimeException(
          "The data sizes for " + approach1 + " and " + gt + " are not the same");
    }

    Set<Set<String>> configurations = data1.keySet();

    StringBuilder result = new StringBuilder();
    result.append(
        "configuration,measured,"
            + approach1
            + ","
            + gt
            + ",,minci,maxci,ciwindow,"
            + approach1
            + " in ci,,absolute error,relative percent error,squared error");
    result.append("\n");

    int count = 0;
    double[] times1 = new double[configurations.size()];
    double[] timesGT = new double[configurations.size()];
    int predsInCI = 0;
    Metric<Double> absoluteErrorMetric = new AbsoluteError();
    Metric<Double> relativeErrorMetric = new RelativeError();
    MeanSquaredError meanSquaredErrorMetric = new MeanSquaredError();

    for (Set<String> configuration : configurations) {
      List<String> entries1 = data1.get(configuration);
      List<String> entriesGT = dataGT.get(configuration);

      String measured = entries1.get(0);

      result.append('"');
      result.append(configuration);
      result.append('"');
      result.append(",");
      result.append('"');
      result.append(measured);
      result.append('"');
      result.append(",");

      double time1 = Double.parseDouble(entries1.get(1));
      times1[count] = time1;
      result.append(time1);
      result.append(",");
      double timeGT = Double.parseDouble(entriesGT.get(1));
      timesGT[count] = timeGT;
      result.append(timeGT);
      result.append(",");

      result.append(",");
      double minci = Double.parseDouble(entriesGT.get(3));
      result.append(minci);
      result.append(",");
      double maxci = Double.parseDouble(entriesGT.get(4));
      result.append(maxci);
      result.append(",");
      double ciwindow = Double.parseDouble(entriesGT.get(5));
      result.append(ciwindow);
      result.append(",");

      boolean predInCI = time1 >= minci && time1 <= maxci;

      if (predInCI) {
        predsInCI++;
      }

      result.append(predInCI ? "*" : "");
      result.append(",");
      result.append(",");

      double absoluteError = Math.abs(time1 - timeGT);
      double relativeError = absoluteError / timeGT;
      double squaredError = Math.pow(absoluteError, 2);

      absoluteErrorMetric.getEntries().add(absoluteError);
      relativeErrorMetric.getEntries().add(relativeError);
      meanSquaredErrorMetric.getEntries().add(absoluteError);

      result.append(Evaluation.DECIMAL_FORMAT.format(absoluteError));
      result.append(",");
      result.append(Evaluation.DECIMAL_FORMAT.format(relativeError * 100));
      result.append(",");
      result.append(Evaluation.DECIMAL_FORMAT.format(squaredError));

      result.append("\n");
      count++;
    }

    result.append("\n");
    result.append("Total: ");
    result.append(configurations.size());
    result.append("\n");
    result.append("\n");
    result.append("Predictions within CI: ");
    result.append(predsInCI);
    result.append("\n");
    result.append("Predictions within CI: ");
    result.append((100.0 * predsInCI) / configurations.size());
    result.append("%");
    result.append("\n");
    result.append("\n");
    result.append("Pearsons correlation: ");
    result.append(
        Evaluation.DECIMAL_FORMAT.format(new PearsonsCorrelation().correlation(times1, timesGT)));
    result.append("\n");
    result.append("Spearmans correlation: ");
    result.append(
        Evaluation.DECIMAL_FORMAT.format(new SpearmansCorrelation().correlation(times1, timesGT)));
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
    result.append("Mean Squared Error: ");
    result.append(Evaluation.DECIMAL_FORMAT.format(meanSquaredErrorMetric.getError()));
    //    result.append("\n");
    //    result.append("\n");
    //    result.append("RMSE: ");
    //    result.append(Evaluation.DECIMAL_FORMAT.format(Math.sqrt(mse)));
    result.append("\n");
    result.append("\n");
    result.append("Mean Absolute Percentage Error: ");
    double mape = relativeErrorMetric.getArithmeticMean() * 100;
    result.append(Evaluation.DECIMAL_FORMAT.format(mape));
    result.append("%");
    result.append("\n");

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
  }

  private Map<Set<String>, List<Double>> getEmptyConfigsToCI(Set<Set<String>> configs) {
    Map<Set<String>, List<Double>> configsToCI = new HashMap<>();

    for (Set<String> config : configs) {
      configsToCI.put(config, new ArrayList<>());
    }

    return configsToCI;
  }

  private Map<Set<String>, Double> getEmptyConfigsToVariance(Set<Set<String>> configs) {
    Map<Set<String>, Double> configsToVariance = new HashMap<>();

    for (Set<String> config : configs) {
      configsToVariance.put(config, 0.0);
    }

    return configsToVariance;
  }

  private Map<Set<String>, Long> getEmptyConfigsToTime(Set<Set<String>> configs) {
    Map<Set<String>, Long> configsToTime = new HashMap<>();

    for (Set<String> config : configs) {
      configsToTime.put(config, 0L);
    }

    return configsToTime;
  }

  private Map<Set<String>, List<String>> getData(File file) throws IOException {
    Map<Set<String>, List<String>> configToData = new HashMap<>();

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line = reader.readLine();

    while ((line = reader.readLine()) != null) {
      Set<String> config = this.getConfig(line);
      List<String> data = this.getEntryData(line);
      configToData.put(config, data);
    }

    reader.close();

    return configToData;
  }

  private List<String> getEntryData(String line) {

    int endOptionIndex = line.lastIndexOf("]");
    String dataString = line.substring(endOptionIndex + 3);
    String[] entries = dataString.split(",");

    return new ArrayList<>(Arrays.asList(entries));
  }

  private Set<String> getConfig(String line) {
    int startOptionIndex = line.indexOf("[") + 1;
    int endOptionIndex = line.lastIndexOf("]");
    String optionsString = line.substring(startOptionIndex, endOptionIndex);
    String[] arrayOptions = optionsString.split(",");

    Set<String> conf = new HashSet<>();

    for (String arrayOption : arrayOptions) {
      conf.add(arrayOption.trim());
    }

    return conf;
  }

  private File getApproachOutputFile(String approach, String measuredTime) {
    return new File(
        this.getOutputDir()
            + "/"
            + this.programName
            + "/"
            + measuredTime
            + "/"
            + FULL_DIR
            + "/"
            + approach
            + Evaluation.DOT_CSV);
  }

  protected abstract String getOutputDir();
}
