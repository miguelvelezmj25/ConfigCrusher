package edu.cmu.cs.mvelezce.tool.analysis.region;

public class CFStatement {

  private final String packageName;
  private final String className;
  private final String methodSignature;
  private final int index;

  public CFStatement(String packageName, String className, String methodSignature, int index) {
    this.packageName = packageName;
    this.className = className;
    this.methodSignature = methodSignature;
    this.index = index;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CFStatement that = (CFStatement) o;

    if (index != that.index) {
      return false;
    }
    if (!packageName.equals(that.packageName)) {
      return false;
    }
    if (!className.equals(that.className)) {
      return false;
    }
    return methodSignature.equals(that.methodSignature);

  }

  @Override
  public int hashCode() {
    int result = packageName.hashCode();
    result = 31 * result + className.hashCode();
    result = 31 * result + methodSignature.hashCode();
    result = 31 * result + index;
    return result;
  }

  @Override
  public String toString() {
    return packageName + "." + className + "." + methodSignature + "." + index;
  }
}
