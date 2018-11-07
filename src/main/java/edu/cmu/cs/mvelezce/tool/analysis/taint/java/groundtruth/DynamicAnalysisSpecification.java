package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DynamicAnalysisSpecification {

  static Set<String> getMinimalSetOfOptions(DecisionBranchCountTable decisionTable) {
    Set<String> minimalOptionSet = new HashSet<>();
    Set<String> options = decisionTable.getOptions();
    Map<Map<String, Boolean>, ThenElseCounts> table = decisionTable.getTable();

    for (String option : options) {
      Set<Map<String, Boolean>> configsWithValuesWhereOptionIsFalse = DynamicAnalysisSpecification
          .getConfigsWithValuesWhereOptionIsFalse(option, table.keySet());

      // Takes care of the case where there are no executed configs with the option set to false
      for (Map<String, Boolean> configWithValuesWhereOptionIsFalse : configsWithValuesWhereOptionIsFalse) {
        Map<String, Boolean> configWithValuesWhereOptionIsTrue = new HashMap<>(
            configWithValuesWhereOptionIsFalse);
        configWithValuesWhereOptionIsTrue.put(option, true);

        ThenElseCounts thenElseCountsWithTrue = table.get(configWithValuesWhereOptionIsTrue);

        // Takes care of the case where there are no executed configs with the option set to true
        if (thenElseCountsWithTrue == null) {
          continue;
        }

        boolean equalBranchCounts = table.get(configWithValuesWhereOptionIsFalse)
            .equals(thenElseCountsWithTrue);

        if (!equalBranchCounts) {
          minimalOptionSet.add(option);
          break;
        }
      }
    }

    return minimalOptionSet;
  }

  static Set<Set<String>> getContext(DecisionInfo decisionInfo) {
    return decisionInfo.getContext();
  }

  private static Set<Map<String, Boolean>> getConfigsWithValuesWhereOptionIsFalse(String option,
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
