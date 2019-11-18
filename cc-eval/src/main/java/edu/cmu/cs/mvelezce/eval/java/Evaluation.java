package edu.cmu.cs.mvelezce.eval.java;

import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class Evaluation {

  public static final String OUTPUT_DIR = "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs";
  public static final String FULL_DIR = "/full";
  public static final String DOT_CSV = ".csv";

  public static final String CC = "CC";
  public static final String GT = "GT";
  public static final String FW = "FW";
  public static final String PW = "PW";
  public static final String SPLATD = "SPLATD";
  public static final String FAM = "FAM";
  public static final String BF = "BF";

  private final String programName;

  public Evaluation(String programName) {
    this.programName = programName;
  }

  public void saveConfigsToPerformance(String approach, PerformanceModel model) throws IOException {
    File outputFile =
        new File(OUTPUT_DIR + "/" + this.programName + FULL_DIR + "/" + approach + DOT_CSV);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    StringBuilder result = new StringBuilder();
    result.append("measured,configuration,performance,std,minci,maxci");
    result.append("\n");

    DecimalFormat decimalFormat = new DecimalFormat("#.###");

    //        for (PerformanceEntryStatistic performanceEntry : performanceEntries) {
    //          if (performanceEntry.getRegionsToProcessedPerformanceHumanReadable().size() != 1) {
    //            throw new RuntimeException("This method can only handle approaches that measure 1
    //     region" +
    //                    " (e.g. Brute force)");
    //          }
    //
    //          result.append("true");
    //          result.append(",");
    //          result.append('"');
    //          result.append(performanceEntry.getConfiguration());
    //          result.append('"');
    //          result.append(",");
    //          double performance =
    //     performanceEntry.getRegionsToProcessedPerformanceHumanReadable().values()
    //                  .iterator().next();
    //          result.append(decimalFormat.format(performance));
    //          result.append(",");
    //          double std =
    //     performanceEntry.getRegionsToProcessedStdHumanReadable().values().iterator()
    //                  .next();
    //          result.append(decimalFormat.format(std));
    //          result.append(",");
    //          List<Double> ci =
    //     performanceEntry.getRegionsToProcessedCIHumanReadable().values().iterator()
    //                  .next();
    //          double minCI = ci.get(0);
    //          double maxCI = ci.get(1);
    //          result.append(decimalFormat.format(minCI));
    //          result.append(",");
    //          result.append(decimalFormat.format(maxCI));
    //          result.append("\n");
    //        }

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(result.toString());
    writer.flush();
    writer.close();
  }
}
