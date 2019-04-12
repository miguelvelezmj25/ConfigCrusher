package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.region;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.BaseDynamicAnalysis;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.TaintHelper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO might need to know what configurations will be executed to know what can be removed from instrumentation
public class PhosphorTaintAnalysis extends BaseDynamicAnalysis<Void> {

  private final Map<String, Set<InfluencingTaints>> statementsToOptions = new HashMap<>();
  //  private final Map<String, InfluencingTaints> statementsToOptions = new HashMap<>();
  private final List<String> options;

  public PhosphorTaintAnalysis(String programName, List<String> options) {
    super(programName, new HashSet<>(options), new HashSet<>());

    this.options = options;
  }

  @Override
  public Void analyze() throws IOException, InterruptedException {
    System.out.println("Total tainted statements " + this.statementsToOptions.size());
    this.removeContextTaintsInConditionTaints();
    this.removeStatementsWithOnlyEmptyConditionTaints();
    this.updatedConditionTaintsIfremoveContextTaintsInConditionTaints();

    this.checkContextTaintsNotEmpty();
    this.checkIfProblematicEntry();

    int[] counts = new int[9];
    int sets = 0;
    int interestingSets = 0;

    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();
//      Iterator<InfluencingTaints> setIter = setOfInfluencingTaints.iterator();

//      while (setIter.hasNext()) {
//        InfluencingTaints influencingTaints = setIter.next();
//        Set<String> contextTaints = influencingTaints.getContext();
//        Set<String> conditionTaints = influencingTaints.getCondition();
//
//        if (conditionTaints.isEmpty()) {
//          setIter.remove();
//        }
//        else if (contextTaints.equals(conditionTaints)) {
//          setIter.remove();
//        }
//        else if(contextTaints.containsAll(conditionTaints)) {
//          setIter.remove();
//        }
//      }

      int size = setOfInfluencingTaints.size();
      int count = counts[size];
      count++;
      counts[size] = count;

      sets += size;

      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
        Set<String> contextTaints = influencingTaints.getContext();
        Set<String> conditionTaints = influencingTaints.getCondition();

        if (!contextTaints.equals(conditionTaints)) {
          if (!contextTaints.containsAll(conditionTaints)) {
            interestingSets++;
          }
        }
      }
    }

    System.out.println("Total tainted statements " + this.statementsToOptions.size());
