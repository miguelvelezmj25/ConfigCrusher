package edu.cmu.cs.mvelezce.eval.java.models.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.eval.java.models.ModelsEvaluation;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ConstraintModelsEvaluation extends ModelsEvaluation<FeatureExpr> {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/models";

  public ConstraintModelsEvaluation(
      String programName,
      Collection<String> options,
      double diffThreshold,
      double perfIntensiveThreshold) {
    super(programName, options, diffThreshold, perfIntensiveThreshold);
  }

  @Override
  protected void saveComparedModels(Map<UUID, Map<FeatureExpr, List<Double>>> comparedModels)
      throws IOException {
    File rootFile =
        new File(OUTPUT_DIR + "/" + this.getProgramName() + "/" + ModelsEvaluation.COMPARISON_ROOT);
    FileUtils.cleanDirectory(rootFile);

    for (Map.Entry<UUID, Map<FeatureExpr, List<Double>>> entry : comparedModels.entrySet()) {
      this.checkForDifferentLocalModels(entry.getKey(), entry.getValue());

      String result = "constraint,m1,m2,diff\n";
      result += this.parseComparedModel(entry.getValue());

      File outputFile =
          new File(
              OUTPUT_DIR
                  + "/"
                  + this.getProgramName()
                  + "/"
                  + ModelsEvaluation.COMPARISON_ROOT
                  + "/"
                  + entry.getKey()
                  + ModelsEvaluation.DOT_CSV);
      outputFile.getParentFile().mkdirs();
      PrintWriter writer = new PrintWriter(outputFile);
      writer.write(result);
      writer.flush();
      writer.close();
    }
  }

  private void checkForDifferentLocalModels(
      UUID region, Map<FeatureExpr, List<Double>> comparedModel) {
    this.checkPerfIntensiveModels(region, comparedModel);

    boolean areModelsDifferent = false;
    StringBuilder message = new StringBuilder();
    message.append("The local models ");
    message.append(region);
    message.append(" are different");
    message.append("\n");

    for (Map.Entry<FeatureExpr, List<Double>> entry : comparedModel.entrySet()) {
      List<Double> perfs = entry.getValue();
      double perfDiff = Math.abs(perfs.get(0) - perfs.get(1)) / 1E9;

      if (perfDiff >= this.getDiffThreshold()) {
        areModelsDifferent = true;
        message.append("Diff of ");
        message.append(ModelsEvaluation.DECIMAL_FORMAT.format(perfDiff));
        message.append(" in ");
        double m1 = perfs.get(0) / 1E9;
        message.append(ModelsEvaluation.DECIMAL_FORMAT.format(m1));
        message.append(" vs ");
        double m2 = perfs.get(1) / 1E9;
        message.append(ModelsEvaluation.DECIMAL_FORMAT.format(m2));

        message.append("\n");
      }
    }

    if (areModelsDifferent) {
      System.err.println(message);
    }
  }

  private void checkPerfIntensiveModels(UUID region, Map<FeatureExpr, List<Double>> comparedModel) {
    for (Map.Entry<FeatureExpr, List<Double>> entry : comparedModel.entrySet()) {
      List<Double> perfs = entry.getValue();
      double m1 = perfs.get(0) / 1E9;
      double m2 = perfs.get(1) / 1E9;

      if (m1 >= this.getPerfIntensiveThreshold() || m2 >= this.getPerfIntensiveThreshold()) {
        System.out.println(
            "There is at least one entry in the local model "
                + region
                + " that contributes at least "
                + this.getPerfIntensiveThreshold()
                + " to the performance of the system");
        break;
      }
    }
  }

  private String parseComparedModel(Map<FeatureExpr, List<Double>> comparedModel) {
    StringBuilder result = new StringBuilder();

    for (Map.Entry<FeatureExpr, List<Double>> entry : comparedModel.entrySet()) {
      String constraint = ConstraintUtils.prettyPrintFeatureExpr(entry.getKey(), this.getOptions());
      result.append(constraint);
      result.append(",");

      List<Double> perfs = entry.getValue();
      double m1 = perfs.get(0) / 1E9;
      result.append(ModelsEvaluation.DECIMAL_FORMAT.format(m1));
      result.append(",");
      double m2 = perfs.get(1) / 1E9;
      result.append(ModelsEvaluation.DECIMAL_FORMAT.format(m2));
      result.append(",");
      double perfDiff = Math.abs(perfs.get(0) - perfs.get(1)) / 1E9;
      result.append(ModelsEvaluation.DECIMAL_FORMAT.format(perfDiff));
      result.append("\n");
    }

    return result.toString();
  }
}
