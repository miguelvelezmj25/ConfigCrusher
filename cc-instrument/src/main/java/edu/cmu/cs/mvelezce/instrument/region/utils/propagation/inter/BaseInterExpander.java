package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.BytecodeOffsetTag;
import soot.tagkit.Tag;

import java.util.*;

public abstract class BaseInterExpander<T> extends BlockRegionAnalyzer<T> {

  private static final Comparator<Edge> EDGE_COMPARATOR = new EdgeComparator();

  private final CallGraph callGraph;
  private final SootAsmMethodMatcher sootAsmMethodMatcher;

  public BaseInterExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData,
      CallGraph callGraph,
      SootAsmMethodMatcher sootAsmMethodMatcher) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.callGraph = callGraph;
    this.sootAsmMethodMatcher = sootAsmMethodMatcher;
  }

  @Override
  public boolean processBlocks(MethodNode methodNode, ClassNode classNode) {
    MethodGraph graph = MethodGraphBuilder.getMethodGraph(methodNode, classNode);

    if (!graph.isConnectedToExit(graph.getEntryBlock())) {
      throw new RuntimeException(
          "This graph is not connected to the exit block "
              + classNode.name
              + " - "
              + methodNode.name);
    }

    Set<MethodBlock> entrySuccs = graph.getEntryBlock().getSuccessors();

    if (entrySuccs.size() > 1) {
      throw new RuntimeException(
          "The entry node of this graph has multiple successors "
              + classNode.name
              + " - "
              + methodNode.name);
    }

    MethodBlock firstBlock = entrySuccs.iterator().next();
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(methodNode);
    JavaRegion firstRegion = blocksToRegions.get(firstBlock);

    if (firstRegion == null) {
      return false;
    }

    if (!this.canPropagateUp(methodNode)) {
      return false;
    }

    throw new UnsupportedOperationException("Implement");
  }

  private boolean canPropagateUp(MethodNode methodNode) {
    SootMethod sootMethod = this.sootAsmMethodMatcher.getSootMethod(methodNode);

    if (sootMethod == null) {
      throw new RuntimeException("Could not find a soot method for " + methodNode.name);
    }

    Map<SootMethod, List<Edge>> callerSootMethodsToEdges =
        this.getCallerSootMethodsToEdges(sootMethod);

    if (callerSootMethodsToEdges.isEmpty()) {
      return false;
    }

    return this.some(callerSootMethodsToEdges);
  }

  private boolean some(Map<SootMethod, List<Edge>> callerSootMethodsToEdges) {
    for (Map.Entry<SootMethod, List<Edge>> entry : callerSootMethodsToEdges.entrySet()) {
      List<Edge> edges = entry.getValue();

      for (int i = 0; i < edges.size(); i++) {
        Edge edge = edges.get(i);
        AbstractInsnNode callerInsn = this.getCallerInsn(edge, i);

        System.out.println();
      }
    }
    throw new UnsupportedOperationException("Implement");
  }

  private AbstractInsnNode getCallerInsn(Edge edge, int invokeIndex) {
    SootMethod srcSootMethod = edge.src();
    MethodNode srcMethodNode = this.sootAsmMethodMatcher.getMethodNode(srcSootMethod);

    if (srcMethodNode == null) {
      throw new RuntimeException("Could not find a method node for " + srcSootMethod);
    }

    ListIterator<AbstractInsnNode> insnIter = srcMethodNode.instructions.iterator();

    int srcOpcode = this.getSrcOpcode(edge.srcUnit());
    int invokeCount = 0;
    SootMethod tgtSootMethod = edge.tgt();
    SootClass tgtSootClass = tgtSootMethod.getDeclaringClass();
    String tgtPackageName = tgtSootClass.getPackageName();
    String tgtClassName = tgtSootClass.getShortName();
    String tgtQualifiedClassName = tgtPackageName + "." + tgtClassName;
    String tgtSootMethodSignature = InstrumenterUtils.getSootMethodSignature(tgtSootMethod);

    while (insnIter.hasNext()) {
      AbstractInsnNode insn = insnIter.next();

      if (insn.getOpcode() != srcOpcode) {
        continue;
      }

      if (!this.matchesMethodInvocation(insn, tgtQualifiedClassName, tgtSootMethodSignature)) {
        continue;
      }

      if (invokeCount != invokeIndex) {
        invokeCount++;

        continue;
      }

      return insn;
    }

    throw new RuntimeException("Could not find the instruction " + edge);
  }

  private boolean matchesMethodInvocation(
      AbstractInsnNode invokeInsn, String tgtQualifiedClassName, String tgtMethodSignature) {
    if (!(invokeInsn instanceof MethodInsnNode)) {
      throw new RuntimeException(
          "This seems to be an invoke instruction that we need to handle " + invokeInsn);
    }

    MethodInsnNode methodInsnNode = ((MethodInsnNode) invokeInsn);

    if (!methodInsnNode.owner.replace("/", ".").equals(tgtQualifiedClassName)) {
      return false;
    }

    return (methodInsnNode.name + methodInsnNode.desc).equals(tgtMethodSignature);
  }

  private int getSrcOpcode(Unit srcUnit) {
    if (!(srcUnit instanceof JInvokeStmt)) {
      throw new RuntimeException("Expected this statement to be a method invocation");
    }

    InvokeExpr invokeExpr = ((JInvokeStmt) srcUnit).getInvokeExpr();
    Integer opcode = this.sootAsmMethodMatcher.getOpcode(invokeExpr.getClass());

    if (opcode == null) {
      throw new RuntimeException("Could not find an opcode for " + invokeExpr.getClass());
    }

    return opcode;
  }

  private Map<SootMethod, List<Edge>> getCallerSootMethodsToEdges(SootMethod sootMethod) {
    Map<SootMethod, List<Edge>> callerSootMethodsToEdges = new HashMap<>();
    Iterator<Edge> edgesInto = this.callGraph.edgesInto(sootMethod);

    while (edgesInto.hasNext()) {
      Edge edge = edgesInto.next();
      SootMethod srcMethod = edge.src();
      SootClass srcClass = srcMethod.getDeclaringClass();
      String packageName = srcClass.getPackageName();

      if (!this.sootAsmMethodMatcher.getApplicationPackages().contains(packageName)) {
        throw new RuntimeException(
            "Apparently, JRE methods could be callers to application methods. So, we used to check the callers of those JRE methods to find application methods. Not sure if this is still relevant now");
      }

      callerSootMethodsToEdges.put(srcMethod, new ArrayList<>());
    }

    edgesInto = this.callGraph.edgesInto(sootMethod);

    while (edgesInto.hasNext()) {
      Edge edge = edgesInto.next();
      SootMethod srcMethod = edge.src();

      List<Edge> edges = callerSootMethodsToEdges.get(srcMethod);

      if (edges == null) {
        continue;
      }

      edges.add(edge);
    }

    for (Map.Entry<SootMethod, List<Edge>> entry : callerSootMethodsToEdges.entrySet()) {
      entry.getValue().sort(EDGE_COMPARATOR);
    }

    return callerSootMethodsToEdges;
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    throw new UnsupportedOperationException("Implement");
  }

  @Override
  protected String debugFileName(String methodName) {
    throw new UnsupportedOperationException("Implement");
    //    return "expandData/" + methodName;
  }

  private static class EdgeComparator implements Comparator<Edge> {

    @Override
    public int compare(Edge e1, Edge e2) {
      String sig1 = e1.src().getBytecodeSignature();
      String sig2 = e2.src().getBytecodeSignature();
      int compare1 = sig1.compareTo(sig2);

      if (compare1 != 0) {
        return compare1;
      }

      int tag1 = this.getBytecodeOffsetTag(e1);
      int tag2 = this.getBytecodeOffsetTag(e2);

      return Integer.compare(tag1, tag2);
    }

    private int getBytecodeOffsetTag(Edge edge) {
      for (Tag tag : edge.srcUnit().getTags()) {
        if (tag instanceof BytecodeOffsetTag) {
          return ((BytecodeOffsetTag) tag).getBytecodeOffset();
        }
      }

      throw new RuntimeException("Could not find a bytecode offset tag for this edge " + edge);
    }
  }
}
