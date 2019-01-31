package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Deprecated
public class VariabilityCtx implements Serializable {

  private static final long serialVersionUID = 5228402185734425841L;

  private final Set<Set<String>> ctx = new HashSet<>();

  public void addConfig(Set<String> config) {
    this.ctx.add(config);
  }

  // TODO change method name to getConfigs
  public Set<Set<String>> getCtx() {
    return ctx;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VariabilityCtx variabilityCtx1 = (VariabilityCtx) o;

    return ctx.equals(variabilityCtx1.ctx);
  }

  @Override
  public int hashCode() {
    return ctx.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");

    Iterator<Set<String>> configsIter = this.ctx.iterator();

    while (configsIter.hasNext()) {
      Set<String> config = configsIter.next();
      stringBuilder.append(config);

      if (configsIter.hasNext()) {
        stringBuilder.append(", ");
      }
    }

    stringBuilder.append("]");

    return stringBuilder.toString();
  }
}
