package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Ordered lists of taints that reached a sink.
 *
 * The taints are represented as set of options.
 *
 * Example: A sink was reached with { {A}, {B} }
 */
public class ExecTaints {

  private List<Set<String>> taints;

  public ExecTaints(List<Set<String>> taints) {
    this.taints = taints;
  }

  public ExecTaints() {
    this(new ArrayList<>());
  }

//  void addExecTaints(List<Set<String>> execTaints) {
//    this.taints.addAll(execTaints);
//  }

  public List<Set<String>> getTaints() {
    return taints;
  }

//  public void setTaints(Set<Set<String>> taints) {
//    this.taints = taints;
//  }

  @Override
  public String toString() {
    return taints.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ExecTaints that = (ExecTaints) o;

    return taints.equals(that.taints);
  }

  @Override
  public int hashCode() {
    return taints.hashCode();
  }
}
