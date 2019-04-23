package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.TaintHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO might need to know what configurations will be executed to know what can be removed from instrumentation
public class PhosphorTaintAnalysis extends BaseDynamicAnalysis<Set<PhosphorControlFlowInfo>> {

  private final Map<String, Set<InfluencingTaints>> statementsToOptionsSet = new HashMap<>();
  private final Map<String, InfluencingTaints> statementsToOptions = new HashMap<>();
  private final List<String> options;

  public PhosphorTaintAnalysis(String programName, List<String> options) {
    super(programName, new HashSet<>(options), new HashSet<>());

    this.options = options;
  }

  PhosphorTaintAnalysis(String programName) {
    this(programName, new ArrayList<>());
  }

  @Override
  public Set<PhosphorControlFlowInfo> analyze() throws IOException, InterruptedException {
//    this.removeContextTaintsInConditionTaints();
//    this.removeStatementsWithOnlyEmptyConditionTaints();
////    this.updatedConditionTaintsIfremoveContextTaintsInConditionTaints();
//
////    this.checkContextTaintsNotEmpty();
////    this.checkIfProblematicEntry();
//
//    // TODO we might not be able to always merge the sets of options
//    this.mergeTaints();
//    this.removeContextOptionsInConditionOptions();
//    this.removeStatementsWithOnlyEmptyConditionOptions();

    return this.getPhosphorControlFlowDecisions();
  }

  private Set<PhosphorControlFlowInfo> getPhosphorControlFlowDecisions() {
    Set<PhosphorControlFlowInfo> phosphorControlFlowInfos = new HashSet<>();

//    for (Map.Entry<String, InfluencingTaints> entry : this.statementsToOptions.entrySet()) {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptionsSet.entrySet()) {
      String statement = entry.getKey();
      String[] statementComponents = statement.split("\\.");

      String packageName = getPackageName(statementComponents[0]);
      String className = getClassName(statementComponents[0]);
      String methodSignature = statementComponents[1];
      int decisionIndex = Integer.parseInt(statementComponents[2]);

      PhosphorControlFlowInfo phosphorControlFlowInfo = new PhosphorControlFlowInfo(packageName,
          className, methodSignature, decisionIndex, entry.getValue());
      phosphorControlFlowInfos.add(phosphorControlFlowInfo);
    }

    return phosphorControlFlowInfos;
  }

  private static String getClassName(String statementComponent) {
    int indexOfLastSlash = statementComponent.lastIndexOf("/");
    return statementComponent.substring(indexOfLastSlash + 1);
  }

  private static String getPackageName(String statementComponent) {
    int indexOfLastSlash = statementComponent.lastIndexOf("/");
    statementComponent = statementComponent.substring(0, indexOfLastSlash);

    return statementComponent.replaceAll("/", ".");
  }

  private void mergeTaints() {
    this.mergeInfluencingTaints();
    this.addToMatchingTypeDataStructure();
  }

  private void addToMatchingTypeDataStructure() {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptionsSet.entrySet()) {
      this.statementsToOptions.put(entry.getKey(), entry.getValue().iterator().next());
    }
  }

  private void mergeInfluencingTaints() {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptionsSet.entrySet()) {
      Set<String> mergedContextTaints = new HashSet<>();
      Set<String> mergedConditionTaints = new HashSet<>();

      for (InfluencingTaints influencingTaints : entry.getValue()) {
        Set<String> contextTaints = influencingTaints.getContext();
        mergedContextTaints.addAll(contextTaints);

        Set<String> conditionTaints = influencingTaints.getCondition();
        mergedConditionTaints.addAll(conditionTaints);
      }

      InfluencingTaints newInfluencingTaints = new InfluencingTaints(mergedContextTaints,
          mergedConditionTaints);
      Set<InfluencingTaints> setWithOnlyOneInfluencingTaint = new HashSet<>();
      setWithOnlyOneInfluencingTaint.add(newInfluencingTaints);

      this.statementsToOptionsSet.put(entry.getKey(), setWithOnlyOneInfluencingTaint);
    }
  }

