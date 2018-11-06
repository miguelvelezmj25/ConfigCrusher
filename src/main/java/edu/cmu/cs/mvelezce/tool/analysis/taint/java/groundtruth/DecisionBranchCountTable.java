package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.MutablePair;

public class DecisionBranchCountTable {

  private final Set<String> options;
  // TODO change to elseThenCountClass
  private final Map<Map<String, Boolean>, MutablePair<Integer, Integer>> table = new HashMap<>();

  // Dummy constructor needed for jackson xml
  private DecisionBranchCountTable() {
    this.options = new HashSet<>();
  }

  public DecisionBranchCountTable(Set<String> options) {
    this.options = options;
  }

  void addEntry(Set<String> config, MutablePair<Integer, Integer> thenElseCounts) {
    Map<String, Boolean> configToValues = Constraint.toConfigWithValues(config, this.options);
    this.table.put(configToValues, thenElseCounts);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    List<String> orderedOptions = new ArrayList<>();

    // Header
    for (String option : this.options) {
      orderedOptions.add(option);
      stringBuilder.append("| ");
      stringBuilder.append(option);
      stringBuilder.append(" ");
    }

    stringBuilder.append("|| ");
    stringBuilder.append("T | E |");
    stringBuilder.append("\n");

    // Break line
    int headerLength = stringBuilder.length();

    for (int i = 0; i < (headerLength - 1); i++) {
      stringBuilder.append("-");
    }

    stringBuilder.append("\n");

    // Entries
    for (Map.Entry<Map<String, Boolean>, MutablePair<Integer, Integer>> entry : this.table.entrySet()) {
      Map<String, Boolean> configsToValues = entry.getKey();

      for (String option : orderedOptions) {
        stringBuilder.append("| ");

        boolean configValue = configsToValues.get(option);
        stringBuilder.append(this.getSingleLetterBoolean(configValue));

        stringBuilder.append(" ");
      }

      stringBuilder.append("|| ");

      MutablePair<Integer, Integer> thenElsePair = entry.getValue();
      stringBuilder.append(thenElsePair.getLeft());
      stringBuilder.append(" | ");
      stringBuilder.append(thenElsePair.getRight());

      stringBuilder.append(" |");
      stringBuilder.append("\n");

    }

    return stringBuilder.toString();
  }

  private String getSingleLetterBoolean(boolean value) {
    if (value) {
      return "T";
    }

    return "F";
  }

//  public Map<Map<String, Boolean>, Boolean> getTable() {
//    return table;
//  }


  public Map<Map<String, Boolean>, MutablePair<Integer, Integer>> getTable() {
    return table;
  }

  public Set<String> getOptions() {
    return options;
  }
}
