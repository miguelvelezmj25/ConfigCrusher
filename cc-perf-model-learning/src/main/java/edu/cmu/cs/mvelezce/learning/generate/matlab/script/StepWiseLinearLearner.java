package edu.cmu.cs.mvelezce.learning.generate.matlab.script;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.learning.LearnedModelBuilder;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

class StepWiseLinearLearner {

  /**
   * train =
   * readtable('../../../../../../data/java/programs/MeasureDiskOrderedScan/feature_wise/feature_wise.csv');
   * x_train = table2array(train(:,1:5)); y_train = table2array(train(:,6:6)); model
   * =stepwiselm(x_train, y_train, 'linear');
   *
   * <p>mkdir('../../../../../../matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise');
   *
   * <p>terms = model.Coefficients.Row; fileID =
   * fopen('../../../../../../matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise/terms.txt',
   * 'w'); fprintf(fileID, '%s\n', terms{:}); fclose(fileID);
   *
   * <p>coefs = model.Coefficients.Estimate; fileID
   * =fopen('../../../../../../matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise/coefs.txt',
   * 'w'); fprintf(fileID, '%10.2f\n', coefs); fclose(fileID);
   *
   * <p>pValues = model.Coefficients.pValue; fileID =
   * fopen('../../../../../../matlab/model/raw/java/programs/MeasureDiskOrderedScan/feature_wise/pValues.txt',
   * 'w'); fprintf(fileID, '%3.2f\n', pValues); fclose(fileID);
   */
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
        LearnedModelBuilder.OUTPUT_DIR
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

    String matlabOutput =
        "../../../../../../matlab/model/raw/java/programs/"
            + this.programName
            + "/"
            + this.samplingApproach.getName();
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
            + "model = stepwiselm(x_train, y_train, 'linear');"
            + "\n"
            + "\n"
            + "mkdir(\'"
            + matlabOutput
            + "\');"
            + "\n"
            + "\n"
            + "terms = model.Coefficients.Row;"
            + "\n"
            + "fileID = fopen(\'"
            + matlabOutput
            + "/terms.txt\', \'w\');"
            + "\n"
            + "fprintf(fileID, \'%s\\n\', terms{:});"
            + "\n"
            + "fclose(fileID);"
            + "\n"
            + "\n"
            + "coefs = model.Coefficients.Estimate;"
            + "\n"
            + "fileID = fopen(\'"
            + matlabOutput
            + "/coefs.txt\', \'w\');"
            + "\n"
            + "fprintf(fileID, \'%10.2f\\n\', coefs);"
            + "\n"
            + "fclose(fileID);"
            + "\n"
            + "\n"
            + "pValues = model.Coefficients.pValue;"
            + "\n"
            + "fileID = fopen(\'"
            + matlabOutput
            + "/pValues.txt\', \'w\');"
            + "\n"
            + "fprintf(fileID, \'%3.2f\\n\', pValues);"
            + "\n"
            + "fclose(fileID);"
            + "\n";

    writer.write(result);
    writer.flush();
    writer.close();
  }
}
