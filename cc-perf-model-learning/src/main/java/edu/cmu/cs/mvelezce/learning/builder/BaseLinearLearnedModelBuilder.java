package edu.cmu.cs.mvelezce.learning.builder;

import edu.cmu.cs.mvelezce.approaches.sampling.SamplingApproach;
import edu.cmu.cs.mvelezce.builder.E2EModelBuilder;
import edu.cmu.cs.mvelezce.builder.partition.BasePartitionPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.PartialPartition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.learning.builder.generate.matlab.script.StepWiseLinearLearner;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.region.manager.RegionsManager;
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
      String programName,
      List<String> options,
      SamplingApproach samplingApproach,
      String measuredTime) {
    super(programName, options, new HashSet<>(), measuredTime);

    this.samplingApproach = samplingApproach;
    this.addProgramRegionToData();
  }

  @Override
  protected void addProgramRegionToData() {
    Set<Partition> linearModelPartitions =
        this.samplingApproach.getLinearModelPartitions(this.getOptions());
    linearModelPartitions.add(new Partition(FeatureExprUtils.getTrue(IDTA.USE_BDD)));
    E2EModelBuilder.REGIONS_TO_DATA.put(
        RegionsManager.PROGRAM_REGION, new PartialPartition(linearModelPartitions));
  }

  @Override
  protected void populateLocalModel(LocalPerformanceModel<Partition> localModel) {
    this.clearStats(localModel);

    try {
      String rootDir =
          StepWiseLinearLearner.MATLAB_OUTPUT_DIR
              + "/"
              + this.getProgramName()
              + "/"
              + this.getMeasuredTime()
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

        Partition partition = this.getPartition(parsedTerms.get(i));
        Map<Partition, Double> perfModel = localModel.getModel();
        Map<Partition, String> perfModelHuman = localModel.getModelToPerfHumanReadable();

        for (Partition modelPartition : perfModel.keySet()) {
          if (!partition.getFeatureExpr().equiv(modelPartition.getFeatureExpr()).isTautology()) {
            continue;
          }

          perfModel.put(modelPartition, Double.parseDouble(coefs.get(i)) * 1E9);
          perfModelHuman.put(modelPartition, coefs.get(i));

          break;
        }
      }

      this.removeInsignificantEntries(localModel.getModel());
      this.removeInsignificantEntriesHuman(localModel.getModelToPerfHumanReadable());
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private void removeInsignificantEntries(Map<Partition, Double> model) {
    Set<Partition> entries = new HashSet<>();

    for (Map.Entry<Partition, Double> entry : model.entrySet()) {
      if (entry.getValue() == BasePartitionPerformanceModelBuilder.EMPTY_DOUBLE) {
        entries.add(entry.getKey());
      }
    }

    for (Partition entry : entries) {
      model.remove(entry);
    }
  }

  private void removeInsignificantEntriesHuman(Map<Partition, String> model) {
    Set<Partition> entries = new HashSet<>();

    for (Map.Entry<Partition, String> entry : model.entrySet()) {
      if (entry.getValue().isEmpty()) {
        entries.add(entry.getKey());
      }
    }

    for (Partition entry : entries) {
      model.remove(entry);
    }
  }

  private void clearStats(LocalPerformanceModel<Partition> localModel) {
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

  private Partition getPartition(Set<String> terms) {
    if (terms.isEmpty()) {
      return new Partition(FeatureExprUtils.getTrue(IDTA.USE_BDD));
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

    return new Partition(
        FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringBuilder.toString()));
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
