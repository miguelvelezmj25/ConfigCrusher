package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import jdk.internal.org.objectweb.asm.tree.ClassNode;

public class Utils {

  // Suppress default constructor for noninstantiability
  private Utils() {
    throw new UnsupportedOperationException();
  }

  public static String getPackageName(ClassNode classNode) {
    String name = classNode.name;
    String sourceFile = classNode.sourceFile;
    String className = sourceFile.replace(".java", "");
    String packageName = name.replace(className, "");

    packageName = packageName.substring(0, packageName.length() - 1);

    return packageName;
  }

  public static String getClassName(ClassNode classNode) {
    String sourceFile = classNode.sourceFile;

    return sourceFile.replace(".java", "");
  }

}
