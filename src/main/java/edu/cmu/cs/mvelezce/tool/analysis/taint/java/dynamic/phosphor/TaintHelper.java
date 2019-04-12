package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.cc.DecisionTaints;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class TaintHelper {

  private TaintHelper() {
  }

  public static Set<String> getContextTaints(DecisionTaints decisionTaints, List<String> options) {
    @Nullable Taint contextTaintObject = decisionTaints.getExecCtxTaints();
    Set<String> contextTaints = new HashSet<>();

    if (contextTaintObject != null) {
      contextTaints = getTaintingOptions(contextTaintObject, options);
    }

    return contextTaints;
  }

  public static Set<String> getConditionTaints(DecisionTaints decisionTaints,
      List<String> options) {
    @Nullable Taint conditionTaintObject = decisionTaints.getConditionTaints();
    Set<String> contextTaints = new HashSet<>();

    if (conditionTaintObject != null) {
      contextTaints = getTaintingOptions(conditionTaintObject, options);
    }

    return contextTaints;
  }

  private static Set<String> getTaintingOptions(Taint taint, List<String> options) {
    Set<String> taintingOptions = new HashSet<>();
    int[] tags = taint.getTags();

    if (tags == null) {
      throw new RuntimeException("You need to use the tags array for tainting");
    }

    if (tags.length > 1) {
      throw new RuntimeException("Implement how to handle array tags with more than 1 entry");
    }

    int tag = tags[0];

    for (int i = 0; tag != 0; i++) {
      if (tag % 2 == 1) {
        String taintingOption = options.get(i);
        taintingOptions.add(taintingOption);
      }

      tag = tag >> 1;
    }

    return taintingOptions;
  }

}
