package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.region;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicRegionAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.PhosphorTaintAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize.RegionToInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaintPhosphorAnalysis extends BaseDynamicRegionAnalysis<Set<Set<String>>> {

  private final PhosphorTaintAnalysis phosphorTaintAnalysis;

  public TaintPhosphorAnalysis(String programName) {
    super(programName, new HashSet<>(), new HashSet<>());

    this.phosphorTaintAnalysis = new PhosphorTaintAnalysis(programName, new ArrayList<>());
  }

  @Override
  public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException {
    Set<PhosphorControlFlowInfo> results = this.readPhosphorTaintResults();
    Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

    for (PhosphorControlFlowInfo result : results) {
      Set<InfluencingTaints> influencingTaints = result.getInfluencingTaints();
      Set<InfluencingTaints> newInfluencingTaints = this
          .removeContextTaintsInConditionTaints(influencingTaints);

      if (newInfluencingTaints.isEmpty()) {
        continue;
      }

      newInfluencingTaints = this.mergeEmptyContextTaints(newInfluencingTaints);
      Set<Set<String>> taints = this.mergeTaints(newInfluencingTaints);

      JavaRegion region = new JavaRegion.Builder(result.getPackageName(), result.getClassName(),
          result.getMethodSignature()).startBytecodeIndex(result.getDecisionIndex()).build();

      regionsToOptionsSet.put(region, taints);
    }

    return regionsToOptionsSet;
  }

  private Set<InfluencingTaints> mergeEmptyContextTaints(
      Set<InfluencingTaints> influencingTaintsSet) {
    if (influencingTaintsSet.size() == 1) {
      return influencingTaintsSet;
    }

    Set<Set<String>> conditionsOfTaintsWithEmptyContexts = this
        .getConditionsOfTaintsWithEmptyContexts(influencingTaintsSet);

    if (conditionsOfTaintsWithEmptyContexts.size() <= 1) {
      return influencingTaintsSet;
    }

    Set<String> mergedConditions = this
        .mergeConditionsOfTaintsWithEmptyContexts(conditionsOfTaintsWithEmptyContexts);

    Set<InfluencingTaints> newInfluencingTaintsSet = new HashSet<>();

    for (InfluencingTaints influencingTaints : influencingTaintsSet) {
      Set<String> context = influencingTaints.getContext();

      if (context.isEmpty()) {
        continue;
      }

      newInfluencingTaintsSet.add(influencingTaints);
    }

    InfluencingTaints mergedConditionTaint = new InfluencingTaints(new HashSet<>(),
        mergedConditions);
    newInfluencingTaintsSet.add(mergedConditionTaint);

    return newInfluencingTaintsSet;
  }

  private Set<String> mergeConditionsOfTaintsWithEmptyContexts(
      Set<Set<String>> conditionsOfTaintsWithEmptyContexts) {
    Set<Set<String>> conditions = new HashSet<>();

    int numberOfConditionsToAnalyze = conditionsOfTaintsWithEmptyContexts.size();
    Set<Set<String>> analyzedConditions = new HashSet<>();

    while (analyzedConditions.size() != numberOfConditionsToAnalyze) {
      Set<String> mergedConditions = new HashSet<>();

      for (Set<String> condition : conditionsOfTaintsWithEmptyContexts) {
        if (analyzedConditions.contains(condition)) {
          continue;
        }

        if (mergedConditions.containsAll(condition) || condition.containsAll(mergedConditions)) {
          mergedConditions.addAll(condition);
          analyzedConditions.add(condition);
        }

      }

      conditions.add(mergedConditions);
    }

    if (conditions.size() > 1) {
      throw new RuntimeException("Did not expect that we could not merge all condition taints");
    }

    return conditions.iterator().next();
  }

  private Set<Set<String>> getConditionsOfTaintsWithEmptyContexts(
      Set<InfluencingTaints> influencingTaintsSet) {
    Set<Set<String>> conditions = new HashSet<>();

    for (InfluencingTaints influencingTaints : influencingTaintsSet) {
      Set<String> context = influencingTaints.getContext();

      if (!context.isEmpty()) {
        continue;
      }

      conditions.add(influencingTaints.getCondition());
    }

    return conditions;
  }

  private Set<Set<String>> mergeTaints(Set<InfluencingTaints> influencingTaintsSet) {
    Set<Set<String>> taintsSet = new HashSet<>();

    for (InfluencingTaints influencingTaints : influencingTaintsSet) {
      Set<String> taints = new HashSet<>(influencingTaints.getContext());
      taints.addAll(influencingTaints.getCondition());

      taintsSet.add(taints);
    }

    return taintsSet;
  }

  @Override
  public Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<RegionToInfo<Set<Set<String>>>> results = mapper
        .readValue(file, new TypeReference<List<RegionToInfo<Set<Set<String>>>>>() {
        });
    Map<JavaRegion, Set<Set<String>>> regionsToOptionsSet = new HashMap<>();

    for (RegionToInfo<Set<Set<String>>> result : results) {
      regionsToOptionsSet.put(result.getRegion(), result.getInfo());
    }

    return regionsToOptionsSet;
  }

  @Override
  public String outputDir() {
    return BaseDynamicRegionAnalysis.DIRECTORY + "/" + this.getProgramName() + "/analysis";
  }

  private Set<InfluencingTaints> removeContextTaintsInConditionTaints(
      Set<InfluencingTaints> influencingTaintsSet) {
    Set<InfluencingTaints> newInfluencingTaintsSet = new HashSet<>();

    for (InfluencingTaints influencingTaints : influencingTaintsSet) {
      Set<String> context = influencingTaints.getContext();
      Set<String> condition = influencingTaints.getCondition();

      Set<String> newCondition = new HashSet<>(condition);
      newCondition.removeAll(context);

      if (newCondition.isEmpty()) {
        continue;
      }

      InfluencingTaints newInfluencingTaints = new InfluencingTaints(context, newCondition);
      newInfluencingTaintsSet.add(newInfluencingTaints);
    }

    return newInfluencingTaintsSet;
  }

  private Set<PhosphorControlFlowInfo> readPhosphorTaintResults() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File inputFile = new File(
        this.phosphorTaintAnalysis.outputDir() + "/" + this.getProgramName() + ".json");

    return mapper
        .readValue(inputFile, new TypeReference<Set<PhosphorControlFlowInfo>>() {
        });
  }

}
