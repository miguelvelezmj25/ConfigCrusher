package edu.cmu.cs.mvelezce.instrument;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.SootMethod;

import java.util.HashSet;
import java.util.Set;

public class InstrumenterUtils {

  private InstrumenterUtils() {}

  public static Set<JavaRegion> getRegionsInClass(
      ClassNode classNode, Set<JavaRegion> javaRegions) {
    String classPackage = getClassPackage(classNode);
    String className = getClassName(classNode);

    Set<JavaRegion> regionsInClass = new HashSet<>();

    for (JavaRegion javaRegion : javaRegions) {
      if (javaRegion.getRegionPackage().equals(classPackage)
          && javaRegion.getRegionClass().equals(className)) {
        regionsInClass.add(javaRegion);
      }
    }

    return regionsInClass;
  }

  public static String getClassPackage(ClassNode classNode) {
    return getClassNodePackage(classNode.name);
  }

  public static String getClassNodePackage(String classNodeName) {
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

  public static Set<JavaRegion> getRegionsInMethod(
      MethodNode methodNode, ClassNode classNode, Set<JavaRegion> regions) {
    String classPackage = getClassPackage(classNode);
    String className = getClassName(classNode);
    String methodSignature = getMethodSignature(methodNode);

    Set<JavaRegion> regionsInMethod = new HashSet<>();

    for (JavaRegion region : regions) {
      if (region.getRegionPackage().equals(classPackage)
          && region.getRegionClass().equals(className)
          && region.getRegionMethodSignature().equals(methodSignature)) {
        regionsInMethod.add(region);
      }
    }

    return regionsInMethod;
  }

  public static String getSootMethodSignature(SootMethod sootMethod) {
    String methodSignature = sootMethod.getBytecodeSignature();
    if (methodSignature.charAt(0) != '<') {
      throw new RuntimeException(
          "Expected the first char in the method signature to be '<', but this is the method signature "
              + methodSignature);
    }

    if (methodSignature.charAt(methodSignature.length() - 1) != '>') {
      throw new RuntimeException(
          "Expected the last char in the method signature to be '>', but this is the method signature "
              + methodSignature);
    }

    methodSignature = methodSignature.substring(1, methodSignature.length() - 1);
    int index = methodSignature.indexOf(":");
    return methodSignature.substring(index + 1).trim();
  }
}
