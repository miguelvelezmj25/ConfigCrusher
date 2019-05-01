package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.region.CFStatement;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatementComparator {

  private static final String DATA_DIR = "/statements/data/";

  private StatementComparator() {
  }

  static CFStatement buildCFStatement(PhosphorControlFlowInfo phosphorControlFlowInfo) {
    String packageName = phosphorControlFlowInfo.getPackageName();
    String className = phosphorControlFlowInfo.getClassName();
    String methodSignature = phosphorControlFlowInfo.getMethodSignature();
    int index = phosphorControlFlowInfo.getDecisionIndex();

    return new CFStatement(packageName, className, methodSignature, index);
  }

  static Set<CFStatement> getCFStatements(Set<PhosphorControlFlowInfo> controlFlowInfos) {
    Set<CFStatement> statements = new HashSet<>();

    for (PhosphorControlFlowInfo phosphorControlFlowInfo : controlFlowInfos) {
      CFStatement cfStatement = buildCFStatement(phosphorControlFlowInfo);
      statements.add(cfStatement);
    }

    return statements;
  }

  static void compareOverlapping(Set<PhosphorControlFlowInfo> controlFlowInfos1,
      Set<PhosphorControlFlowInfo> controlFlowInfos2) {
    Set<CFStatement> overlappingStatements = getOverlappingStatements(controlFlowInfos1,
        controlFlowInfos2);

    Map<CFStatement, Set<InfluencingTaints>> statementsToInfluencingTaints1 = matchStatementsToInfluencingTaints(
        overlappingStatements, controlFlowInfos1);
    Map<CFStatement, Set<InfluencingTaints>> statementsToInfluencingTaints2 = matchStatementsToInfluencingTaints(
        overlappingStatements, controlFlowInfos2);

    for (Map.Entry<CFStatement, Set<InfluencingTaints>> entry : statementsToInfluencingTaints1
        .entrySet()) {
      CFStatement statement = entry.getKey();

      Set<InfluencingTaints> influencingTaints1 = entry.getValue();
      Set<InfluencingTaints> influencingTaints2 = statementsToInfluencingTaints2.get(statement);

      printInfo(statement, influencingTaints1, influencingTaints2);
    }
//
//    System.out.println(statementsToInfluencingTaints1.equals(statementsToInfluencingTaints2));
  }

  private static void printInfo(CFStatement statement, Set<InfluencingTaints> influencingTaints1,
      Set<InfluencingTaints> influencingTaints2) {
    if (!influencingTaints1.equals(influencingTaints2)) {
      System.out.println(statement);

      printMissingTaintsInSmallerWorkload(influencingTaints1, influencingTaints2);
      printMissingTaintsInLargerWorkload(influencingTaints1, influencingTaints2);

      System.out.println();
    }
  }

  private static void printMissingTaintsInLargerWorkload(Set<InfluencingTaints> influencingTaints1,
      Set<InfluencingTaints> influencingTaints2) {
    Set<InfluencingTaints> extraTaints = new HashSet<>(influencingTaints1);
    extraTaints.removeAll(influencingTaints2);

    if (extraTaints.isEmpty()) {
      return;
    }

    System.out.println("Missing taints in larger workload");

    for (InfluencingTaints influencingTaints : extraTaints) {
      System.out.println(influencingTaints);
    }
  }

  private static void printMissingTaintsInSmallerWorkload(Set<InfluencingTaints> influencingTaints1,
      Set<InfluencingTaints> influencingTaints2) {
    Set<InfluencingTaints> extraTaints = new HashSet<>(influencingTaints2);
    extraTaints.removeAll(influencingTaints1);

    if (extraTaints.isEmpty()) {
      return;
    }

    System.out.println("Missing taints in smaller workload");

    for (InfluencingTaints influencingTaints : extraTaints) {
      System.out.println(influencingTaints);
    }
  }

  private static Map<CFStatement, Set<InfluencingTaints>> matchStatementsToInfluencingTaints(
      Set<CFStatement> overlappingStatements, Set<PhosphorControlFlowInfo> controlFlowInfos) {
    Map<CFStatement, Set<InfluencingTaints>> statementsToInfluencingTaints = new HashMap<>();
    addCFGStatements(statementsToInfluencingTaints, controlFlowInfos, overlappingStatements);
    addInfluencingTaints(statementsToInfluencingTaints, controlFlowInfos);

    return statementsToInfluencingTaints;
  }

  private static void addInfluencingTaints(
      Map<CFStatement, Set<InfluencingTaints>> statementsToInfluencingTaints,
      Set<PhosphorControlFlowInfo> controlFlowInfos) {
    for (PhosphorControlFlowInfo controlFlowInfo : controlFlowInfos) {
      CFStatement cfgStatement = buildCFStatement(controlFlowInfo);

      if (!statementsToInfluencingTaints.containsKey(cfgStatement)) {
        continue;
      }

      statementsToInfluencingTaints.get(cfgStatement)
          .addAll(controlFlowInfo.getInfluencingTaints());
    }
  }

  private static void addCFGStatements(
      Map<CFStatement, Set<InfluencingTaints>> statementsToInfluencingTaints,
      Set<PhosphorControlFlowInfo> controlFlowInfos,
      Set<CFStatement> overlappingStatements) {

    for (PhosphorControlFlowInfo controlFlowInfo : controlFlowInfos) {
      CFStatement cfgStatement = buildCFStatement(controlFlowInfo);

      if (!overlappingStatements.contains(cfgStatement)) {
        continue;
      }

      statementsToInfluencingTaints.put(cfgStatement, new HashSet<>());
    }
  }

  private static Set<CFStatement> getOverlappingStatements(
      Set<PhosphorControlFlowInfo> controlFlowInfos1,
      Set<PhosphorControlFlowInfo> controlFlowInfos2) {
    Set<CFStatement> statements1 = getCFStatements(controlFlowInfos1);
    Set<CFStatement> statements2 = getCFStatements(controlFlowInfos2);

    Set<CFStatement> overlappingStatements = new HashSet<>(statements1);
    overlappingStatements.retainAll(statements2);

    return overlappingStatements;
  }

  static Set<PhosphorControlFlowInfo> readFromFile(String programName, String fileName)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    File file = new File(Comparator.BASE_DIR + programName + DATA_DIR + fileName);

    return mapper
        .readValue(file, new TypeReference<Set<PhosphorControlFlowInfo>>() {
        });
  }

}