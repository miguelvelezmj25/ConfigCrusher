package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.Constraint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExecutionTruthTable {

  private final Map<Map<String, Boolean>, Boolean> table = new HashMap<>();
  private final Set<String> options;

  ExecutionTruthTable(Set<String> options) {
    this.options = options;
  }

  void addEntry(Set<String> config, boolean value) {
    Map<String, Boolean> configToValues = Constraint.toConfigWithValues(config, this.options);
    this.table.put(configToValues, value);
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
    stringBuilder.append("V |");
    stringBuilder.append("\n");

    // Break line
    int headerLength = stringBuilder.length();

    for (int i = 0; i < (headerLength - 1); i++) {
      stringBuilder.append("-");
    }

    stringBuilder.append("\n");

    // Entries
    for (Map.Entry<Map<String, Boolean>, Boolean> entry : this.table.entrySet()) {
      Map<String, Boolean> configsToValues = entry.getKey();

      for (String option : orderedOptions) {
        stringBuilder.append("| ");

        boolean configValue = configsToValues.get(option);
        stringBuilder.append(this.getSingleLetterBoolean(configValue));

        stringBuilder.append(" ");
      }

      stringBuilder.append("|| ");

      boolean executed = entry.getValue();
      stringBuilder.append(this.getSingleLetterBoolean(executed));

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
}
