package edu.cmu.cs.mvelezce.evaluation.phosphor;

import com.opencsv.CSVWriter;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.BFPhosphorAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorAnalysis;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;

public class PhosphorResultAnalyzer {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/evaluation/phosphor/programs";

  private final String programName;

  PhosphorResultAnalyzer(String programName) {
    this.programName = programName;
  }

  void analyze() throws IOException, InterruptedException {
    Map<JavaRegion, Set<Constraint>> bfResults = this.readBFPhosphorResults();
    Map<JavaRegion, Set<Constraint>> ccResults = this.readCCPhosphorResults();

    Set<JavaRegion> sinks = new HashSet<>(bfResults.keySet());
    sinks.addAll(ccResults.keySet());

    List<String[]> data = new ArrayList<>();
    data.add(new String[]{"Sink", "BF Taints", "BF Ctx", "CC Taints", "CC Ctx", "Equal Taints",
        "Equal Ctxs", "Missing taints from CC", "Missing taints from BF", "Missing ctx from CC",
        "Missing ctx from BF"});

    for (JavaRegion region : sinks) {
      List<String> entryList = this.buildEntry(region, bfResults, ccResults);
      String[] entry = new String[entryList.size()];
      entry = entryList.toArray(entry);
      data.add(entry);
    }

    this.writeToCSVFile(data);
  }

  private List<String> buildEntry(JavaRegion region,
      Map<JavaRegion, Set<Constraint>> bfResults,
      Map<JavaRegion, Set<Constraint>> ccResults) {
    List<String> entry = new ArrayList<>();

    // "Sink"
    String sink = this.getSink(region);
    entry.add(sink);

    // "BF Taints"
    Set<Constraint> bfConstraints = bfResults.get(region);
    Set<String> bfTaints = this.getTaintsFromTaints(bfConstraints);
    entry.add(bfTaints.toString());

    // "BF Ctx"
    Set<String> bfContext = this.getTaintsFromContext(bfConstraints);
    entry.add(bfContext.toString());

    // "CC Taints"
    Set<Constraint> ccConstraints = ccResults.get(region);
    Set<String> ccTaints = this.getTaintsFromTaints(ccConstraints);
    entry.add(ccTaints.toString());

    // "CC Ctx"
    Set<String> ccContext = this.getTaintsFromContext(ccConstraints);
    entry.add(ccContext.toString());

    //"Equal Taints"
    entry.add(String.valueOf(bfTaints.equals(ccTaints)));
    // "Equal Ctxs"
    entry.add(String.valueOf(bfContext.equals(ccContext)));

    // "Missing taints from CC"
    Set<String> missingTaintsFromCC = new HashSet<>(ccTaints);
    missingTaintsFromCC.removeAll(bfTaints);
    entry.add(missingTaintsFromCC.toString());

    // "Missing taints from BF"
    Set<String> missingTaintsFromBF = new HashSet<>(bfTaints);
    missingTaintsFromBF.removeAll(ccTaints);
    entry.add(missingTaintsFromBF.toString());

    // "Missing ctx from CC"
    Set<String> missingCtxFromCC = new HashSet<>(ccContext);
    missingCtxFromCC.removeAll(bfContext);
    entry.add(missingCtxFromCC.toString());

    // "Missing ctx from BF"
    Set<String> missingCtxFromBF = new HashSet<>(bfContext);
    missingCtxFromBF.removeAll(ccContext);
    entry.add(missingCtxFromBF.toString());

    return entry;
  }

  private Set<String> getTaintsFromTaints(@Nullable Set<Constraint> constraints) {
    Set<String> taints = new HashSet<>();

    if (constraints == null) {
      return taints;
    }

    for (Constraint constraint : constraints) {
      Map<String, Boolean> taintsFromTaints = constraint.getPartialConfig();

      for (Map.Entry<String, Boolean> entry : taintsFromTaints.entrySet()) {
        String taintValue = entry.getKey() + "=" + entry.getValue();
        taints.add(taintValue);
      }
    }

    return taints;
  }

  private Set<String> getTaintsFromContext(@Nullable Set<Constraint> constraints) {
    Set<String> taints = new HashSet<>();

    if (constraints == null) {
      return taints;
    }

    for (Constraint constraint : constraints) {
      Map<String, Boolean> taintsFromContext = constraint.getContext();

      for (Map.Entry<String, Boolean> entry : taintsFromContext.entrySet()) {
        String contextValue = entry.getKey() + "=" + entry.getValue();
        taints.add(contextValue);
      }
    }

    if (taints.isEmpty()) {
      taints.add("TRUE");
    }

    return taints;
  }

  private void writeToCSVFile(List<String[]> data) throws IOException {
    File outputDir = new File(PhosphorResultAnalyzer.OUTPUT_DIR + "/" + this.programName);

    if (outputDir.exists()) {
      FileUtils.forceDelete(outputDir);
    }

    outputDir.mkdirs();

    FileWriter outputFile = new FileWriter(outputDir + "/compare.csv");
    CSVWriter writer = new CSVWriter(outputFile);
    writer.writeAll(data);
    writer.close();
  }

  private String getSink(JavaRegion region) {
    return region.getRegionPackage()
        + "."
        + region.getRegionClass()
        + "."
        + region.getRegionMethod()
        + "."
        + region.getStartRegionIndex();
  }

  Map<JavaRegion, Set<Constraint>> readCCPhosphorResults()
      throws IOException, InterruptedException {
    DynamicAnalysis<Set<Constraint>> analysis = new PhosphorAnalysis(this.programName);

    String[] args = new String[0];
    return analysis.analyze(args);
  }

  Map<JavaRegion, Set<Constraint>> readBFPhosphorResults()
      throws IOException, InterruptedException {
    DynamicAnalysis<Set<Constraint>> analysis = new BFPhosphorAnalysis(this.programName);

    String[] args = new String[0];
    return analysis.analyze(args);
  }

}
