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
import org.apache.commons.io.FileUtils;

public class ResultAnalyzer {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/evaluation/phosphor/programs";

  private final String programName;

  ResultAnalyzer(String programName) {
    this.programName = programName;
  }

  void analyze() throws IOException {
    Map<JavaRegion, Set<Constraint>> bfResults = this.readBFPhosphorResults();
    Map<JavaRegion, Set<Constraint>> ccResults = this.readCCPhosphorResults();

    Set<JavaRegion> sinks = new HashSet<>(bfResults.keySet());
    sinks.addAll(ccResults.keySet());

    List<String[]> data = new ArrayList<>();
    data.add(new String[]{"Sink", "BF Taints", "BF Ctx", "CC Taints", "CC Ctx"});

    for (JavaRegion region : sinks) {
      List<String> entryList = new ArrayList<>();

      String sink = this.getSink(region);
      entryList.add(sink);

      Set<Constraint> bfConstraints = bfResults.get(region);
      Set<String> taints = this.getTaintsFromTaints(bfConstraints);
      entryList.add(taints.toString());
      Set<String> context = this.getTaintsFromContext(bfConstraints);
      entryList.add(context.toString());

      Set<Constraint> ccConstraints = bfResults.get(region);
      taints = this.getTaintsFromTaints(ccConstraints);
      entryList.add(taints.toString());
      context = this.getTaintsFromContext(ccConstraints);
      entryList.add(context.toString());

      String[] entry = new String[entryList.size()];
      entry = entryList.toArray(entry);
      data.add(entry);
    }

    this.writeToCSVFile(data);
  }

  private Set<String> getTaintsFromTaints(Set<Constraint> constraints) {
    Set<String> taints = new HashSet<>();

    for (Constraint constraint : constraints) {
      Map<String, Boolean> taintsFromTaints = constraint.getPartialConfig();

      for (Map.Entry<String, Boolean> entry : taintsFromTaints.entrySet()) {
        String taintValue = entry.getKey() + "=" + entry.getValue();
        taints.add(taintValue);
      }
    }

    return taints;
  }

  private Set<String> getTaintsFromContext(Set<Constraint> constraints) {
    Set<String> taints = new HashSet<>();

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
    File outputDir = new File(ResultAnalyzer.OUTPUT_DIR + "/" + this.programName);

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

  Map<JavaRegion, Set<Constraint>> readCCPhosphorResults() throws IOException {
    DynamicAnalysis<Set<Constraint>> analysis = new PhosphorAnalysis(this.programName);

    String[] args = new String[0];
    return analysis.analyze(args);
  }

  Map<JavaRegion, Set<Constraint>> readBFPhosphorResults() throws IOException {
    DynamicAnalysis<Set<Constraint>> analysis = new BFPhosphorAnalysis(this.programName);

    String[] args = new String[0];
    return analysis.analyze(args);
  }

}
