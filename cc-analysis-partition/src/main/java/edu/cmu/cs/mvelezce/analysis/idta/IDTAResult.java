package edu.cmu.cs.mvelezce.analysis.idta;

import java.util.HashSet;
import java.util.Set;

public class IDTAResult {

  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final int decisionIndex;
  private final Set<String> info;

  private IDTAResult() {
    this.packageName = "";
    this.className = "";
    this.methodSignature = "";
    this.decisionIndex = -1;
    this.info = new HashSet<>();
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

  public Set<String> getInfo() {
    return info;
  }
}
