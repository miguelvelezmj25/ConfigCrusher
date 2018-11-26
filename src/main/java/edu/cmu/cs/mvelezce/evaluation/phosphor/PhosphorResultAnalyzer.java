package edu.cmu.cs.mvelezce.evaluation.phosphor;

import com.bpodgursky.jbool_expressions.Expression;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.BFPhosphorAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ExecVarCtx;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.SinkData;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.BranchCoverageAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.Context;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.DecisionBranchCountTable;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.DecisionInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth.DynamicAnalysisSpecification;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhosphorResultAnalyzer {

  private static final String OUTPUT_DIR = Options.DIRECTORY + "/evaluation/phosphor/programs";

  private final String programName;
  private final List<String> options;

  PhosphorResultAnalyzer(String programName, List<String> options) {
    this.programName = programName;
    this.options = options;
  }

  void analyze() throws IOException, InterruptedException {
    Map<JavaRegion, DecisionInfo> dynamicSpecRes = this.readDynamicSpecificationResults();
    Map<JavaRegion, SinkData> bfPhosphorRes = this.readBFPhosphorResults();
    Set<JavaRegion> specRegionsAnalyzed = this.compareSpecToPhosphor(dynamicSpecRes, bfPhosphorRes);
    this.checkMissingBFRegions(bfPhosphorRes, specRegionsAnalyzed);
  }

  private void checkMissingBFRegions(Map<JavaRegion, SinkData> bfPhosphorRes,
      Set<JavaRegion> specRegionsAnalyzed) {
    for (JavaRegion bfAnalyzedRegion : bfPhosphorRes.keySet()) {
      if (!specRegionsAnalyzed.contains(bfAnalyzedRegion)) {
        System.out.println(bfAnalyzedRegion);
        System.out.println("Region not found in spec");
      }

    }
  }

  private Set<JavaRegion> compareSpecToPhosphor(Map<JavaRegion, DecisionInfo> dynamicSpecRes,
      Map<JavaRegion, SinkData> bfPhosphorRes) {
    Set<JavaRegion> specAnalyzedRegions = new HashSet<>();

    for (Map.Entry<JavaRegion, DecisionInfo> dynamicSpec : dynamicSpecRes.entrySet()) {
      JavaRegion specRegion = dynamicSpec.getKey();
      specAnalyzedRegions.add(specRegion);

      SinkData bfSinkData = bfPhosphorRes.get(specRegion);
      StringBuilder errors = new StringBuilder();

      if (bfSinkData == null) {
        errors.append("Region not found in bf");
      }
      else {
        errors.append(this.compareRegions(dynamicSpec.getValue(), bfSinkData));
      }

      if (errors.length() > 0) {
        System.out.println("Region: " + specRegion);
        System.out.println(errors);
        System.out.println();
      }
    }

    return specAnalyzedRegions;
  }

  private String compareRegions(DecisionInfo specDecisionInfo, SinkData bfSinkData) {
    Map<List<String>, Context> tracesToContexts = specDecisionInfo.getStackTracesToContexts();
    Map<ExecVarCtx, Set<Set<String>>> data = bfSinkData.getData();

    StringBuilder errors = new StringBuilder();
    String ctxErrors = this.compareCtxs(tracesToContexts.values(), data.keySet());
    errors.append(ctxErrors);
    String optionsErrors = this.compareOptions(specDecisionInfo, data);
    errors.append(optionsErrors);

    return errors.toString();
  }

  private String compareOptions(DecisionInfo specInfo, Map<ExecVarCtx, Set<Set<String>>> bfData) {
    Map<Expression<String>, Set<Set<String>>> specCtxToOptions = this.getCtxToOptions(specInfo);
    Map<Expression<String>, Set<Set<String>>> bfCtxToOptions = this.getCtxToOptions(bfData);

    StringBuilder errors = new StringBuilder();

    if (!specCtxToOptions.equals(bfCtxToOptions)) {
      errors.append("WARNING: The options are different");
      errors.append("\n");
      errors.append("Spec: ");
      errors.append(specCtxToOptions);
      errors.append("\n");
      errors.append("Bf: ");
      errors.append(bfCtxToOptions);
      errors.append("\n");
    }

    return errors.toString();
  }

  private Map<Expression<String>, Set<Set<String>>> getCtxToOptions(DecisionInfo specInfo) {
    Map<Expression<String>, Set<Set<String>>> cnfCtxsToOptions = new HashMap<>();

    Map<List<String>, Context> tracesToCtxs = specInfo.getStackTracesToContexts();
    Map<List<String>, DecisionBranchCountTable> tracesToTables = specInfo
        .getStackTracesToDecisionBranchTables();

    for(Context ctx : tracesToCtxs.values()) {
      Expression<String> cnf = DecisionInfo.toCNF(ctx, this.options);
      Set<Set<String>> optionsSet = new HashSet<>();
      cnfCtxsToOptions.put(cnf, optionsSet);
    }


    // TODO test that there is a different trace, but the same context, which then leads to two entries with the same context
    for (Map.Entry<List<String>, Context> entry : tracesToCtxs.entrySet()) {
      Context ctx = entry.getValue();
      Expression<String> cnf = DecisionInfo.toCNF(ctx, this.options);
      Set<Set<String>> optionsSet = cnfCtxsToOptions.get(cnf);

      DecisionBranchCountTable table = tracesToTables.get(entry.getKey());
      Set<String> options = DynamicAnalysisSpecification.getMinimalSetOfOptions(table);
      optionsSet.add(options);
    }

    return cnfCtxsToOptions;
  }

  private Map<Expression<String>, Set<Set<String>>> getCtxToOptions(
      Map<ExecVarCtx, Set<Set<String>>> bfData) {
    Map<Expression<String>, Set<Set<String>>> cnfCtxsToOptions = new HashMap<>();

    for (Map.Entry<ExecVarCtx, Set<Set<String>>> entry : bfData.entrySet()) {
      Expression<String> cnf = entry.getKey().toCNF();
      cnfCtxsToOptions.put(cnf, entry.getValue());
    }

    return cnfCtxsToOptions;
  }

  private String compareCtxs(Collection<Context> specCtxs, Set<ExecVarCtx> bfCtxs) {
    Set<Expression<String>> specCNFCtxs = this.getSpecCNFCtxs(specCtxs);
    Set<Expression<String>> bfCNFCtxs = this.getBFCNFCtxs(bfCtxs);

    StringBuilder errors = new StringBuilder();

    if (!specCNFCtxs.equals(bfCNFCtxs)) {
      errors.append("WARNING: contexts are different");
      errors.append("\n");
      errors.append("Spec: ");
      errors.append(specCNFCtxs);
      errors.append("\n");
      errors.append("BF: ");
      errors.append(bfCNFCtxs);
      errors.append("\n");
    }

    return errors.toString();
  }

  private Set<Expression<String>> getBFCNFCtxs(Set<ExecVarCtx> bfCtxs) {
    Set<Expression<String>> bfCNFCtxs = new HashSet<>();

    for (ExecVarCtx bfCtx : bfCtxs) {
      Expression<String> bfCNF = bfCtx.toCNF();
      bfCNFCtxs.add(bfCNF);
    }

    return bfCNFCtxs;
  }


  private Set<Expression<String>> getSpecCNFCtxs(Collection<Context> specCtxs) {
    Set<Expression<String>> specCNFCtxs = new HashSet<>();

    for (Context specCtx : specCtxs) {
      Expression<String> specCNF = DecisionInfo.toCNF(specCtx, this.options);
      specCNFCtxs.add(specCNF);
    }

    return specCNFCtxs;
  }

  private Map<JavaRegion, DecisionInfo> readDynamicSpecificationResults()
      throws IOException, InterruptedException {
    DynamicAnalysis<DecisionInfo> analysis = new BranchCoverageAnalysis(this.programName);
    String[] args = new String[0];
    return analysis.analyze(args);
  }

  private Map<JavaRegion, SinkData> readBFPhosphorResults()
      throws IOException, InterruptedException {
    DynamicAnalysis<SinkData> analysis = new BFPhosphorAnalysis(this.programName);
    String[] args = new String[0];
    return analysis.analyze(args);
  }

  //  void analyze() throws IOException, InterruptedException {
//    Map<JavaRegion, Set<Constraint>> bfResults = this.readBFPhosphorResults();
//    Map<JavaRegion, Set<Constraint>> ccResults = this.readCCPhosphorResults();
//
//    Set<JavaRegion> sinks = new HashSet<>(bfResults.keySet());
//    sinks.addAll(ccResults.keySet());
//
//    List<String[]> data = new ArrayList<>();
//    data.add(new String[]{"Sink", "BF Taints", "BF Ctx", "CC Taints", "CC Ctx", "Equal Taints",
//        "Equal Ctxs", "Missing taints from CC", "Missing taints from BF", "Missing ctx from CC",
//        "Missing ctx from BF"});
//
//    for (JavaRegion region : sinks) {
//      List<String> entryList = this.buildEntry(region, bfResults, ccResults);
//      String[] entry = new String[entryList.size()];
//      entry = entryList.toArray(entry);
//      data.add(entry);
//    }
//
//    this.writeToCSVFile(data);
//  }
//
//  private List<String> buildEntry(JavaRegion region,
//      Map<JavaRegion, Set<Constraint>> bfResults,
//      Map<JavaRegion, Set<Constraint>> ccResults) {
//    List<String> entry = new ArrayList<>();
//
//    // "Sink"
//    String sink = this.getSink(region);
//    entry.add(sink);
//
//    // "BF Taints"
//    Set<Constraint> bfConstraints = bfResults.get(region);
//    Set<String> bfTaints = this.getTaintsFromTaints(bfConstraints);
//    entry.add(bfTaints.toString());
//
//    // "BF Ctx"
//    Set<String> bfContext = this.getTaintsFromContext(bfConstraints);
//    entry.add(bfContext.toString());
//
//    // "CC Taints"
//    Set<Constraint> ccConstraints = ccResults.get(region);
//    Set<String> ccTaints = this.getTaintsFromTaints(ccConstraints);
//    entry.add(ccTaints.toString());
//
//    // "CC Ctx"
//    Set<String> ccContext = this.getTaintsFromContext(ccConstraints);
//    entry.add(ccContext.toString());
//
//    //"Equal Taints"
//    entry.add(String.valueOf(bfTaints.equals(ccTaints)));
//    // "Equal Ctxs"
//    entry.add(String.valueOf(bfContext.equals(ccContext)));
//
//    // "Missing taints from CC"
//    Set<String> missingTaintsFromCC = new HashSet<>(ccTaints);
//    missingTaintsFromCC.removeAll(bfTaints);
//    entry.add(missingTaintsFromCC.toString());
//
//    // "Missing taints from BF"
//    Set<String> missingTaintsFromBF = new HashSet<>(bfTaints);
//    missingTaintsFromBF.removeAll(ccTaints);
//    entry.add(missingTaintsFromBF.toString());
//
//    // "Missing ctx from CC"
//    Set<String> missingCtxFromCC = new HashSet<>(ccContext);
//    missingCtxFromCC.removeAll(bfContext);
//    entry.add(missingCtxFromCC.toString());
//
//    // "Missing ctx from BF"
//    Set<String> missingCtxFromBF = new HashSet<>(bfContext);
//    missingCtxFromBF.removeAll(ccContext);
//    entry.add(missingCtxFromBF.toString());
//
//    return entry;
//  }
//
//  private Set<String> getTaintsFromTaints(@Nullable Set<Constraint> constraints) {
//    Set<String> taints = new HashSet<>();
//
//    if (constraints == null) {
//      return taints;
//    }
//
//    for (Constraint constraint : constraints) {
//      Map<String, Boolean> taintsFromTaints = constraint.getPartialConfig();
//
//      for (Map.Entry<String, Boolean> entry : taintsFromTaints.entrySet()) {
//        String taintValue = entry.getKey() + "=" + entry.getValue();
//        taints.add(taintValue);
//      }
//    }
//
//    return taints;
//  }
//
//  private Set<String> getTaintsFromContext(@Nullable Set<Constraint> constraints) {
//    Set<String> taints = new HashSet<>();
//
//    if (constraints == null) {
//      return taints;
//    }
//
//    for (Constraint constraint : constraints) {
//      Map<String, Boolean> taintsFromContext = constraint.getContext();
//
//      for (Map.Entry<String, Boolean> entry : taintsFromContext.entrySet()) {
//        String contextValue = entry.getKey() + "=" + entry.getValue();
//        taints.add(contextValue);
//      }
//    }
//
//    if (taints.isEmpty()) {
//      taints.add("TRUE");
//    }
//
//    return taints;
//  }
//
//  private void writeToCSVFile(List<String[]> data) throws IOException {
//    File outputDir = new File(PhosphorResultAnalyzer.OUTPUT_DIR + "/" + this.programName);
//
//    if (outputDir.exists()) {
//      FileUtils.forceDelete(outputDir);
//    }
//
//    outputDir.mkdirs();
//
//    FileWriter outputFile = new FileWriter(outputDir + "/compare.csv");
//    CSVWriter writer = new CSVWriter(outputFile);
//    writer.writeAll(data);
//    writer.close();
//  }
//
//  private String getSink(JavaRegion region) {
//    return region.getRegionPackage()
//        + "."
//        + region.getRegionClass()
//        + "."
//        + region.getRegionMethod()
//        + "."
//        + region.getStartRegionIndex();
//  }
//

}
