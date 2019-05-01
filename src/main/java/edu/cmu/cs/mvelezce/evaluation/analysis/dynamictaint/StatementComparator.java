package edu.cmu.cs.mvelezce.evaluation.analysis.dynamictaint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.region.CFStatement;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.PhosphorControlFlowInfo;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatementComparator {

  private static final String DATA_DIR = "/statements/data/";
  private static final String OVERLAPPING_DIR = "/overlapping/";
  private static final String MISSING_DIR = "/missing/";

  private final String programName;

  StatementComparator(String programName) {
    this.programName = programName;
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

  void compareMissing(Set<PhosphorControlFlowInfo> controlFlowInfos1,
      Set<PhosphorControlFlowInfo> controlFlowInfos2) throws FileNotFoundException {
    Set<CFStatement> missingStatements = getMissingStatements(controlFlowInfos1, controlFlowInfos2);
    Set<CFStatement> allStatements = getCFStatements(controlFlowInfos2);
    this.writeMissingStatements(missingStatements, allStatements);

    Map<CFStatement, Set<InfluencingTaints>> statementsToInfluencingTaints = matchStatementsToInfluencingTaints(
        missingStatements, controlFlowInfos2);

    this.writeMissingInfo(statementsToInfluencingTaints);
  }

  private void writeMissingInfo(
      Map<CFStatement, Set<InfluencingTaints>> statementsToInfluencingTaints)
      throws FileNotFoundException {
    File outputFile = new File(
        Comparator.BASE_DIR + this.programName + MISSING_DIR + "missing-influences.txt");
    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);

    for (Map.Entry<CFStatement, Set<InfluencingTaints>> entry : statementsToInfluencingTaints
        .entrySet()) {
      this.writeMissingInfo(writer, entry.getKey(), entry.getValue());
    }

    writer.close();
  }

  private void writeMissingStatements(Set<CFStatement> missingStatements,
      Set<CFStatement> allStatements) throws FileNotFoundException {
    Set<String> missingStatementsStrings = getSortedStatementStrings(missingStatements);
    Set<String> allStatementStrings = getSortedStatementStrings(allStatements);

    List<String> sortedAllStatementStrings = new ArrayList<>(allStatementStrings);
    Collections.sort(sortedAllStatementStrings);

    File outputFile = new File(
        Comparator.BASE_DIR + this.programName + MISSING_DIR + "missing.txt");
    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);

    for (String statement : sortedAllStatementStrings) {
      if (missingStatementsStrings.contains(statement)) {
        writer.print("** ");
      }
      else {
        writer.print("   ");
      }

      writer.println(statement);
    }

    writer.close();
  }

  private static Set<String> getSortedStatementStrings(Set<CFStatement> statements) {
    Set<String> statementsStrings = new HashSet<>();

    for (CFStatement statement : statements) {
      statementsStrings.add(statement.toString());
    }

    return statementsStrings;
  }

  private void writeMissingInfo(PrintWriter writer, CFStatement statement,
      Set<InfluencingTaints> influencingTaintsAtStatement) {
    writer.println(statement);

    for (InfluencingTaints influencingTaints : influencingTaintsAtStatement) {
      writer.println(influencingTaints);
    }

    writer.println();
  }

  private static Set<CFStatement> getMissingStatements(
      Set<PhosphorControlFlowInfo> controlFlowInfos1,
      Set<PhosphorControlFlowInfo> controlFlowInfos2) {
    Set<CFStatement> statements1 = getCFStatements(controlFlowInfos1);
    Set<CFStatement> statements2 = getCFStatements(controlFlowInfos2);

    Set<CFStatement> missingStatements = new HashSet<>(statements2);
    missingStatements.removeAll(statements1);

    return missingStatements;
  }


  void compareOverlapping(Set<PhosphorControlFlowInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowInfo> largeControlFlowInfos) throws FileNotFoundException {
    Set<CFStatement> overlappingStatements = getOverlappingStatements(smallControlFlowInfos,
        largeControlFlowInfos);

    Map<CFStatement, Set<InfluencingTaints>> smallStatementsToInfluencingTaints = matchStatementsToInfluencingTaints(
        overlappingStatements, smallControlFlowInfos);
    Map<CFStatement, Set<InfluencingTaints>> largeStatementsToInfluencingTaints = matchStatementsToInfluencingTaints(
        overlappingStatements, largeControlFlowInfos);

    this.writeOverlappingInfo(smallStatementsToInfluencingTaints,
        largeStatementsToInfluencingTaints);
  }

  private void writeOverlappingInfo(
      Map<CFStatement, Set<InfluencingTaints>> smallStatementsToInfluencingTaints,
      Map<CFStatement, Set<InfluencingTaints>> largeStatementsToInfluencingTaints)
      throws FileNotFoundException {
    File outputFile = new File(
        Comparator.BASE_DIR + this.programName + OVERLAPPING_DIR + "overlapping.txt");
    outputFile.getParentFile().mkdirs();
    PrintWriter writer = new PrintWriter(outputFile);

    for (Map.Entry<CFStatement, Set<InfluencingTaints>> entry : smallStatementsToInfluencingTaints
        .entrySet()) {
      CFStatement statement = entry.getKey();

      Set<InfluencingTaints> smallInfluencingTaints = entry.getValue();
      Set<InfluencingTaints> largeInfluencingTaints = largeStatementsToInfluencingTaints
          .get(statement);

      writeOverlappingInfo(writer, statement, smallInfluencingTaints, largeInfluencingTaints);
    }

    writer.close();
  }

  private static void writeOverlappingInfo(PrintWriter writer, CFStatement statement,
      Set<InfluencingTaints> smallInfluencingTaints,
      Set<InfluencingTaints> largeInfluencingTaints) {
    if (smallInfluencingTaints.equals(largeInfluencingTaints)) {
      return;
    }

    writer.println("Inconsistent statement");
    writer.println(statement);

    printMissingTaintsInSmallerWorkload(writer, smallInfluencingTaints, largeInfluencingTaints);
    printMissingTaintsInLargerWorkload(writer, smallInfluencingTaints, largeInfluencingTaints);

    writer.println();
  }

  private static void printMissingTaintsInLargerWorkload(PrintWriter writer,
      Set<InfluencingTaints> influencingTaints1, Set<InfluencingTaints> influencingTaints2) {
    Set<InfluencingTaints> extraTaints = new HashSet<>(influencingTaints1);
    extraTaints.removeAll(influencingTaints2);

    if (extraTaints.isEmpty()) {
      return;
    }

    writer.println("Missing taints in larger workload");

    for (InfluencingTaints influencingTaints : extraTaints) {
      writer.println(influencingTaints);
    }
  }

  private static void printMissingTaintsInSmallerWorkload(PrintWriter writer,
      Set<InfluencingTaints> influencingTaints1, Set<InfluencingTaints> influencingTaints2) {
    Set<InfluencingTaints> extraTaints = new HashSet<>(influencingTaints2);
    extraTaints.removeAll(influencingTaints1);

    if (extraTaints.isEmpty()) {
      return;
    }

    writer.println("Missing taints in smaller workload");

    for (InfluencingTaints influencingTaints : extraTaints) {
      writer.println(influencingTaints);
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
      Set<PhosphorControlFlowInfo> smallControlFlowInfos,
      Set<PhosphorControlFlowInfo> largeControlFlowInfos) {
    Set<CFStatement> smallStatements = getCFStatements(smallControlFlowInfos);
    Set<CFStatement> largeStatements = getCFStatements(largeControlFlowInfos);

    Set<CFStatement> overlappingStatements = new HashSet<>(smallStatements);
    overlappingStatements.retainAll(largeStatements);

    return overlappingStatements;
  }

  Set<PhosphorControlFlowInfo> readFromFile(String fileName)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    File file = new File(Comparator.BASE_DIR + this.programName + DATA_DIR + fileName);

    return mapper
        .readValue(file, new TypeReference<Set<PhosphorControlFlowInfo>>() {
        });
  }

}