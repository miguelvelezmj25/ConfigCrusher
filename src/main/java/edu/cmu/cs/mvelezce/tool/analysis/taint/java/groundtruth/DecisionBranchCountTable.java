package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Deprecated
public class DecisionBranchCountTable {

  private final Set<String> options;
  private final Map<Map<String, Boolean>, ThenElseCounts> table = new HashMap<>();

  // Dummy constructor needed for jackson xml
  private DecisionBranchCountTable() {
    this.options = new HashSet<>();
  }

  public DecisionBranchCountTable(Set<String> options) {
    this.options = options;
  }

  void addEntry(Set<String> config, ThenElseCounts thenElseCounts) {
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
    for (Entry<Map<String, Boolean>, ThenElseCounts> entry : this.table.entrySet()) {
      Map<String, Boolean> configsToValues = entry.getKey();

      for (String option : orderedOptions) {
        stringBuilder.append("| ");

        boolean configValue = configsToValues.get(option);
        stringBuilder.append(this.getSingleLetterBoolean(configValue));

        stringBuilder.append(" ");
      }

      stringBuilder.append("|| ");

      ThenElseCounts thenElsePair = entry.getValue();
      stringBuilder.append(thenElsePair.getThenCount());
      stringBuilder.append(" | ");
      stringBuilder.append(thenElsePair.getElseCount());

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

  public Map<Map<String, Boolean>, ThenElseCounts> getTable() {
    return table;
  }

  public Set<String> getOptions() {
    return options;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DecisionBranchCountTable that = (DecisionBranchCountTable) o;

    if (!options.equals(that.options)) {
      return false;
    }
    return table.equals(that.table);
  }

  @Override
  public int hashCode() {
    int result = options.hashCode();
    result = 31 * result + table.hashCode();
    return result;
  }
}
