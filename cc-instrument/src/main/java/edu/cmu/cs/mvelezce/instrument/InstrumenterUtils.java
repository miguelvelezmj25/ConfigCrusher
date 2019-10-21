package edu.cmu.cs.mvelezce.instrument;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

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
    String classPackage = classNode.name;
    classPackage = classPackage.substring(0, classPackage.lastIndexOf("/"));
    classPackage = classPackage.replace("/", ".");

    return classPackage;
  }

  public static String getClassName(ClassNode classNode) {
    String className = classNode.name;
    className = className.substring(className.lastIndexOf("/") + 1);

    return className;
  }

  public static String getMethodName(MethodNode methodNode) {
    return methodNode.name + methodNode.desc;
  }

  public static Set<JavaRegion> getRegionsInMethod(
      MethodNode methodNode, ClassNode classNode, Set<JavaRegion> regions) {
    String classPackage = getClassPackage(classNode);
    String className = getClassName(classNode);
    String methodName = getMethodName(methodNode);

    Set<JavaRegion> regionsInMethod = new HashSet<>();

    for (JavaRegion region : regions) {
      if (region.getRegionPackage().equals(classPackage)
          && region.getRegionClass().equals(className)
          && region.getRegionMethod().equals(methodName)) {
        regionsInMethod.add(region);
      }
    }

    return regionsInMethod;
  }
}
