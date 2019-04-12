package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;

public class PhosphorControlFlowInfo {

  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final int decisionIndex;
  private final InfluencingTaints influencingTaints;

  private PhosphorControlFlowInfo() {
    this.packageName = null;
    this.className = null;
    this.methodSignature = null;
    this.decisionIndex = -1;
    this.influencingTaints = null;
  }

  public PhosphorControlFlowInfo(String packageName, String className, String methodSignature,
      int decisionIndex, InfluencingTaints influencingTaints) {
    this.packageName = packageName;
    this.className = className;
    this.methodSignature = methodSignature;
    this.decisionIndex = decisionIndex;
    this.influencingTaints = influencingTaints;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getClassName() {
    return className;
  }

  public String getMethodSignature() {
    return methodSignature;
  }

  public int getDecisionIndex() {
    return decisionIndex;
  }

  public InfluencingTaints getInfluencingTaints() {
    return influencingTaints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PhosphorControlFlowInfo that = (PhosphorControlFlowInfo) o;

    if (decisionIndex != that.decisionIndex) {
      return false;
    }
    if (!packageName.equals(that.packageName)) {
      return false;
    }
    if (!className.equals(that.className)) {
      return false;
    }
    if (!methodSignature.equals(that.methodSignature)) {
      return false;
    }
    return influencingTaints.equals(that.influencingTaints);
  }

  @Override
  public int hashCode() {
    int result = packageName.hashCode();
    result = 31 * result + className.hashCode();
    result = 31 * result + methodSignature.hashCode();
    result = 31 * result + decisionIndex;
    result = 31 * result + influencingTaints.hashCode();
    return result;
  }
}
