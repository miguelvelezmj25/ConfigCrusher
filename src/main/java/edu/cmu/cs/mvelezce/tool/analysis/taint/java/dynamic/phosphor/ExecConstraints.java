package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExecConstraints {

  private List<Set<ConfigConstraint>> constraints;

  public ExecConstraints(List<Set<ConfigConstraint>> constraints) {
    this.constraints = constraints;
  }

  public ExecConstraints() {
    this(new ArrayList<>());
  }

  void addExecConstraints(Set<ConfigConstraint> execConstraints) {
    this.constraints.add(execConstraints);
  }

  public List<Set<ConfigConstraint>> getConstraints() {
    return constraints;
  }

  @Override
  public String toString() {
    return constraints.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExecConstraints that = (ExecConstraints) o;

    return constraints.equals(that.constraints);
  }

  @Override
  public int hashCode() {
    return constraints.hashCode();
  }
}
