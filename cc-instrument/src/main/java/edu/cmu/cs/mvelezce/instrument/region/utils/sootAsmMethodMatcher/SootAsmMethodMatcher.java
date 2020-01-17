package edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher;

import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.utils.asm.ASMUtils;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.apache.commons.lang3.tuple.Pair;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.StaticFieldRef;
import soot.jimple.internal.*;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.queue.QueueReader;

import javax.annotation.Nullable;
import java.util.*;

public final class SootAsmMethodMatcher {

  private static final SootAsmMethodMatcher INSTANCE = new SootAsmMethodMatcher();

  private final Set<String> applicationPackages = new HashSet<>();
  private final Map<SootMethod, MethodNode> sootMethodsToMethodNodes = new HashMap<>();
  private final Map<MethodNode, SootMethod> methodNodesToSootMethods = new HashMap<>();
  private final Map<Class<? extends Value>, Pair<Integer, Integer>> sootInvokesToOpcodes =
      new HashMap<>();
  private final Map<Integer, Class<? extends Value>> opcodesToSootInvokes = new HashMap<>();
  private final Set<ClassNode> classNodesToConsider = new HashSet<>();

  private SootAsmMethodMatcher() {
    sootInvokesToOpcodes.put(
        JStaticInvokeExpr.class, Pair.of(Opcodes.INVOKESTATIC, Opcodes.INVOKESTATIC));
    sootInvokesToOpcodes.put(
        JSpecialInvokeExpr.class, Pair.of(Opcodes.INVOKESPECIAL, Opcodes.INVOKESPECIAL));
    sootInvokesToOpcodes.put(
        JVirtualInvokeExpr.class, Pair.of(Opcodes.INVOKEVIRTUAL, Opcodes.INVOKEVIRTUAL));
    sootInvokesToOpcodes.put(
        JInterfaceInvokeExpr.class, Pair.of(Opcodes.INVOKEINTERFACE, Opcodes.INVOKEINTERFACE));
    sootInvokesToOpcodes.put(StaticFieldRef.class, Pair.of(Opcodes.PUTSTATIC, Opcodes.GETSTATIC));
    sootInvokesToOpcodes.put(JNewExpr.class, Pair.of(Opcodes.NEW, Opcodes.NEW));
    sootInvokesToOpcodes.put(JNewArrayExpr.class, Pair.of(Opcodes.NEWARRAY, Opcodes.ANEWARRAY));

    opcodesToSootInvokes.put(Opcodes.INVOKESTATIC, JStaticInvokeExpr.class);
    opcodesToSootInvokes.put(Opcodes.INVOKESPECIAL, JSpecialInvokeExpr.class);
    opcodesToSootInvokes.put(Opcodes.INVOKEVIRTUAL, JVirtualInvokeExpr.class);
    opcodesToSootInvokes.put(Opcodes.INVOKEINTERFACE, JInterfaceInvokeExpr.class);
    opcodesToSootInvokes.put(Opcodes.GETSTATIC, StaticFieldRef.class);
    opcodesToSootInvokes.put(Opcodes.PUTSTATIC, StaticFieldRef.class);
    opcodesToSootInvokes.put(Opcodes.NEW, JNewExpr.class);
    opcodesToSootInvokes.put(Opcodes.NEWARRAY, JNewArrayExpr.class);
    opcodesToSootInvokes.put(Opcodes.ANEWARRAY, JNewArrayExpr.class);
  }

  public static SootAsmMethodMatcher getInstance() {
    return INSTANCE;
  }

  public void init(CallGraph callGraph, Set<ClassNode> classNodes) {
    if (!this.sootMethodsToMethodNodes.isEmpty() || !this.methodNodesToSootMethods.isEmpty()) {
      return;
    }

    this.calcApplicationPackages(classNodes);

    Map<String, MethodNode> fullyQualifiedMethodNodes =
        this.getFullyQualifiedMethodNodes(classNodes);
    Map<String, SootMethod> fullyQualifiedSootMethods =
        this.getFullyQualifiedSootMethods(callGraph);

    this.matchMethodNodesAndSootMethods(fullyQualifiedMethodNodes, fullyQualifiedSootMethods);
    this.getClassNodesToConsider(classNodes);
  }

