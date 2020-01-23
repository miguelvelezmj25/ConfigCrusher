package edu.cmu.cs.mvelezce.eval.java.influence;

import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

public class InfluenceEvaluation {

  private static final String OUTPUT_DIR =
      "../cc-eval/" + Options.DIRECTORY + "/eval/java/programs/influence";
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");
  private static final String DOT_CSV = ".csv";
  private static final String COMPARISON_ROOT = "comparison";

  private final String programName;
  private final PerformanceModel<Set<String>> model1;
  private final PerformanceModel<Set<String>> model2;
  private final double diffThreshold;
  private final double perfIntensiveThreshold;

  public InfluenceEvaluation(
      String programName,
      PerformanceModel<Set<String>> model1,
      PerformanceModel<Set<String>> model2,
      double diffThreshold,
      double perfIntensiveThreshold) {
    this.programName = programName;
    this.model1 = model1;
    this.model2 = model2;
    this.diffThreshold = diffThreshold;
    this.perfIntensiveThreshold = perfIntensiveThreshold;
  }

  public void compare() throws IOException {
    Map<UUID, Map<Set<String>, Double>> regionsToModels1 = this.getRegionsToModels(this.model1);
    Map<UUID, Map<Set<String>, Double>> regionsToModels2 = this.getRegionsToModels(this.model2);

    Set<UUID> regions = new HashSet<>();
    regions.addAll(regionsToModels1.keySet());
    regions.addAll(regionsToModels2.keySet());

    Map<UUID, Set<Set<String>>> allModelEntries =
        this.getAllModelEntries(regions, regionsToModels1, regionsToModels2);
    Map<UUID, Map<Set<String>, List<Double>>> comparedModels =
        this.compareInfluences(allModelEntries, regionsToModels1, regionsToModels2);
    this.saveComparedModels(comparedModels);
  }

  private Map<UUID, Map<Set<String>, List<Double>>> compareInfluences(
      Map<UUID, Set<Set<String>>> allModelEntries,
      Map<UUID, Map<Set<String>, Double>> regionsToModels1,
      Map<UUID, Map<Set<String>, Double>> regionsToModels2) {
    Map<UUID, Map<Set<String>, List<Double>>> regionsToComparedInfluences = new HashMap<>();

    for (Map.Entry<UUID, Set<Set<String>>> entry : allModelEntries.entrySet()) {
      LinkedHashMap<Set<String>, List<Double>> comparedInfluences = new LinkedHashMap<>();

      for (Set<String> term : entry.getValue()) {
        comparedInfluences.put(term, new ArrayList<>());
      }

      regionsToComparedInfluences.put(entry.getKey(), comparedInfluences);
    }

    for (UUID region : allModelEntries.keySet()) {
      Map<Set<String>, Double> model1 = regionsToModels1.get(region);
      Map<Set<String>, Double> model2 = regionsToModels2.get(region);
      Map<Set<String>, List<Double>> comparedInfluences = regionsToComparedInfluences.get(region);

      for (Map.Entry<Set<String>, List<Double>> entry : comparedInfluences.entrySet()) {
        Set<String> term = entry.getKey();
        List<Double> perfs = entry.getValue();
        perfs.add(model1.get(term));
        perfs.add(model2.get(term));
      }
    }

    return regionsToComparedInfluences;
  }

  private Map<UUID, Set<Set<String>>> getAllModelEntries(
      Set<UUID> regions,
      Map<UUID, Map<Set<String>, Double>> regionsToModels1,
      Map<UUID, Map<Set<String>, Double>> regionsToModels2) {
    Map<UUID, Set<Set<String>>> regionsToEntries = new HashMap<>();

    for (UUID region : regions) {
      regionsToEntries.putIfAbsent(region, new LinkedHashSet<>());

      regionsToEntries.get(region).addAll(regionsToModels1.get(region).keySet());
      regionsToEntries.get(region).addAll(regionsToModels2.get(region).keySet());
    }

    return regionsToEntries;
  }

