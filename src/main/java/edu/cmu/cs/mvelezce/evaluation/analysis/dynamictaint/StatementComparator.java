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

    Map<CFStatement, InfluencingTaints> statementsToInfluencingTaints1 = matchStatementsToInfluencingTaints(
        overlappingStatements, controlFlowInfos1);
    Map<CFStatement, InfluencingTaints> statementsToInfluencingTaints2 = matchStatementsToInfluencingTaints(
        overlappingStatements, controlFlowInfos2);

    for (Map.Entry<CFStatement, InfluencingTaints> entry : statementsToInfluencingTaints1
        .entrySet()) {
      CFStatement statement = entry.getKey();

      InfluencingTaints influencingTaints1 = entry.getValue();
      InfluencingTaints influencingTaints2 = statementsToInfluencingTaints2.get(statement);

      if (!influencingTaints1.equals(influencingTaints2)) {
        System.out.println(influencingTaints1 + " --- " + influencingTaints2);
      }
    }

    System.out.println(statementsToInfluencingTaints1.equals(statementsToInfluencingTaints2));
  }

  private static Map<CFStatement, InfluencingTaints> matchStatementsToInfluencingTaints(
      Set<CFStatement> statements,
      Set<PhosphorControlFlowInfo> controlFlowInfos) {
    Map<CFStatement, InfluencingTaints> statementsToInfluencingTaints = new HashMap<>();

    for (PhosphorControlFlowInfo controlFlowInfo : controlFlowInfos) {
      CFStatement cfgStatement = buildCFStatement(controlFlowInfo);

      if (!statements.contains(cfgStatement)) {
        continue;
      }

      Set<String> context = new HashSet<>();
      Set<String> condition = new HashSet<>();

      for (InfluencingTaints influencingTaints : controlFlowInfo.getInfluencingTaints()) {
        context.addAll(influencingTaints.getContext());
        condition.addAll(influencingTaints.getCondition());
      }

      statementsToInfluencingTaints.put(cfgStatement, new InfluencingTaints(context, condition));
    }

    return statementsToInfluencingTaints;
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