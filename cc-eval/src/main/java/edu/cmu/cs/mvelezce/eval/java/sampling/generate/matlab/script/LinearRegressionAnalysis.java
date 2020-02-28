package edu.cmu.cs.mvelezce.eval.java.sampling.generate.matlab.script;

import edu.cmu.cs.mvelezce.learning.builder.generate.matlab.script.StepWiseLinearLearner;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * filename =
 * '/Users/mvelezce/Documents/programming/java/projects/ConfigCrusher/cc-eval/src/main/resources/eval/java/programs/sampling/profiler/jprofiler/MeasureDiskOrderedScan/MeasureDiskOrderedScan.csv';
 * delimiter = ','; startRow = 2; endRow = 292; formatSpec = '%*q%f%f%*s%*s%[^\n\r]';
 *
 * <p>fileID = fopen(filename,'r');
 *
 * <p>textscan(fileID, '%[^\n\r]', startRow-1, 'WhiteSpace', '', 'ReturnOnError', false); dataArray
 * = textscan(fileID, formatSpec, endRow-startRow+1, 'Delimiter', delimiter, 'TextType', 'string',
 * 'ReturnOnError', false, 'EndOfLine', '\r\n');
 *
 * <p>fclose(fileID);
 *
 * <p>y = dataArray{:, 1}; x = dataArray{:, 2};
 *
 * <p>clearvars filename delimiter startRow endRow formatSpec fileID dataArray ans;
 *
 * <p>X = [ones(length(x),1) x];
 *
 * <p>X\y
 */
public class LinearRegressionAnalysis {

  public static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/sampling";

  private final String programName;
  private final int executedConfigs;
  private final String measuredTime;

  public LinearRegressionAnalysis(String programName, int executedConfigs, String measuredTime) {
    this.programName = programName;
    this.executedConfigs = executedConfigs;
    this.measuredTime = measuredTime;
  }

  void generateLinearRegressionScript() throws IOException {
    String script =
        "filename = '/Users/mvelezce/Documents/programming/java/projects/ConfigCrusher/cc-eval/src/main/resources/eval/java/programs/sampling/profiler/jprofiler/"
            + this.programName
            + "/"
            + this.measuredTime
            + "/"
            + this.programName
            + ".csv';\n"
            + "delimiter = ',';\n"
            + "startRow = 2;\n"
            + "endRow = "
            + (this.executedConfigs + 1)
            + ";\n"
            + "formatSpec = '%*q%f%f%*s%*s%[^\\n\\r]';\n"
            + "\n"
            + "fileID = fopen(filename,'r');\n"
            + "textscan(fileID, '%[^\\n\\r]', startRow-1, 'WhiteSpace', '', 'ReturnOnError', false);\n"
            + "dataArray = textscan(fileID, formatSpec, endRow-startRow+1, 'Delimiter', delimiter, 'TextType', 'string', 'ReturnOnError', false, 'EndOfLine', '\\r\\n');\n"
            + "fclose(fileID);\n"
            + "\n"
            + "y = dataArray{:, 1};\n"
            + "x = dataArray{:, 2};\n"
            + "\n"
            + "clearvars filename delimiter startRow endRow formatSpec fileID dataArray ans;\n"
            + "\n"
            + "X = [ones(length(x),1) x];\n"
            + "interceptAndSlope = X\\y;\n"
            + "interceptAndSlope\n"
            + "\n"
            + "res = X*interceptAndSlope;\n"
            + "Rsq2 = 1 - sum((y - res).^2)/sum((y - mean(y)).^2)\n";

    String outputDir =
        OUTPUT_DIR
            + "/linear/regression/matlab/script/java/programs/"
            + this.programName
            + "/"
            + this.measuredTime
            + "/"
            + this.programName
            + StepWiseLinearLearner.DOT_M;

    File outputFile = new File(outputDir);
    outputFile.getParentFile().mkdirs();

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    PrintWriter writer = new PrintWriter(outputFile);
    writer.write(script);
    writer.flush();
    writer.close();
  }
}
