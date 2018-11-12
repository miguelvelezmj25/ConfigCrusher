package edu.cmu.cs.mvelezce.tool.analysis.taint.java.groundtruth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Context implements Serializable {

  private static final long serialVersionUID = 5228402185734425841L;

  private final Set<Set<String>> context = new HashSet<>();

  void addConfig(Set<String> config) {
    this.context.add(config);
  }

  public Set<Set<String>> getContext() {
    return context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Context context1 = (Context) o;

    return context.equals(context1.context);
  }

  @Override
  public int hashCode() {
    return context.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");

    Iterator<Set<String>> configsIter = this.context.iterator();

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
