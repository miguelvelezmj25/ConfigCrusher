package edu.cmu.cs.mvelezce.learning.builder;

import de.fosd.typechef.featureexpr.FeatureExpr;
import de.fosd.typechef.featureexpr.sat.SATFeatureExprFactory;
import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.builder.E2EModelBuilder;
import edu.cmu.cs.mvelezce.builder.constraint.BaseConstraintPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.learning.builder.generate.matlab.script.StepWiseLinearLearner;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.region.RegionsManager;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public abstract class BaseLinearLearnedModelBuilder extends E2EModelBuilder {

  public static final String OUTPUT_DIR = "../cc-perf-model-learning/" + Options.DIRECTORY;
  private static final String TERM_DELIMETER = ":";
  private static final String INTERCEPT = "(Intercept)";

  private final SamplingApproach samplingApproach;

  public BaseLinearLearnedModelBuilder(
      String programName, List<String> options, SamplingApproach samplingApproach) {
    super(programName, options, new HashSet<>());

    this.samplingApproach = samplingApproach;
    this.addProgramRegionToData();
  }

  private void addProgramRegionToData() {
    Set<FeatureExpr> linearModelConstraints =
        this.samplingApproach.getLinearModelConstraints(this.getOptions());
    linearModelConstraints.add(SATFeatureExprFactory.True());
    E2EModelBuilder.REGIONS_TO_DATA.put(RegionsManager.PROGRAM_REGION, linearModelConstraints);
  }

  @Override
  protected void populateLocalModel(LocalPerformanceModel<FeatureExpr> localModel) {
    this.clearStats(localModel);

    try {
      String rootDir =
          StepWiseLinearLearner.MATLAB_OUTPUT_DIR
              + "/"
              + this.getProgramName()
              + "/"
              + this.samplingApproach.getName()
              + "/";
      List<String> terms = this.parseFile(new File(rootDir + StepWiseLinearLearner.TERMS_FILE));
      List<String> coefs = this.parseFile(new File(rootDir + StepWiseLinearLearner.COEFS_FILE));

      if (terms.size() != coefs.size()) {
        throw new RuntimeException("The terms and coefs files are not the same length");
      }

      List<String> pValues =
          this.parseFile(new File(rootDir + StepWiseLinearLearner.P_VALUES_FILE));

      if (terms.size() != pValues.size()) {
        throw new RuntimeException("The terms and pValues files are not the same length");
      }

      List<Set<String>> parsedTerms = this.parseTerms(terms, this.getOptions());

      for (int i = 0; i < terms.size(); i++) {
        if (Double.parseDouble(pValues.get(i)) > 0.05) {
          continue;
        }

        FeatureExpr constraint = this.getConstraint(parsedTerms.get(i));
        Map<FeatureExpr, Double> perfModel = localModel.getModel();
        Map<FeatureExpr, String> perfModelHuman = localModel.getModelToPerfHumanReadable();

        for (FeatureExpr modelConstraint : perfModel.keySet()) {
          if (!constraint.equiv(modelConstraint).isTautology()) {
            continue;
          }

          perfModel.put(modelConstraint, Double.parseDouble(coefs.get(i)) * 1E9);
          perfModelHuman.put(modelConstraint, coefs.get(i));

          break;
        }
      }

      this.removeInsignificantEntries(localModel.getModel());
      this.removeInsignificantEntriesHuman(localModel.getModelToPerfHumanReadable());
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private void removeInsignificantEntries(Map<FeatureExpr, Double> model) {
    Set<FeatureExpr> entries = new HashSet<>();

    for (Map.Entry<FeatureExpr, Double> entry : model.entrySet()) {
      if (entry.getValue() == BaseConstraintPerformanceModelBuilder.EMPTY_DOUBLE) {
        entries.add(entry.getKey());
      }
    }

    for (FeatureExpr entry : entries) {
      model.remove(entry);
    }
  }

  private void removeInsignificantEntriesHuman(Map<FeatureExpr, String> model) {
    Set<FeatureExpr> entries = new HashSet<>();

    for (Map.Entry<FeatureExpr, String> entry : model.entrySet()) {
      if (entry.getValue().isEmpty()) {
        entries.add(entry.getKey());
      }
    }

    for (FeatureExpr entry : entries) {
      model.remove(entry);
    }
  }

  private void clearStats(LocalPerformanceModel<FeatureExpr> localModel) {
    localModel.getModelToMin().clear();
    localModel.getModelToMax().clear();
    localModel.getModelToDiff().clear();
    localModel.getModelToSampleVariance().clear();
    localModel.getModelToConfidenceInterval().clear();
    localModel.getModelToMinHumanReadable().clear();
    localModel.getModelToMaxHumanReadable().clear();
    localModel.getModelToDiffHumanReadable().clear();
    localModel.getModelToSampleVarianceHumanReadble().clear();
    localModel.getModelToConfidenceIntervalHumanReadable().clear();
  }

  private FeatureExpr getConstraint(Set<String> terms) {
    if (terms.isEmpty()) {
      return SATFeatureExprFactory.True();
    }

    StringBuilder stringBuilder = new StringBuilder("(");
    Iterator<String> termIter = terms.iterator();

    while (termIter.hasNext()) {
      String term = termIter.next();
      stringBuilder.append(term);

      if (termIter.hasNext()) {
        stringBuilder.append(" && ");
      }
    }

    stringBuilder.append(")");

    return FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringBuilder.toString());
  }

  private List<Set<String>> parseTerms(List<String> rawTerms, List<String> options) {
    List<Set<String>> result = new ArrayList<>();

    for (String raw : rawTerms) {
      String[] terms = raw.split(TERM_DELIMETER);
      Set<String> optionsInTerms = new HashSet<>();

      for (String rawTerm : terms) {
        if (rawTerm.equals(INTERCEPT)) {
          continue;
        }

        String termIndexString = rawTerm.substring(1);
        Integer termIndex = Integer.valueOf(termIndexString);
        String term = options.get(termIndex - 1);
        optionsInTerms.add(term);
      }

      result.add(optionsInTerms);
    }

    return result;
  }

  private List<String> parseFile(File file) throws IOException {
    List<String> lines = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = reader.readLine()) != null) {
      if (line.isEmpty()) {
        continue;
      }

      lines.add(line.trim());
    }

    reader.close();

    return lines;
  }

  protected SamplingApproach getSamplingApproach() {
    return samplingApproach;
  }
}
