package edu.cmu.cs.mvelezce.evaluation.phosphor;

import com.bpodgursky.jbool_expressions.Expression;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.DynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.BFPhosphorAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ExecVarCtx;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.SinkData;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhosphorResultAnalyzer {

//  private static final String OUTPUT_DIR = Options.DIRECTORY + "/evaluation/phosphor/programs";

  private final String programName;
  private final List<String> options;

  PhosphorResultAnalyzer(String programName, List<String> options) {
    this.programName = programName;
    this.options = options;
  }

  void analyze() throws IOException, InterruptedException {
    Map<JavaRegion, DecisionInfo> specRes = this.readSpecResults();
    Map<JavaRegion, SinkData> phosphorRes = this.readPhosphorResults();

    Set<JavaRegion> specRegionsAnalyzed = this.compareSpecToPhosphor(specRes, phosphorRes);
    this.checkMissingPhosphorRegions(phosphorRes.keySet(), specRegionsAnalyzed);
  }

  private void checkMissingPhosphorRegions(Set<JavaRegion> phosphorRegions,
      Set<JavaRegion> specRegionsAnalyzed) {
    for (JavaRegion phosphorRegion : phosphorRegions) {
      if (!specRegionsAnalyzed.contains(phosphorRegion)) {
        System.out.println(phosphorRegion);
        System.out.println("Region not found in spec");
        throw new UnsupportedOperationException("Add code to show the information from the spec");
      }

    }
  }

  private Set<JavaRegion> compareSpecToPhosphor(Map<JavaRegion, DecisionInfo> specRes,
      Map<JavaRegion, SinkData> phosphorRes) {
    Set<JavaRegion> specAnalyzedRegions = new HashSet<>();

    StringBuilder allErrors = new StringBuilder();

    for (Map.Entry<JavaRegion, DecisionInfo> specEntry : specRes.entrySet()) {
      JavaRegion specRegion = specEntry.getKey();
      specAnalyzedRegions.add(specRegion);

      SinkData phosphorSinkData = phosphorRes.get(specRegion);
      StringBuilder errors = new StringBuilder();

      if (phosphorSinkData == null) {
        errors.append(this.checkMissingRegion(specEntry.getValue()));
      }
      else {
        errors.append(this.compareRegions(specEntry.getValue(), phosphorSinkData));
      }

      if (errors.length() > 0) {
        allErrors.append("Region: ");
        allErrors.append(specRegion);
        allErrors.append("\n");
        allErrors.append(errors);
        allErrors.append("\n");
      }
    }

    if (allErrors.length() > 0) {
      System.out.println("####################################");
      System.out.println(this.programName);
      System.out.println();
      System.out.println(allErrors);
    }

    return specAnalyzedRegions;
  }

  private String checkMissingRegion(DecisionInfo specDecisionInfo) {
    Map<Expression<String>, Set<Set<String>>> ctxToOptions = this
        .getSpecCtxToOptions(specDecisionInfo);

    boolean ctxTrue =
        ctxToOptions.size() == 1 && ctxToOptions.keySet().iterator().next().toLexicographicString()
            .equals("true");
    boolean noOptions =
        ctxToOptions.values().size() == 1 & ctxToOptions.values().iterator().next().size() == 1
            && ctxToOptions.values().iterator().next().iterator().next().isEmpty();

    if (ctxTrue && noOptions) {
      return "";
    }

    StringBuilder errors = new StringBuilder();

    errors.append("Region not found in phosphor");
    errors.append("\n");
    errors.append(ctxToOptions);
    errors.append("\n");

    return errors.toString();
  }

  private String compareRegions(DecisionInfo specDecisionInfo, SinkData phosphorSinkData) {
    throw new UnsupportedOperationException("Implement");
//    Map<List<String>, VariabilityCtx> callingCtxsToVariabilityCtxs = specDecisionInfo
//        .getCallingCtxsToVariabilityCtxs();
//    Map<ExecVarCtx, Set<Set<String>>> data = phosphorSinkData.getData();
//
//    StringBuilder errors = new StringBuilder();
//    String ctxErrors = this
//        .compareVariabilityCtxs(callingCtxsToVariabilityCtxs.values(), data.keySet());
//    errors.append(ctxErrors);
//
//    String optionsErrors = this.compareOptions(specDecisionInfo, data);
//    errors.append(optionsErrors);
//
//    return errors.toString();
  }

  private String compareOptions(DecisionInfo specInfo,
      Map<ExecVarCtx, Set<Set<String>>> phosphorData) {
    Map<Expression<String>, Set<Set<String>>> specCtxToOptions = this.getSpecCtxToOptions(specInfo);
    Map<Expression<String>, Set<Set<String>>> phosphorCtxToOptions = this
        .getPhosphorCtxToOptions(phosphorData);

    StringBuilder errors = new StringBuilder();

    if (!specCtxToOptions.equals(phosphorCtxToOptions)) {
      errors.append(this.compareConfigs(specCtxToOptions, phosphorCtxToOptions));
    }

    return errors.toString();
  }

  private String compareConfigs(Map<Expression<String>, Set<Set<String>>> specCtxToOptions,
      Map<Expression<String>, Set<Set<String>>> phosphorCtxToOptions) {
    Set<Map<String, Boolean>> specConfigs = this.getConfigs(specCtxToOptions);
    Set<Map<String, Boolean>> phosphorConfigs = this.getConfigs(phosphorCtxToOptions);

    StringBuilder errors = new StringBuilder();

    if (specConfigs.equals(phosphorConfigs)) {
      return errors.toString();
    }

    errors.append("WARNING: The options are different");
    errors.append("\n");
    errors.append("Spec: ");
    errors.append(specCtxToOptions);
    errors.append("\n");
    errors.append("Phosphor: ");
    errors.append(phosphorCtxToOptions);
    errors.append("\n");

    Set<Map<String, Boolean>> missing = new HashSet<>();

    for (Map<String, Boolean> specConfig : specConfigs) {
      if (!phosphorConfigs.contains(specConfig)) {
        missing.add(specConfig);
      }
    }

    if (!missing.isEmpty()) {
      errors.append("Spec will sample the following ");
      errors.append(missing.size());
      errors.append(" configurations, but phosphor will not");
      errors.append("\n");
      errors.append(missing);
      errors.append("\n");
    }

    missing = new HashSet<>();

    for (Map<String, Boolean> phosphorConfig : phosphorConfigs) {
      if (!specConfigs.contains(phosphorConfig)) {
        missing.add(phosphorConfig);
      }
    }

    if (!missing.isEmpty()) {
      errors.append("Phosphor will sample the following ");
      errors.append(missing.size());
      errors.append(" configurations, but spec will not");
      errors.append("\n");
      errors.append(missing);
      errors.append("\n");
    }

    return errors.toString();
  }

  private Set<Map<String, Boolean>> getConfigs(
      Map<Expression<String>, Set<Set<String>>> ctxToOptions) {
    Set<Map<String, Boolean>> configs = new HashSet<>();

    for (Map.Entry<Expression<String>, Set<Set<String>>> entry : ctxToOptions.entrySet()) {
      Expression<String> expr = entry.getKey();
      Map<String, Boolean> ctxConfig = this.getCtxConfig(expr);

      Set<Map<String, Boolean>> optionsConfigs = this.getOptionsConfigs(entry.getValue());

      if (ctxConfig.isEmpty() && optionsConfigs.isEmpty()) {
        continue;
      }

      if (optionsConfigs.isEmpty()) {
        for (String option : this.options) {
          ctxConfig.putIfAbsent(option, false);
        }

        configs.add(ctxConfig);
      }
      else {
        for (Map<String, Boolean> optionsConfig : optionsConfigs) {
          Map<String, Boolean> config = new HashMap<>();
          config.putAll(optionsConfig);
          config.putAll(ctxConfig);

          configs.add(config);
        }
      }
    }

    return configs;
  }

  private Set<Map<String, Boolean>> getOptionsConfigs(Set<Set<String>> optionsSets) {
    Set<Map<String, Boolean>> configs = new HashSet<>();

    for (Set<String> options : optionsSets) {
      if (options.isEmpty()) {
        continue;
      }

      Set<Set<String>> allConfigs = Helper.getConfigurations(options);

      for (Set<String> configSet : allConfigs) {
        Map<String, Boolean> config = new HashMap<>();

        for (String option : this.options) {
          config.put(option, configSet.contains(option));
        }

        configs.add(config);
      }
    }

    return configs;
  }

  private Map<String, Boolean> getCtxConfig(Expression<String> expr) {
    Map<String, Boolean> config = new HashMap<>();
    String ctx = expr.toLexicographicString();

    if (ctx.equals("true")) {
      return config;
    }

    if (ctx.contains("|")) {
      throw new RuntimeException("The ctx " + ctx + " has OR");
    }

    ctx = ctx.replace("(", "");
    ctx = ctx.replace(")", "");
    String[] options = ctx.split("&");

    for (String option : options) {
      option = option.trim();
      int optionLength = option.length();

      if (optionLength < 1 || optionLength > 2) {
        throw new RuntimeException("The option " + option + " does not have the expected format");
      }

      config.put(String.valueOf(option.charAt(option.length() - 1)), !option.contains("!"));
    }

    return config;
  }

  private Map<Expression<String>, Set<Set<String>>> getSpecCtxToOptions(DecisionInfo specInfo) {
    Map<Expression<String>, Set<Set<String>>> cnfCtxsToOptions = new HashMap<>();

    Map<List<String>, VariabilityCtx> callingCtxsToVariabilityCtxs = specInfo
        .getCallingCtxsToVariabilityCtxs();
    Map<List<String>, DecisionBranchCountTable> callingCtxsToTables = specInfo
        .getCallingCtxsToDecisionBranchTables();

    for (VariabilityCtx variabilityCtx : callingCtxsToVariabilityCtxs.values()) {
      Expression<String> cnf = DecisionInfo.toCNF(variabilityCtx, this.options);
      Set<Set<String>> optionsSet = new HashSet<>();
      cnfCtxsToOptions.put(cnf, optionsSet);
    }

    // TODO test that there is a different trace, but the same ctx, which then leads to two entries with the same ctx
    for (Map.Entry<List<String>, VariabilityCtx> entry : callingCtxsToVariabilityCtxs.entrySet()) {
      VariabilityCtx ctx = entry.getValue();
      Expression<String> cnf = DecisionInfo.toCNF(ctx, this.options);
      Set<Set<String>> optionsSet = cnfCtxsToOptions.get(cnf);

      DecisionBranchCountTable table = callingCtxsToTables.get(entry.getKey());
      Set<String> options = SpecificationAnalysis.getMinimalSetOfOptions(table);
      optionsSet.add(options);
    }

    return cnfCtxsToOptions;
  }

  private Map<Expression<String>, Set<Set<String>>> getPhosphorCtxToOptions(
      Map<ExecVarCtx, Set<Set<String>>> phosphorData) {
    Map<Expression<String>, Set<Set<String>>> cnfCtxsToOptions = new HashMap<>();

    for (Map.Entry<ExecVarCtx, Set<Set<String>>> entry : phosphorData.entrySet()) {
      Expression<String> cnf = entry.getKey().toCNF();
      cnfCtxsToOptions.put(cnf, entry.getValue());
    }

    return cnfCtxsToOptions;
  }

  private String compareVariabilityCtxs(Collection<VariabilityCtx> specCtxs,
      Set<ExecVarCtx> phosphorCtxs) {
    Set<Expression<String>> specCNFCtxs = this.getSpecCNFCtxs(specCtxs);
    Set<Expression<String>> phosphorCNFCtxs = this.getPhosphorCNFCtxs(phosphorCtxs);

    StringBuilder errors = new StringBuilder();

    if (!specCNFCtxs.equals(phosphorCNFCtxs)) {
      errors.append("WARNING: ctxs are different");
      errors.append("\n");
      errors.append("Spec: ");
      errors.append(specCNFCtxs);
      errors.append("\n");
      errors.append("Phosphor: ");
      errors.append(phosphorCNFCtxs);
      errors.append("\n");
    }

    return errors.toString();
  }

  private Set<Expression<String>> getPhosphorCNFCtxs(Set<ExecVarCtx> phosphorCtxs) {
    Set<Expression<String>> phosphorCNFCtxs = new HashSet<>();

    for (ExecVarCtx phosphorCtx : phosphorCtxs) {
      Expression<String> phosphorCNF = phosphorCtx.toCNF();
      phosphorCNFCtxs.add(phosphorCNF);
    }

    return phosphorCNFCtxs;
  }


  private Set<Expression<String>> getSpecCNFCtxs(Collection<VariabilityCtx> specCtxs) {
    Set<Expression<String>> specCNFCtxs = new HashSet<>();

    for (VariabilityCtx specCtx : specCtxs) {
      Expression<String> specCNF = DecisionInfo.toCNF(specCtx, this.options);
      specCNFCtxs.add(specCNF);
    }

    return specCNFCtxs;
  }

  private Map<JavaRegion, DecisionInfo> readSpecResults()
      throws IOException, InterruptedException {
    DynamicRegionAnalysis<DecisionInfo> analysis = new SpecificationAnalysis(this.programName);
    String[] args = new String[0];
    return analysis.analyze(args);
  }

  private Map<JavaRegion, SinkData> readPhosphorResults()
      throws IOException, InterruptedException {
    throw new UnsupportedOperationException("The phosphor analysis does not return anything anymore.");
//    DynamicRegionAnalysis<SinkData> analysis = new BFPhosphorAnalysis(this.programName);
//    String[] args = new String[0];
//    return analysis.analyze(args);
  }

}
