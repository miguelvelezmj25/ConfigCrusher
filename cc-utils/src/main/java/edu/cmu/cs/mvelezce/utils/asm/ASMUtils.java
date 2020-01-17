package edu.cmu.cs.mvelezce.utils.asm;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

public final class ASMUtils {

  private ASMUtils() {}

  public static String getClassPackage(ClassNode classNode) {
    return getClassNodePackage(classNode.name);
  }

  public static String getClassNodePackage(String classNodeName) {
    if (classNodeName.equals("[B")) {
      return classNodeName;
    }

    String classPackage = classNodeName.substring(0, classNodeName.lastIndexOf("/"));
    classPackage = classPackage.replace("/", ".");

    return classPackage;
  }

  public static String getClassNodeName(String classNodeName) {
    return classNodeName.replace("/", ".");
  }

  public static String getClassName(ClassNode classNode) {
    String className = classNode.name;
    className = className.substring(className.lastIndexOf("/") + 1);

    return className;
  }

  public static String getMethodSignature(MethodNode methodNode) {
    return methodNode.name + methodNode.desc;
  }
}
