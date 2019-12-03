package edu.cmu.cs.mvelezce.learning.generate.matlab.script;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.learning.ModelBuilder;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

class StepWiseLinearLearner {

  private static final String DOT_M = ".m";

  private final String programName;
  private final List<String> options;
  private final SamplingApproach samplingApproach;

  StepWiseLinearLearner(
      String programName, List<String> options, SamplingApproach samplingApproach) {
    this.programName = programName;
    this.options = options;
    this.samplingApproach = samplingApproach;
  }

  void generateLearningScript() throws IOException {
    String outputDir =
        ModelBuilder.OUTPUT_DIR
            + "/matlab/script/java/programs/"
            + this.programName
            + "/"
            + this.samplingApproach.getName()
            + "/"
            + this.samplingApproach.getName()
            + DOT_M;
    File outputFile = new File(outputDir);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);
    String result =
        "train = readtable(\'../../../../../../data/java/programs/"
            + this.programName
            + "/"
            + this.samplingApproach.getName()
            + "/"
            + this.samplingApproach.getName()
            + Options.DOT_CSV
            + "\');"
            + "\n"
            + "x_train = table2array(train(:,1:"
            + this.options.size()
            + "));"
            + "\n"
            + "y_train = table2array(train(:,"
            + (this.options.size() + 1)
            + ":"
            + (this.options.size() + 1)
            + "));"
            + "\n"
            + "model = stepwiselm(x_train, y_train, 'linear');";
    writer.write(result);
    writer.flush();
    writer.close();
  }
}
