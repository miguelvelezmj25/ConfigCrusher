package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExecConfigConstraints {

  private List<Set<ConfigConstraint>> configConstraints;

  public ExecConfigConstraints(List<Set<ConfigConstraint>> constraints) {
    this.configConstraints = constraints;
  }

  public ExecConfigConstraints() {
    this(new ArrayList<>());
  }

  void addExecConstraints(Set<ConfigConstraint> execConstraints) {
    this.configConstraints.add(execConstraints);
  }

  public List<Set<ConfigConstraint>> getConfigConstraints() {
    return configConstraints;
  }

  @Override
  public String toString() {
    return configConstraints.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExecConfigConstraints that = (ExecConfigConstraints) o;

    return configConstraints.equals(that.configConstraints);
  }

  @Override
  public int hashCode() {
    return configConstraints.hashCode();
  }
}
