package edu.cmu.cs.mvelezce.learning.builder.generate.matlab.script;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.learning.builder.BaseLinearLearnedModelBuilder;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class StepWiseLinearLearner {

  /*
   train =
   readtable('../../../../../../data/java/programs/MeasureDiskOrderedScan/real/feature_wise/feature_wise.csv');
   x_train = table2array(train(:,1:5)); y_train = table2array(train(:,6:6)); model =
   stepwiselm(x_train, y_train, 'linear');

   <p>mkdir('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/real/feature_wise');

   <p>terms = model.Coefficients.Row; fileID =
   fopen('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/real/feature_wise/terms.txt',
   'w'); fprintf(fileID, '%s\n', terms{:}); fclose(fileID);

   <p>coefs = model.Coefficients.Estimate; fileID =
   fopen('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/real/feature_wise/coefs.txt',
   'w'); fprintf(fileID, '%10.2f\n', coefs); fclose(fileID);

   <p>pValues = model.Coefficients.pValue; fileID =
   fopen('../../../../../../../../../../cc-perf-model-learning/src/main/resources/matlab/model/raw/java/programs/MeasureDiskOrderedScan/real/feature_wise/pValues.txt',
   'w'); fprintf(fileID, '%3.2f\n', pValues); fclose(fileID);
  */

  public static final String MATLAB_OUTPUT_DIR =
      BaseLinearLearnedModelBuilder.OUTPUT_DIR + "/matlab/model/raw/java/programs";
  public static final String COEFS_FILE = "coefs.txt";
  public static final String P_VALUES_FILE = "pValues.txt";
  public static final String TERMS_FILE = "terms.txt";

  public static final String DOT_M = ".m";

  private final String programName;
  private final List<String> options;
  private final SamplingApproach samplingApproach;
  private final String measuredTime;

  StepWiseLinearLearner(
      String programName,
      List<String> options,
      SamplingApproach samplingApproach,
      String measuredTime) {
    this.programName = programName;
    this.options = options;
    this.samplingApproach = samplingApproach;
    this.measuredTime = measuredTime;
  }

  void generateLearningScript() throws IOException {
    String outputDir =
        BaseLinearLearnedModelBuilder.OUTPUT_DIR
            + "/matlab/script/java/programs/"
            + this.programName
            + "/"
            + this.measuredTime
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
        "../../../../../../../../../../"
            + MATLAB_OUTPUT_DIR
            + "/"
            + this.programName
            + "/"
            + this.measuredTime
            + "/"
            + this.samplingApproach.getName();
    String result =
        "train = readtable('../../../../../../../data/java/programs/"
            + this.programName
            + "/"
            + this.measuredTime
            + "/"
            + this.samplingApproach.getName()
            + "/"
            + this.samplingApproach.getName()
            + Options.DOT_CSV
            + "');"
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
            + "model = stepwiselm(x_train, y_train, '"
            + this.samplingApproach.getLinearModelType()
            + "');"
            + "\n"
            + "\n"
            + "mkdir('"
            + matlabOutput
            + "');"
            + "\n"
            + "\n"
            + "terms = model.Coefficients.Row;"
            + "\n"
            + "fileID = fopen('"
            + matlabOutput
            + "/"
            + TERMS_FILE
            + "', 'w');"
            + "\n"
            + "fprintf(fileID, '%s\\n', terms{:});"
            + "\n"
            + "fclose(fileID);"
            + "\n"
            + "\n"
            + "coefs = model.Coefficients.Estimate;"
            + "\n"
            + "fileID = fopen('"
            + matlabOutput
            + "/"
            + COEFS_FILE
            + "', 'w');"
            + "\n"
            + "fprintf(fileID, '%10.2f\\n', coefs);"
            + "\n"
            + "fclose(fileID);"
            + "\n"
            + "\n"
            + "pValues = model.Coefficients.pValue;"
            + "\n"
            + "fileID = fopen('"
            + matlabOutput
            + "/"
            + P_VALUES_FILE
            + "', 'w');"
            + "\n"
            + "fprintf(fileID, '%3.2f\\n', pValues);"
            + "\n"
            + "fclose(fileID);"
            + "\n"
            + "\n"
            + "model"
            + "\n";

    writer.write(result);
    writer.flush();
    writer.close();
  }
}
