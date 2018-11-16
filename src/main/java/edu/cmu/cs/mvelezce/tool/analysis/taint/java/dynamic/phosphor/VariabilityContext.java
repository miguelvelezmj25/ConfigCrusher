package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class VariabilityContext extends PartialConfig {

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[[");

    Map<String, Boolean> partialConfig = this.getPartialConfig();

    if (partialConfig.isEmpty()) {
      stringBuilder.append("true");
    }
    else {
      Iterator<Entry<String, Boolean>> partialConfigIter = partialConfig.entrySet().iterator();

      while (partialConfigIter.hasNext()) {
        Map.Entry<String, Boolean> entry = partialConfigIter.next();

        if (!entry.getValue()) {
          stringBuilder.append("!");
        }

        stringBuilder.append(entry.getKey());

        if(partialConfigIter.hasNext()) {
          stringBuilder.append(" ");
          stringBuilder.append("^");
          stringBuilder.append(" ");
        }
      }
    }

    stringBuilder.append("]]");

    return stringBuilder.toString();
  }

}