//    System.out.println("Total sets " + sets);
//    System.out.println("Total interesting sets " + interestingSets);

    for (int i = 0; i < counts.length; i++) {
      if (i == 0 && counts[i] != 0) {
        System.err.println("HOW CAN THERE BE AN UNTAINTED STATEMENT?");
      }

      if (i != 0) {
        System.out.println("Statements with " + i + " sets " + counts[i]);
      }
    }

    int statementsWithAllEmptyConditions = 0;

    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
      Set<InfluencingTaints> setsOfInfluencingTaints = entry.getValue();

      if (setsOfInfluencingTaints.size() <= 1) {
        continue;
      }

      Set<String> x = new HashSet<>();

      for (InfluencingTaints influencingTaints : setsOfInfluencingTaints) {
        Set<String> conditionTaints = influencingTaints.getCondition();

        if (x.containsAll(conditionTaints) || conditionTaints.containsAll(x)) {
          x.addAll(conditionTaints);
        }
        else {
          System.out.println();
        }
      }

      if (x.isEmpty()) {
        statementsWithAllEmptyConditions++;
      }
    }

    System.out.println("Statements with all empty conditions " + statementsWithAllEmptyConditions);

    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
      Set<InfluencingTaints> setsOfInfluencingTaints = entry.getValue();

      if (setsOfInfluencingTaints.size() <= 1) {
        continue;
      }

      Set<String> x = new HashSet<>();

      for (InfluencingTaints influencingTaints : setsOfInfluencingTaints) {
        Set<String> contextTaints = influencingTaints.getContext();

        if (x.containsAll(contextTaints) || contextTaints.containsAll(x)) {
          x.addAll(contextTaints);
        }
        else {
          System.out.println();
        }
      }
    }

    throw new UnsupportedOperationException("Implement");
  }

  private void updatedConditionTaintsIfremoveContextTaintsInConditionTaints() {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();

      if (setOfInfluencingTaints.size() <= 1) {
        continue;
      }

      Set<String> superSetOfConditionTaints = new HashSet<>();

      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
        Set<String> conditionTaints = influencingTaints.getCondition();

        if (conditionTaints.isEmpty()) {
          superSetOfConditionTaints = new HashSet<>();
          break;
        }

        if (conditionTaints.containsAll(superSetOfConditionTaints) || superSetOfConditionTaints
            .containsAll(conditionTaints)) {
          superSetOfConditionTaints.addAll(conditionTaints);
        }
      }

      // TODO update the context with the super set
      if (!superSetOfConditionTaints.isEmpty()) {
        System.out.println();
      }
    }
  }

  private void removeContextTaintsInConditionTaints() {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
      Set<InfluencingTaints> newSetOfInfluencingTaints = new HashSet<>();
      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();

      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
        Set<String> contextTaints = influencingTaints.getContext();
        Set<String> conditionTaints = influencingTaints.getCondition();
        Set<String> newConditionTaints = new HashSet<>(conditionTaints);
        newConditionTaints.removeAll(contextTaints);

        InfluencingTaints newInfluencingTaints = new InfluencingTaints(contextTaints,
            newConditionTaints);
        newSetOfInfluencingTaints.add(newInfluencingTaints);
      }

      this.statementsToOptions.put(entry.getKey(), newSetOfInfluencingTaints);
    }
  }

  private void checkContextTaintsNotEmpty() {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();

      if (setOfInfluencingTaints.size() <= 1) {
        continue;
      }

      Set<String> superSetOfContextTaints = new HashSet<>();

      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
        Set<String> contextTaints = influencingTaints.getContext();

        if (contextTaints.isEmpty()) {
          superSetOfContextTaints = new HashSet<>();
          break;
        }

        if (contextTaints.containsAll(superSetOfContextTaints) || superSetOfContextTaints
            .containsAll(contextTaints)) {
          superSetOfContextTaints.addAll(contextTaints);
        }
      }

      // TODO update the context with the super set
      if (!superSetOfContextTaints.isEmpty()) {
        System.out.println();
      }
    }
  }

  // If the context is the same of all influencing taints, but the conditions are different and
  // we do not sample all combos of the conditions, we do not know what execution belongs to which options.
  private void checkIfProblematicEntry() {
    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
      Set<Set<String>> allContextTaints = new HashSet<>();
      Set<InfluencingTaints> setOfInfluencingTaints = entry.getValue();

      for (InfluencingTaints influencingTaints : setOfInfluencingTaints) {
        Set<String> contextTaints = influencingTaints.getContext();
        allContextTaints.add(contextTaints);
      }

      if (setOfInfluencingTaints.size() > 1 && allContextTaints.size() == 1) {
        // MIGHT BE A PROBLEM
        System.out.println();
      }
    }
  }

  private void removeStatementsWithOnlyEmptyConditionTaints() {
    Set<String> statementsToRemove = new HashSet<>();

    for (Map.Entry<String, Set<InfluencingTaints>> entry : this.statementsToOptions.entrySet()) {
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

    this.statementsToOptions.keySet().removeAll(statementsToRemove);
    System.out.println(
        "Total tainted statements after removing statements with only empty condition taints "
            + this.statementsToOptions.size());
  }

  public void getTaints(Set<DecisionTaints> results) {
    this.putStatements(results);
    this.putOptions(results);
  }

  private void putOptions(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      Set<String> conditionTaints = TaintHelper.getConditionTaints(decisionTaints, this.options);
      Set<String> contextTaints = TaintHelper.getContextTaints(decisionTaints, this.options);
      InfluencingTaints influencingTaints = new InfluencingTaints(contextTaints, conditionTaints);

//      // TODO what are the benefits/drawbacks of doing this tracking?
//      if (conditionTaints.equals(contextTaints)) {
//        continue;
//      }
//
//      if(!contextTaints.isEmpty() && conditionTaints.containsAll(contextTaints)) {
//        conditionTaints.removeAll(contextTaints);
//      }
//
//      if (conditionTaints.equals(contextTaints)) {
//        continue;
//      }
//
//      if(contextTaints.containsAll(conditionTaints)) {
//        continue;
//      }

      String statement = decisionTaints.getDecision();
      Set<InfluencingTaints> currentTaints = this.statementsToOptions.get(statement);
      currentTaints.add(influencingTaints);
//      if (!influencingTaints.equals(currentTaints)) {
//        this.statementsToOptions.put(statement, influencingTaints);
//      }
    }

//    int empty = 0;
//    int one = 0;
//    int two = 0;
//    int three = 0;
//    int four = 0;
//
//    for (Map.Entry<String, InfluencingTaints> entry : this.statementsToOptions.entrySet()) {
//      InfluencingTaints influencingTaints = entry.getValue();
//
//      if(influencingTaints.size() != 1) {
//        continue;
//      }
//
//      InfluencingTaints x = influencingTaints.iterator().next();
//
//      if(x.getContext().isEmpty()) {
//        System.out.println();
//      }
//
////      switch (entry.size()) {
////        case 0:
////          empty++;
////          break;
////        case 1:
////          one++;
////          break;
////        case 2:
////          two++;
////          break;
////        case 3:
////          three++;
////          break;
////        case 4:
////          four++;
////          break;
////        default:
////          throw new RuntimeException("MORE THAN 4 taints?");
////      }
//    }
//
//    System.out.println("Statements with empty sets: " + empty);
//    System.out.println("Statements with one sets: " + one);
//    System.out.println("Statements with two sets: " + two);
//    System.out.println("Statements with three sets: " + three);
//    System.out.println("Statements with four sets: " + four);
  }

  private void putStatements(Set<DecisionTaints> results) {
    for (DecisionTaints decisionTaints : results) {
      String statement = decisionTaints.getDecision();
      this.statementsToOptions
//          .putIfAbsent(statement, new InfluencingTaints(new HashSet<>(), new HashSet<>()));
          .putIfAbsent(statement, new HashSet<>());
    }
  }

  @Override
  public void writeToFile(Void some) throws IOException {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  public Void readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  public String outputDir() {
    return DIRECTORY + "/" + this.getProgramName() + "/cc/regions";
  }
}
