package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.cc.DecisionInfo;
import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.cc.TaintLabel;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public class PhosphorExecutionAnalysis {

  private static final String PHOSPHOR_OUTPUT_DIR =
      BaseAdapter.USER_HOME
          + "/Documents/Programming/Java/Projects/phosphor/Phosphor/examples/implicit-optimized";

  private final String programName;

  PhosphorExecutionAnalysis(String programName) {
    this.programName = programName;
  }

  Map<String, Map<Set<String>, List<Set<String>>>> getPhosphorResults()
      throws IOException {
    String dir = PHOSPHOR_OUTPUT_DIR + "/" + programName;
    Collection<File> serializedFiles = this.getSerializedFiles(dir);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dir + " must have 1 file.");
    }

    List<DecisionInfo> phosphorTaintResults = this
        .readPhosphorTaintResults(serializedFiles.iterator().next());
    Map<String, Map<Taint, List<Taint>>> analysisData = this
        .getDataFromAnalysis(phosphorTaintResults);
    Map<String, Map<Set<TaintLabel>, List<Set<TaintLabel>>>> analysisDataWithLabels = this
        .getSinksToLabelData(analysisData);

    return this.changeLabelsToTaints(analysisDataWithLabels);
  }

  private Collection<File> getSerializedFiles(String dir) {
    File dirFile = new File(dir);

    return FileUtils.listFiles(dirFile, null, false);
  }

  private List<DecisionInfo> readPhosphorTaintResults(File serializedFile) throws IOException {
    return this.deserialize(serializedFile);
  }

  // TODO check catching and throwing
  private List<DecisionInfo> deserialize(File file) throws IOException {
    FileInputStream fis = new FileInputStream(file);
    ObjectInputStream ois = new ObjectInputStream(fis);
    List<DecisionInfo> decisionInfos;

    try {
      decisionInfos = (List<DecisionInfo>) ois.readObject();

    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    ois.close();
    fis.close();

    return decisionInfos;
  }

  private Map<String, Map<Taint, List<Taint>>> getDataFromAnalysis(
      List<DecisionInfo> decisionInfos) {
    Map<String, Map<Taint, List<Taint>>> sinksToDecisionsInfluence = new HashMap<>();

    Set<String> sinks = this.getReachedSinks(decisionInfos);
    this.addSinksFromAnalysis(sinks, sinksToDecisionsInfluence);
    this.addExecCtxTaintsFromAnalysis(decisionInfos, sinksToDecisionsInfluence);
    this.addConditionTaintsFromAnalysis(decisionInfos, sinksToDecisionsInfluence);

    return sinksToDecisionsInfluence;
  }

  private Set<String> getReachedSinks(List<DecisionInfo> decisionInfos) {
    Set<String> sinks = new HashSet<>();

    for (DecisionInfo decisionInfo : decisionInfos) {
      sinks.add(decisionInfo.getDecision());
    }

    return sinks;
  }

  private void addSinksFromAnalysis(Set<String> sinks,
      Map<String, Map<Taint, List<Taint>>> sinksToDecisionsInfluence) {
    for (String sink : sinks) {
      sinksToDecisionsInfluence.put(sink, new HashMap<>());
    }
  }

  private Map<String, Map<Set<TaintLabel>, List<Set<TaintLabel>>>> getSinksToLabelData(
      Map<String, Map<Taint, List<Taint>>> analysisData) {
    Map<String, Map<Set<TaintLabel>, List<Set<TaintLabel>>>> sinksToLabelData = new HashMap<>();

    for (Map.Entry<String, Map<Taint, List<Taint>>> entry : analysisData.entrySet()) {
      Map<Taint, List<Taint>> sinkData = entry.getValue();
      Map<Set<TaintLabel>, List<Set<TaintLabel>>> variabilityCtxsToLabels = this
          .getVariabilityCtxsToLabels(sinkData);
      sinksToLabelData.put(entry.getKey(), variabilityCtxsToLabels);
    }

    return sinksToLabelData;
  }

  private Map<String, Map<Set<String>, List<Set<String>>>> changeLabelsToTaints(
      Map<String, Map<Set<TaintLabel>, List<Set<TaintLabel>>>> sinksToLabelData) {
    Map<String, Map<Set<String>, List<Set<String>>>> sinksToTaints = new HashMap<>();

    for (Map.Entry<String, Map<Set<TaintLabel>, List<Set<TaintLabel>>>> entry : sinksToLabelData
        .entrySet()) {
      Map<Set<String>, List<Set<String>>> taintData = this
          .transformDataLabelsToTaints(entry.getValue());
      sinksToTaints.put(entry.getKey(), taintData);
    }

    return sinksToTaints;
  }

  private void addExecCtxTaintsFromAnalysis(List<DecisionInfo> decisionInfos,
      Map<String, Map<Taint, List<Taint>>> sinksToDecisionInfluences) {
    for (DecisionInfo decisionInfo : decisionInfos) {
      String sink = decisionInfo.getDecision();
      Map<Taint, List<Taint>> decisionInfluences = sinksToDecisionInfluences.get(sink);

      DecisionTaints decisionTaints = decisionInfo.getDecisionTaints();
      Taint execCtxTaints = decisionTaints.getExecCtxTaints();
      decisionInfluences.put(execCtxTaints, new ArrayList<>());
    }
  }

  private void addConditionTaintsFromAnalysis(List<DecisionInfo> decisionInfos,
      Map<String, Map<Taint, List<Taint>>> sinksToDecisionInfluences) {
    for (DecisionInfo decisionInfo : decisionInfos) {
      String sink = decisionInfo.getDecision();
      Map<Taint, List<Taint>> decisionInfluences = sinksToDecisionInfluences.get(sink);

      DecisionTaints decisionTaints = decisionInfo.getDecisionTaints();
      Taint execCtxTaints = decisionTaints.getExecCtxTaints();

      List<Taint> conditionInfluences = decisionInfluences.get(execCtxTaints);
      Taint conditionTaints = decisionTaints.getConditionTaints();
      conditionInfluences.add(conditionTaints);
    }
  }

  private Map<Set<TaintLabel>, List<Set<TaintLabel>>> getVariabilityCtxsToLabels(
      Map<Taint, List<Taint>> sinkData) {
    Map<Set<TaintLabel>, List<Set<TaintLabel>>> variabilityCtxsToLabels = new HashMap<>();

    for (Map.Entry<Taint, List<Taint>> entry : sinkData.entrySet()) {
      Taint variabilityCtxTaint = entry.getKey();
      Set<TaintLabel> variabilityCtx = this.getVariabilityCtx(variabilityCtxTaint);

      List<Taint> executionTaints = entry.getValue();
      List<Set<TaintLabel>> executionLabelSet = this.getExecutionLabelSet(executionTaints);

      variabilityCtxsToLabels.put(variabilityCtx, executionLabelSet);
    }

    return variabilityCtxsToLabels;
  }

  private Map<Set<String>, List<Set<String>>> transformDataLabelsToTaints(
      Map<Set<TaintLabel>, List<Set<TaintLabel>>> ctxsToTaintLabels) {
    Map<Set<String>, List<Set<String>>> ctxsToTaints = new HashMap<>();

    for (Map.Entry<Set<TaintLabel>, List<Set<TaintLabel>>> entry : ctxsToTaintLabels
        .entrySet()) {
      Set<String> ctx = this.transformLabelsToTaints(entry.getKey());
      List<Set<String>> taintSets = new ArrayList<>();

      for (Set<TaintLabel> LabelSet : entry.getValue()) {
        Set<String> taintSet = this.transformLabelsToTaints(LabelSet);
        taintSets.add(taintSet);
      }

      ctxsToTaints.put(ctx, taintSets);
    }

    return ctxsToTaints;
  }

  private Set<TaintLabel> getVariabilityCtx(Taint variabilityCtx) {
    if (variabilityCtx == null) {
      return new HashSet<>();
    }

    return variabilityCtx.getLabels();
  }

  private List<Set<TaintLabel>> getExecutionLabelSet(List<Taint> executionTaints) {
    List<Set<TaintLabel>> executionLabelSet = new ArrayList<>();

    for (Taint taint : executionTaints) {
      Set<TaintLabel> labels = taint.getLabels();
      Set<TaintLabel> executionLabels = new HashSet<>(labels);
      executionLabelSet.add(executionLabels);
    }

    return executionLabelSet;
  }

  private Set<String> transformLabelsToTaints(Set<TaintLabel> labels) {
    Set<String> strings = new HashSet<>();

    for (TaintLabel taintLabel : labels) {
      strings.add(taintLabel.getSource());
    }

    return strings;
  }

}