  private Map<UUID, Map<Set<String>, Double>> getRegionsToModels(
      PerformanceModel<Set<String>> model) {
    Set<LocalPerformanceModel<Set<String>>> localModels = model.getLocalModels();
    Map<UUID, Map<Set<String>, Double>> regionsToModels = new HashMap<>();

    for (LocalPerformanceModel<Set<String>> localModel : localModels) {
      UUID region = localModel.getRegion();
      Map<Set<String>, Double> perfModel = localModel.getModel();

      regionsToModels.put(region, perfModel);
    }

    return regionsToModels;
  }

  protected void saveComparedModels(Map<UUID, Map<Set<String>, List<Double>>> comparedModels)
      throws IOException {
    File rootFile = new File(OUTPUT_DIR + "/" + this.programName + "/" + COMPARISON_ROOT);
    FileUtils.cleanDirectory(rootFile);

    for (Map.Entry<UUID, Map<Set<String>, List<Double>>> entry : comparedModels.entrySet()) {
      this.checkForDifferentLocalModels(entry.getKey(), entry.getValue());

      String result = "term,m1,m2,diff\n";
      result += this.parseComparedModel(entry.getValue());

      File outputFile =
          new File(
              OUTPUT_DIR
                  + "/"
                  + this.programName
                  + "/"
                  + COMPARISON_ROOT
                  + "/"
                  + entry.getKey()
                  + DOT_CSV);
      outputFile.getParentFile().mkdirs();
      PrintWriter writer = new PrintWriter(outputFile);
      writer.write(result);
      writer.flush();
      writer.close();
    }
  }

  private void checkForDifferentLocalModels(
      UUID region, Map<Set<String>, List<Double>> comparedModel) {
    this.checkPerfIntensiveModels(region, comparedModel);

    boolean areModelsDifferent = false;
    StringBuilder message = new StringBuilder();
    message.append("The local models ");
    message.append(region);
    message.append(" are different");
    message.append("\n");

    for (Map.Entry<Set<String>, List<Double>> entry : comparedModel.entrySet()) {
      List<Double> perfs = entry.getValue();
      double perfDiff = Math.abs(perfs.get(0) - perfs.get(1)) / 1E9;

      if (perfDiff >= this.diffThreshold) {
        areModelsDifferent = true;
        message.append("Diff of ");
        message.append(DECIMAL_FORMAT.format(perfDiff));
        message.append(" in ");
        double m1 = perfs.get(0) / 1E9;
        message.append(DECIMAL_FORMAT.format(m1));
        message.append(" vs ");
        double m2 = perfs.get(1) / 1E9;
        message.append(DECIMAL_FORMAT.format(m2));

        message.append("\n");
      }
    }

    if (areModelsDifferent) {
      System.err.println(message);
    }
  }

  private void checkPerfIntensiveModels(UUID region, Map<Set<String>, List<Double>> comparedModel) {
    for (Map.Entry<Set<String>, List<Double>> entry : comparedModel.entrySet()) {
      List<Double> perfs = entry.getValue();
      double m1 = Math.abs(perfs.get(0)) / 1E9;
      double m2 = Math.abs(perfs.get(1)) / 1E9;

      if (m1 >= this.perfIntensiveThreshold || m2 >= this.perfIntensiveThreshold) {
        System.out.println(
            "There is at least one entry in the local model "
                + region
                + " that contributes at least "
                + this.perfIntensiveThreshold
                + " to the performance of the system");
        break;
      }
    }
  }

  private String parseComparedModel(Map<Set<String>, List<Double>> comparedModel) {
    StringBuilder result = new StringBuilder();

    for (Map.Entry<Set<String>, List<Double>> entry : comparedModel.entrySet()) {
      result.append("\"");
      result.append(entry.getKey());
      result.append("\"");
      result.append(",");

      List<Double> perfs = entry.getValue();
      double m1 = perfs.get(0) / 1E9;
      result.append(DECIMAL_FORMAT.format(m1));
      result.append(",");
      double m2 = perfs.get(1) / 1E9;
      result.append(DECIMAL_FORMAT.format(m2));
      result.append(",");
      double perfDiff = Math.abs(perfs.get(0) - perfs.get(1)) / 1E9;
      result.append(DECIMAL_FORMAT.format(perfDiff));
      result.append("\n");
    }

    return result.toString();
  }
}
