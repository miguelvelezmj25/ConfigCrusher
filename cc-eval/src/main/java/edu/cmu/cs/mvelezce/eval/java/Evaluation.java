package edu.cmu.cs.mvelezce.eval.java;

import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Evaluation<T> {

  public static final String FULL_DIR = "/full";
  public static final String DOT_CSV = ".csv";

  public static final String CC = "CC";
  public static final String IDTA = "IDTA";
  public static final String GT = "GT";
  public static final String FW = "FW";
  public static final String PW = "PW";
  public static final String SPLATD = "SPLATD";
  public static final String FAM = "FAM";
  public static final String BF = "BF";

  private final String programName;
  private final List<String> options;

  public Evaluation(String programName, List<String> options) {
    this.programName = programName;
    this.options = options;
  }

  public void saveConfigsToPerformance(
      String approach, Set<Set<String>> configs, PerformanceModel<T> model) throws IOException {
    File outputFile =
        new File(
            this.getOutputDir() + "/" + this.programName + FULL_DIR + "/" + approach + DOT_CSV);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    Map<Set<String>, Long> configsToTime = this.getEmptyConfigsToTime(configs);

    for (Set<String> config : configs) {
      long currentTime = configsToTime.get(config);
      currentTime += model.evaluate(config, this.options);
      configsToTime.put(config, currentTime);
    }

    StringBuilder result = new StringBuilder();
    result.append("measured,configuration,performance,std,minci,maxci");
    result.append("\n");

    DecimalFormat decimalFormat = new DecimalFormat("#.###");

    for (Map.Entry<Set<String>, Long> entry : configsToTime.entrySet()) {
      result.append("true");
      result.append(",");
      result.append('"');
      result.append(entry.getKey());
      result.append('"');
      result.append(",");
      double performance = entry.getValue() / 1E9;
      result.append(decimalFormat.format(performance));
      result.append(",");
      result.append(",");
      result.append(",");
      result.append("\n");
    }

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

  protected abstract String getOutputDir();
}
