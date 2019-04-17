package edu.cmu.cs.mvelezce.tool.instrumentation.java.transformation.methodnode.javaregion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.apache.commons.lang3.tuple.Pair;
import soot.SootMethod;

public class SootMethodsToMethodNodesMatcher {

  private SootMethodsToMethodNodesMatcher() {
  }

  static void matchSootMethodsToMethodNodes(Set<ClassNode> classNodes,
      Set<SootMethod> applicationSootMethods, Map<SootMethod, MethodNode> sootMethodToMethodNode,
      Map<MethodNode, SootMethod> methodNodeToSootMethod) {
    Map<String, Pair<SootMethod, MethodNode>> qualifiedMethodNamesToPairs = new HashMap<>();

    addSootMethods(qualifiedMethodNamesToPairs, applicationSootMethods);
    addMethodNodes(qualifiedMethodNamesToPairs, classNodes);

    for (Map.Entry<String, Pair<SootMethod, MethodNode>> entry : qualifiedMethodNamesToPairs
        .entrySet()) {
      if (entry.getValue().getRight() == null) {
        // TODO this is braking the static analysis version since it is not correctly reading the Source class from the instrumented program that we want
        throw new RuntimeException("Unable to find method node for " + entry.getKey());
      }
    }

    for (Pair<SootMethod, MethodNode> pair : qualifiedMethodNamesToPairs.values()) {
      SootMethod sootMethod = pair.getLeft();
      MethodNode methodNode = pair.getRight();

      sootMethodToMethodNode.put(sootMethod, methodNode);
      methodNodeToSootMethod.put(methodNode, sootMethod);
    }
  }

  private static void addSootMethods(
      Map<String, Pair<SootMethod, MethodNode>> qualifiedMethodNamesToPairs,
      Set<SootMethod> applicationSootMethods) {
    for (SootMethod sootMethod : applicationSootMethods) {
      String classPackageName = sootMethod.getDeclaringClass().getPackageName();
      String className = sootMethod.getDeclaringClass().getShortName();
      String methodName = sootMethod.getBytecodeSignature();
      methodName = methodName.substring(methodName.indexOf(" "), methodName.length() - 1).trim();

      String qualifiedMethodName = classPackageName + className + methodName;

      if (qualifiedMethodNamesToPairs.containsKey(qualifiedMethodName)) {
        throw new RuntimeException(
            "The method " + qualifiedMethodName + " is duplicated in the application code");
      }

      Pair<SootMethod, MethodNode> pair = Pair.of(sootMethod, null);
      qualifiedMethodNamesToPairs.put(qualifiedMethodName, pair);
    }
  }

  private static void addMethodNodes(
      Map<String, Pair<SootMethod, MethodNode>> qualifiedMethodNamesToPairs,
      Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      List<MethodNode> methodNodes = classNode.methods;
      String classPackageName = RegionTransformer.getClassPackage(classNode);
      String className = RegionTransformer.getClassName(classNode);

      for (MethodNode methodNode : methodNodes) {
        String methodName = RegionTransformer.getMethodName(methodNode);
        String qualifiedMethodName = classPackageName + className + methodName;

        if (!qualifiedMethodNamesToPairs.containsKey(qualifiedMethodName)) {
          continue;
        }

        Pair<SootMethod, MethodNode> pair = qualifiedMethodNamesToPairs.get(qualifiedMethodName);

        if (pair.getRight() != null) {
          throw new RuntimeException(
              "There is already a method node for the method " + qualifiedMethodName);
        }

        Pair<SootMethod, MethodNode> newPair = Pair.of(pair.getLeft(), methodNode);
        qualifiedMethodNamesToPairs.put(qualifiedMethodName, newPair);
      }
    }
  }

}
