package edu.cmu.cs.mvelezce.eval.java;

import edu.cmu.cs.mvelezce.eval.metrics.Metric;
import edu.cmu.cs.mvelezce.eval.metrics.error.absolute.AbsoluteError;
import edu.cmu.cs.mvelezce.eval.metrics.error.relative.RelativeError;
import edu.cmu.cs.mvelezce.eval.metrics.error.squared.MeanSquaredError;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public abstract class Evaluation<T> {

  public static final String FULL_DIR = "/full";
  public static final String COMPARISON_DIR = "/comparison";
  public static final String DOT_CSV = ".csv";
  public static final String CC = "CC";
  public static final String IDTA = "IDTA";
  public static final String GT = "GT";
  public static final String FW = "FW";
  public static final String PW = "PW";
  public static final String SPLATD = "SPLATD";
  public static final String FAM = "FAM";
  public static final String BF = "BF";
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");
  private final String programName;
  private final List<String> options;

  public Evaluation(String programName, List<String> options) {
    this.programName = programName;
    this.options = options;
  }

  void saveConfigsToPerformanceExhaustive(
      String approach, Set<Set<String>> configs, PerformanceModel<T> model) throws IOException {
    throw new UnsupportedOperationException("implement");
    //    File outputFile = this.getApproachOutputFile(approach);
    //
    //    if (outputFile.exists()) {
    //      FileUtils.forceDelete(outputFile);
    //    }
    //
    //    Map<Set<String>, Long> configsToTime = this.getEmptyConfigsToTime(configs);
    //
    //    for (Set<String> config : configs) {
    //      long currentTime = configsToTime.get(config);
    //      currentTime += model.evaluate(config, this.options);
    //      configsToTime.put(config, currentTime);
    //    }
    //
    //    StringBuilder result = new StringBuilder();
    //    result.append("configuration,measured,performance,std,minci,maxci");
    //    result.append("\n");
    //
    //    for (Map.Entry<Set<String>, Long> entry : configsToTime.entrySet()) {
    //      result.append('"');
    //      result.append(entry.getKey());
    //      result.append('"');
    //      result.append(",");
    //      result.append("TODO");
    //      result.append(",");
    //      double performance = entry.getValue() / 1E9;
    //      result.append(DECIMAL_FORMAT.format(performance));
    //      result.append(",");
    //      result.append(",");
    //      result.append(",");
    //      result.append("\n");
    //    }
    //
    //    outputFile.getParentFile().mkdirs();
    //    PrintWriter writer = new PrintWriter(outputFile);
    //    writer.write(result.toString());
    //    writer.flush();
    //    writer.close();
  }

  void saveConfigsToPerformance(
      String approach,
      Set<Set<String>> executedConfigs,
      Set<Set<String>> configsToPredict,
      PerformanceModel<T> model)
      throws IOException {
    File outputFile = this.getApproachOutputFile(approach);

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
      result.append(DECIMAL_FORMAT.format(performance));
      result.append("\n");
    }

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
  }

  void compareApproaches(String approach1, String approach2) throws IOException {
    File outputFile =
        new File(
            this.getOutputDir()
                + "/"
                + this.programName
                + COMPARISON_DIR
                + "/"
                + approach1
                + "_"
                + approach2
                + DOT_CSV);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    File outputFile1 = this.getApproachOutputFile(approach1);
    Map<Set<String>, List<String>> data1 = this.getData(outputFile1);

    File outputFile2 = this.getApproachOutputFile(approach2);
    Map<Set<String>, List<String>> data2 = this.getData(outputFile2);

    if (data1.size() != data2.size()) {
      throw new RuntimeException(
          "The data sizes for " + approach1 + " and " + approach2 + " are not the same");
    }

    Set<Set<String>> configurations = data1.keySet();

    StringBuilder result = new StringBuilder();
    result.append(
        "configuration,measured,"
            + approach1
            + ","
            + approach2
            + ",absolute error,relative error,squared error");
    result.append("\n");

    Metric<Double> absoluteErrorMetric = new AbsoluteError();
    Metric<Double> relativeErrorMetric = new RelativeError();
    MeanSquaredError meanSquaredErrorMetric = new MeanSquaredError();

    for (Set<String> configuration : configurations) {
      List<String> entries1 = data1.get(configuration);
      List<String> entries2 = data2.get(configuration);

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
      result.append(time1);
      result.append(",");
      //      result.append(entries1.get(2));
      //      result.append(",");

      double time2 = Double.parseDouble(entries2.get(1));
      result.append(time2);
      result.append(",");
      //      result.append(entries2.get(2));
      //      result.append(",");

      double absoluteError = Math.abs(time1 - time2);
      double relativeError = absoluteError / time2;
      double squaredError = Math.pow(absoluteError, 2);

      absoluteErrorMetric.getEntries().add(absoluteError);
      relativeErrorMetric.getEntries().add(relativeError);
      meanSquaredErrorMetric.getEntries().add(absoluteError);

      result.append(DECIMAL_FORMAT.format(absoluteError));
      result.append(",");
      result.append(DECIMAL_FORMAT.format(relativeError));
      result.append(",");
      result.append(DECIMAL_FORMAT.format(squaredError));
      result.append("\n");
    }

    result.append("\n");
    result.append("Total: ");
    result.append(configurations.size());
    result.append("\n");
    result.append("\n");
    result.append("Min AE: ");
    result.append(DECIMAL_FORMAT.format(absoluteErrorMetric.getMin()));
    result.append("\n");
    result.append("Max AE: ");
    result.append(DECIMAL_FORMAT.format(absoluteErrorMetric.getMax()));
    result.append("\n");
    result.append("MAE: ");
    result.append(DECIMAL_FORMAT.format(absoluteErrorMetric.getArithmeticMean()));
    result.append("\n");
    result.append("\n");
    result.append("Min RE: ");
    result.append(DECIMAL_FORMAT.format(relativeErrorMetric.getMin()));
    result.append("\n");
    result.append("Max RE: ");
    result.append(DECIMAL_FORMAT.format(relativeErrorMetric.getMax()));
    result.append("\n");
    result.append("MRE: ");
    result.append(DECIMAL_FORMAT.format(relativeErrorMetric.getArithmeticMean()));
    result.append("\n");
    result.append("\n");
    result.append("MSE: ");
    result.append(DECIMAL_FORMAT.format(meanSquaredErrorMetric.getError()));
    //    result.append("\n");
    //    result.append("\n");
    //    result.append("RMSE: ");
    //    result.append(DECIMAL_FORMAT.format(Math.sqrt(mse)));
    result.append("\n");
    result.append("\n");
    result.append("MAPE: ");
    double mape = relativeErrorMetric.getArithmeticMean() * 100;
    result.append(DECIMAL_FORMAT.format(mape));
    result.append("\n");

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
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

  private File getApproachOutputFile(String approach) {
    return new File(
        this.getOutputDir() + "/" + this.programName + FULL_DIR + "/" + approach + DOT_CSV);
  }

  protected abstract String getOutputDir();
}