//  private void updatedConditionTaintsIfremoveContextTaintsInConditionTaints() {
//    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
//      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
//
//      if (setOfInfluencingTaints.size() <= 1) {
//        continue;
//      }
//
//      Set<String> superSetOfConditionTaints = new HashSet<>();
//
//      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
//        Set<String> conditionTaints = influencingTaints.getCondition();
//
//        if (conditionTaints.isEmpty()) {
//          superSetOfConditionTaints = new HashSet<>();
//          break;
//        }
//
//        if (conditionTaints.containsAll(superSetOfConditionTaints) || superSetOfConditionTaints
//            .containsAll(conditionTaints)) {
//          superSetOfConditionTaints.addAll(conditionTaints);
//        }
//      }
//
//      // TODO update the context with the super set
//      if (!superSetOfConditionTaints.isEmpty()) {
//        System.out.println();
//      }
//    }
//  }

  private void removeContextTaintsInConditionTaints() {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptionsSet.entrySet()) {
      Set<InfluencingTaints> newSetOfInfluencingTaints = new HashSet<>();
      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();

      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
        InfluencingTaints newInfluencingTaints = this.getNewInfluencingTaint(influencingTaints);
        newSetOfInfluencingTaints.add(newInfluencingTaints);
      }

      this.statementsToOptionsSet.put(entry.getKey(), newSetOfInfluencingTaints);
    }
  }

  private void removeContextOptionsInConditionOptions() {
    for (Map.Entry<String, InfluencingTaints> entry : this.statementsToOptions.entrySet()) {
      InfluencingTaints influencingTaints = entry.getValue();
      InfluencingTaints newInfluencingTaints = this.getNewInfluencingTaint(influencingTaints);
      this.statementsToOptions.put(entry.getKey(), newInfluencingTaints);
    }
  }

  private InfluencingTaints getNewInfluencingTaint(InfluencingTaints influencingTaints) {
    Set<String> contextTaints = influencingTaints.getContext();
    Set<String> conditionTaints = influencingTaints.getCondition();

    Set<String> newConditionTaints = new HashSet<>(conditionTaints);
    newConditionTaints.removeAll(contextTaints);

    return new InfluencingTaints(contextTaints, newConditionTaints);
  }

//  private void checkContextTaintsNotEmpty() {
//    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
//      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
//
//      if (setOfInfluencingTaints.size() <= 1) {
//        continue;
//      }
//
//      Set<String> superSetOfContextTaints = new HashSet<>();
//
//      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
//        Set<String> contextTaints = influencingTaints.getContext();
//
//        if (contextTaints.isEmpty()) {
//          superSetOfContextTaints = new HashSet<>();
//          break;
//        }
//
//        if (contextTaints.containsAll(superSetOfContextTaints) || superSetOfContextTaints
//            .containsAll(contextTaints)) {
//          superSetOfContextTaints.addAll(contextTaints);
//        }
//      }
//
//      // TODO update the context with the super set
//      if (!superSetOfContextTaints.isEmpty()) {
//        System.out.println();
//      }
//    }
//  }

//  // If the context is the same of all influencing taints, but the conditions are different and
//  // we do not sample all combos of the conditions, we do not know what execution belongs to which options.
//  private void checkIfProblematicEntry() {
//    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
//      Set<Set<String>> allContextTaints = new HashSet<>();
//      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
//
//      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
//        Set<String> contextTaints = influencingTaints.getContext();
//        allContextTaints.add(contextTaints);
//      }
//
//      if (setOfInfluencingTaints.size() > 1 && allContextTaints.size() == 1) {
//        // MIGHT BE A PROBLEM
//        System.out.println();
//      }
//    }
//  }

  private void removeStatementsWithOnlyEmptyConditionTaints() {
    Set<String> statementsToRemove = new HashSet<>();

    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptionsSet.entrySet()) {
      Set<String> allConditionTaints = new HashSet<>();
      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();

      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
        Set<String> conditionTaints = influencingTaints.getCondition();
        allConditionTaints.addAll(conditionTaints);
      }

      if (allConditionTaints.isEmpty()) {
        statementsToRemove.add(entry.getKey());
      }
    }

    this.statementsToOptionsSet.keySet().removeAll(statementsToRemove);
  }

  private void removeStatementsWithOnlyEmptyConditionOptions() {
    Set<String> statementsToRemove = new HashSet<>();

    for (Map.Entry<String, InfluencingTaints> entry : this.statementsToOptions.entrySet()) {
      InfluencingTaints influencingTaints = entry.getValue();
      Set<String> conditionTaints = influencingTaints.getCondition();

      if (conditionTaints.isEmpty()) {
        statementsToRemove.add(entry.getKey());
      }
    }

    this.statementsToOptions.keySet().removeAll(statementsToRemove);
  }

  public void recordTaints(Set<DecisionTaints> results) {
    this.putStatements(results);
    this.putOptions(results);
  }

  private void putOptions(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      Set<String> conditionTaints = TaintHelper.getConditionTaints(decisionTaints, this.options);
      Set<String> contextTaints = TaintHelper.getContextTaints(decisionTaints, this.options);
      InfluencingTaints influencingTaints = new InfluencingTaints(contextTaints, conditionTaints);

      String statement = decisionTaints.getDecision();
      Set<InfluencingTaints> currentTaints = this.statementsToOptionsSet.get(statement);
      currentTaints.add(influencingTaints);
    }
  }

  private void putStatements(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      String statement = decisionTaints.getDecision();
      this.statementsToOptionsSet.putIfAbsent(statement, new HashSet<>());
    }
  }

  @Override
  public void writeToFile(Set<PhosphorControlFlowInfo> controlFlowInfos) throws IOException {
    String outputFile = this.outputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, controlFlowInfos);
  }

  @Override
  public Set<PhosphorControlFlowInfo> readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper
        .readValue(file, new TypeReference<Set<PhosphorControlFlowInfo>>() {
        });
  }

  @Override
  public String outputDir() {
    return DIRECTORY + "/" + this.getProgramName() + "/cc/statements";
  }
}