  private void getClassNodesToConsider(Set<ClassNode> classNodes) {
    Set<MethodNode> methodNodesToConsider = this.methodNodesToSootMethods.keySet();

    for (ClassNode classNode : classNodes) {
      List<MethodNode> methodNodes = classNode.methods;

      for (MethodNode methodNode : methodNodes) {
        if (methodNodesToConsider.contains(methodNode)) {
          this.classNodesToConsider.add(classNode);
          break;
        }
      }
    }
  }

  @Nullable
  public MethodNode getMethodNode(SootMethod sootMethod) {
    return this.sootMethodsToMethodNodes.get(sootMethod);
  }

  @Nullable
  public SootMethod getSootMethod(MethodNode methodNode) {
    return this.methodNodesToSootMethods.get(methodNode);
  }

  @Nullable
  public Integer getOpcode(Class<? extends Value> invokeExpr) {
    return this.getOpcode(invokeExpr, false);
  }

  @Nullable
  public Integer getOpcodeArrayType(Class<? extends Value> invokeExpr, boolean primType) {
    return this.getOpcode(invokeExpr, primType);
  }

  @Nullable
  public Integer getOpcodeStaticRef(Class<? extends Value> invokeExpr, boolean assign) {
    return this.getOpcode(invokeExpr, assign);
  }

  @Nullable
  private Integer getOpcode(Class<? extends Value> invokeExpr, boolean condition) {
    Pair<Integer, Integer> opcodes = sootInvokesToOpcodes.get(invokeExpr);

    if (condition) {
      return opcodes.getLeft();
    }

    return opcodes.getRight();
  }

  @Nullable
  public Class<? extends Value> getSootInvokeExpr(int opcode) {
    return opcodesToSootInvokes.get(opcode);
  }

  public Set<String> getApplicationPackages() {
    return applicationPackages;
  }

  public Set<ClassNode> getClassNodesToConsider() {
    return classNodesToConsider;
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

  private Map<String, SootMethod> getFullyQualifiedSootMethods(CallGraph callGraph) {
    Map<String, SootMethod> fullyQualifiedNamesToSootMethods = new HashMap<>();
    QueueReader<Edge> edges = callGraph.listener();

    while (edges.hasNext()) {
      Edge edge = edges.next();

      String fullyQualifiedSootMethod = this.getFullyQualifiedName(edge.src());

      if (fullyQualifiedSootMethod != null) {
        fullyQualifiedNamesToSootMethods.put(fullyQualifiedSootMethod, edge.src());
      }

      fullyQualifiedSootMethod = this.getFullyQualifiedName(edge.tgt());

      if (fullyQualifiedSootMethod != null) {
        fullyQualifiedNamesToSootMethods.put(fullyQualifiedSootMethod, edge.tgt());
      }
    }

    return fullyQualifiedNamesToSootMethods;
  }

  @Nullable
  private String getFullyQualifiedName(SootMethod sootMethod) {
    SootClass sootClass = sootMethod.getDeclaringClass();
    String packageName = sootClass.getPackageName();

    if (!this.applicationPackages.contains(packageName)) {
      return null;
    }

    String methodSignature = InstrumenterUtils.getSootMethodSignature(sootMethod);

    return this.getFullyQualifiedName(packageName, sootClass.getShortName(), methodSignature);
  }

  private void calcApplicationPackages(Set<ClassNode> classNodes) {
    for (ClassNode classNode : classNodes) {
      String packageName = ASMUtils.getClassPackage(classNode);
      this.applicationPackages.add(packageName);
    }
  }

  private Map<String, MethodNode> getFullyQualifiedMethodNodes(Set<ClassNode> classNodes) {
    Map<String, MethodNode> fullyQualifiedNamesToMethodNodes = new HashMap<>();

    for (ClassNode classNode : classNodes) {
      String packageName = ASMUtils.getClassPackage(classNode);
      String className = ASMUtils.getClassName(classNode);

      for (MethodNode methodNode : classNode.methods) {
        String methodSignature = ASMUtils.getMethodSignature(methodNode);
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
