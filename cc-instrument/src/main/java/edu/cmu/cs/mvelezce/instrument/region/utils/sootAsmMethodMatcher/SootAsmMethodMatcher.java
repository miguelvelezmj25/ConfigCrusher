package edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher;

import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.queue.QueueReader;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SootAsmMethodMatcher {

  private static final SootAsmMethodMatcher INSTANCE = new SootAsmMethodMatcher();

  private final Map<SootMethod, MethodNode> sootMethodsToMethodNodes = new HashMap<>();
  private final Map<MethodNode, SootMethod> methodNodesToSootMethods = new HashMap<>();

  private SootAsmMethodMatcher() {}

  public static SootAsmMethodMatcher getInstance() {
    return INSTANCE;
  }

  public void init(CallGraph callGraph, Set<ClassNode> classNodes) {
    if (!this.sootMethodsToMethodNodes.isEmpty() || !this.methodNodesToSootMethods.isEmpty()) {
      return;
    }

    for (ClassNode classNode : classNodes) {
      if (classNode.name.contains("$")) {
        throw new UnsupportedOperationException("Check the naming of the classes");
      }
    }

    Set<String> applicationPackages = this.getApplicationPackages(classNodes);
    Map<String, MethodNode> fullyQualifiedMethodNodes =
        this.getFullyQualifiedMethodNodes(classNodes);
    Map<String, SootMethod> fullyQualifiedSootMethods =
        this.getFullyQualifiedSootMethods(callGraph, applicationPackages);

    this.matchMethodNodesAndSootMethods(fullyQualifiedMethodNodes, fullyQualifiedSootMethods);
  }

  private void matchMethodNodesAndSootMethods(
      Map<String, MethodNode> fullyQualifiedMethodNodes,
      Map<String, SootMethod> fullyQualifiedSootMethods) {
    for (String fullyQualifiedMethod : fullyQualifiedSootMethods.keySet()) {
      if (!fullyQualifiedMethodNodes.containsKey(fullyQualifiedMethod)) {
        throw new RuntimeException("Could not find the method node " + fullyQualifiedMethod);
      }

      MethodNode methodNode = fullyQualifiedMethodNodes.get(fullyQualifiedMethod);
      SootMethod sootMethod = fullyQualifiedSootMethods.get(fullyQualifiedMethod);

      this.sootMethodsToMethodNodes.put(sootMethod, methodNode);
      this.methodNodesToSootMethods.put(methodNode, sootMethod);
    }

    if (this.sootMethodsToMethodNodes.size() != fullyQualifiedSootMethods.size()
        || this.methodNodesToSootMethods.size() != fullyQualifiedSootMethods.size()) {
      throw new RuntimeException("Did not match all method nodes and all soot methods");
    }
  }

  private Map<String, SootMethod> getFullyQualifiedSootMethods(
      CallGraph callGraph, Set<String> applicationPackages) {
    Map<String, SootMethod> fullyQualifiedNamesToSootMethods = new HashMap<>();
    QueueReader<Edge> edges = callGraph.listener();

    while (edges.hasNext()) {
      Edge edge = edges.next();

      String fullyQualifiedSootMethod = this.getFullyQualifiedName(edge.src(), applicationPackages);

      if (fullyQualifiedSootMethod != null) {
        fullyQualifiedNamesToSootMethods.put(fullyQualifiedSootMethod, edge.src());
      }

      fullyQualifiedSootMethod = this.getFullyQualifiedName(edge.tgt(), applicationPackages);

      if (fullyQualifiedSootMethod != null) {
        fullyQualifiedNamesToSootMethods.put(fullyQualifiedSootMethod, edge.tgt());
      }

      edge.tgt();
    }

    return fullyQualifiedNamesToSootMethods;
  }

  @Nullable
  private String getFullyQualifiedName(SootMethod sootMethod, Set<String> applicationPackages) {
    SootClass sootClass = sootMethod.getDeclaringClass();
    String packageName = sootClass.getPackageName();

    if (!applicationPackages.contains(packageName)) {
      return null;
    }

    String methodSignature = sootMethod.getBytecodeSignature();
    methodSignature = methodSignature.replaceAll("<", "");
    methodSignature = methodSignature.replaceAll(">", "");
    int index = methodSignature.indexOf(":");
    methodSignature = methodSignature.substring(index + 1).trim();

    return this.getFullyQualifiedName(packageName, sootClass.getShortName(), methodSignature);
  }

  private Set<String> getApplicationPackages(Set<ClassNode> classNodes) {
    Set<String> applicationPackages = new HashSet<>();

    for (ClassNode classNode : classNodes) {
      String packageName = InstrumenterUtils.getClassPackage(classNode);
      applicationPackages.add(packageName);
    }

    return applicationPackages;
  }

  private Map<String, MethodNode> getFullyQualifiedMethodNodes(Set<ClassNode> classNodes) {
    Map<String, MethodNode> fullyQualifiedNamesToMethodNodes = new HashMap<>();

    for (ClassNode classNode : classNodes) {
      String packageName = InstrumenterUtils.getClassPackage(classNode);
      String className = InstrumenterUtils.getClassName(classNode);

      for (MethodNode methodNode : classNode.methods) {
        String methodSignature = InstrumenterUtils.getMethodName(methodNode);
        fullyQualifiedNamesToMethodNodes.put(
            this.getFullyQualifiedName(packageName, className, methodSignature), methodNode);
      }
    }

    return fullyQualifiedNamesToMethodNodes;
  }

  private String getFullyQualifiedName(
      String packageName, String className, String methodSignature) {
    return packageName + "." + className + "." + methodSignature;
  }
}
