package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.MutablePair;

public class Specification {

  Set<String> getMinimalSetOfOptions(DecisionBranchCountTable decisionTable) {
    Set<String> minimalOptionSet = new HashSet<>();
    Set<String> options = decisionTable.getOptions();
    Map<Map<String, Boolean>, MutablePair<Integer, Integer>> table = decisionTable.getTable();

    for (String option : options) {
      Set<Map<String, Boolean>> configsWithValuesWhereOptionIsFalse = this
          .getConfigsWithValuesWhereOptionIsFalse(option, table.keySet());

      for (Map<String, Boolean> configWithValuesWhereOptionIsFalse : configsWithValuesWhereOptionIsFalse) {
        Map<String, Boolean> configWithValuesWhereOptionIsTrue = new HashMap<>(
            configWithValuesWhereOptionIsFalse);
        configWithValuesWhereOptionIsTrue.put(option, true);

        boolean equalBranchCounts = table.get(configWithValuesWhereOptionIsFalse)
            .equals(table.get(configWithValuesWhereOptionIsTrue));

        if (!equalBranchCounts) {
          minimalOptionSet.add(option);
          break;
        }
      }
    }

    return minimalOptionSet;
  }

  Set<Set<String>> getContext(DecisionBranchCountTable decisionBranchCountTable) {
    throw new UnsupportedOperationException("Implement");
  }

  private Set<Map<String, Boolean>> getConfigsWithValuesWhereOptionIsFalse(String option,
      Set<Map<String, Boolean>> configsWithValues) {
    Set<Map<String, Boolean>> configsWithValuesWhereOptionIsFalse = new HashSet<>();

    for (Map<String, Boolean> entry : configsWithValues) {
      if (!entry.get(option)) {
        configsWithValuesWhereOptionIsFalse.add(entry);
      }
    }

    return configsWithValuesWhereOptionIsFalse;
  }

}
