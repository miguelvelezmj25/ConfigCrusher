package edu.cmu.cs.mvelezce.instrument.region.utils.removeRegions.inter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.comparator.edge.EdgeComparator;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseRemoveNestedRegionsInter<T> extends BlockRegionAnalyzer<T> {

  private final SootAsmMethodMatcher sootAsmMethodMatcher;
  private final CallGraph callGraph;

  public BaseRemoveNestedRegionsInter(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData,
      SootAsmMethodMatcher sootAsmMethodMatcher,
      CallGraph callGraph) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.sootAsmMethodMatcher = sootAsmMethodMatcher;
    this.callGraph = callGraph;
  }

  protected abstract boolean completelyContainsAll(T coveringData, @Nullable T regionData);

  @Override
  protected String debugFileName(String methodName) {
    return "removeNestedRegionsInter/" + methodName;
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    T callerData = this.getData(region);
    Queue<MethodBlock> worklist = new ArrayDeque<>();
    worklist.add(block);
    Set<MethodBlock> analyzedBlocks = new HashSet<>();

    while (!worklist.isEmpty()) {
      MethodBlock methodBlock = worklist.poll();

      if (analyzedBlocks.contains(methodBlock)) {
        continue;
      }

      analyzedBlocks.add(methodBlock);
      List<AbstractInsnNode> insnList = methodBlock.getInstructions();

      for (AbstractInsnNode insnNode : insnList) {
        int opcode = insnNode.getOpcode();

        if (opcode < Opcodes.INVOKEVIRTUAL || opcode > Opcodes.INVOKEDYNAMIC) {
          continue;
        }

        if (!(insnNode instanceof MethodInsnNode)) {
          throw new RuntimeException(
              "This seems to be an invoke instruction that we needs to handle " + insnNode);
        }

        MethodInsnNode methodInsnNode = ((MethodInsnNode) insnNode);
        String callingPackageName = InstrumenterUtils.getClassNodePackage(methodInsnNode.owner);

        if (!this.sootAsmMethodMatcher.getApplicationPackages().contains(callingPackageName)) {
          continue;
        }

        MethodNode methodNode = this.getBlockRegionMatcher().getMethodNode(methodBlock);

        if (methodNode == null) {
          throw new RuntimeException("Could not find the method node with a specific method block");
        }

        int methodCallIndex = this.getMethodCallIndex(methodInsnNode, methodNode);
        Unit srcUnit = this.getSrcUnit(methodNode, methodInsnNode, methodCallIndex);
        Iterator<Edge> calleeEdges = this.callGraph.edgesOutOf(srcUnit);

        while (calleeEdges.hasNext()) {
          Edge calleeEdge = calleeEdges.next();
          SootMethod targetSootMethod = calleeEdge.tgt();

          if (!this.canRemoveNestedRegion(targetSootMethod, callerData)) {
            continue;
          }

          MethodNode targetMethodNode = this.sootAsmMethodMatcher.getMethodNode(targetSootMethod);
          Set<MethodBlock> blocksWithUncoveredData =
              this.removeCoveredNestedRegions(targetMethodNode, callerData);

          Set<MethodBlock> targetBlocks =
              new HashSet<>(
                  this.getBlockRegionMatcher()
                      .getMethodNodesToRegionsInBlocks()
                      .get(targetMethodNode)
                      .keySet());

          targetBlocks.removeAll(blocksWithUncoveredData);
          worklist.addAll(targetBlocks);
        }
      }
    }

    return new HashSet<>();
  }

  private Set<MethodBlock> removeCoveredNestedRegions(MethodNode targetMethodNode, T callerData) {
    Set<MethodBlock> blocksWithUncoveredData = new HashSet<>();
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(targetMethodNode);

    for (Map.Entry<MethodBlock, JavaRegion> entry : blocksToRegions.entrySet()) {
      MethodBlock block = entry.getKey();
      if (block.getInstructions().isEmpty()) {
        blocksWithUncoveredData.add(block);

        continue;
      }

      JavaRegion region = entry.getValue();
      T regionData = this.getData(region);

      if (!this.completelyContainsAll(callerData, regionData)) {
        blocksWithUncoveredData.add(block);

        continue;
      }

      blocksToRegions.put(entry.getKey(), null);
      this.removeRegionToData(region);
    }

    return blocksWithUncoveredData;
  }

  private boolean canRemoveNestedRegion(SootMethod targetSootMethod, T callerData) {
    Iterator<Edge> callerEdgesIter = this.callGraph.edgesInto(targetSootMethod);
    Set<Edge> callerEdges = new HashSet<>();

    while (callerEdgesIter.hasNext()) {
      Edge edge = callerEdgesIter.next();
      SootMethod srcMethod = edge.src();
      SootClass srcClass = srcMethod.getDeclaringClass();
      String packageName = srcClass.getPackageName();

      if (!this.sootAsmMethodMatcher.getApplicationPackages().contains(packageName)) {
        throw new RuntimeException(
            "Apparently, JRE methods could be callers to application methods. So, we used to check the callers of those JRE methods to find application methods. Not sure if this is still relevant now");
      }

      callerEdges.add(edge);
    }

    if (callerEdges.size() == 1) {
      return true;
    }

    throw new UnsupportedOperationException(
        "Check that the regions in this method can be remove if all callers match? the caller data ");
  }

  private Unit getSrcUnit(
      MethodNode methodNode, MethodInsnNode methodInsnNode, int methodCallIndex) {
    SootMethod sootMethod = this.sootAsmMethodMatcher.getSootMethod(methodNode);

    if (sootMethod == null) {
      throw new RuntimeException("Could not find a soot method for " + methodNode.name);
    }

    int index = 0;
    List<Edge> callerEdges = this.getCallerEdges(sootMethod);

    for (Edge edge : callerEdges) {
      SootMethod targetSootMethod = edge.tgt();
      SootClass targetSootClass = targetSootMethod.getDeclaringClass();
      String targetClassNodeName = InstrumenterUtils.getClassNodeName(methodInsnNode.owner);

      if (!targetSootClass.getName().equals(targetClassNodeName)) {
        continue;
      }

      String targetSootMethodSignature = InstrumenterUtils.getSootMethodSignature(targetSootMethod);
      String targetMethodNodeSignature = methodInsnNode.name + methodInsnNode.desc;

      if (!targetSootMethodSignature.equals(targetMethodNodeSignature)) {
        continue;
      }

      if (index == methodCallIndex) {
        return edge.srcUnit();
      }

      index++;
    }

    throw new RuntimeException(
        "Could not find the source unit to call " + methodInsnNode.name + " in " + methodNode.name);
  }

  private List<Edge> getCallerEdges(SootMethod sootMethod) {
    Iterator<Edge> callerEdges = this.callGraph.edgesOutOf(sootMethod);
    List<Edge> orderedCallerEdges = new ArrayList<>();

    while (callerEdges.hasNext()) {
      orderedCallerEdges.add(callerEdges.next());
    }

    orderedCallerEdges.sort(EdgeComparator.getInstance());

    return orderedCallerEdges;
  }

  private int getMethodCallIndex(MethodInsnNode methodInsnNode, MethodNode methodNode) {
    int methodCallIndex = 0;
    int opcode = methodInsnNode.getOpcode();
    ListIterator<AbstractInsnNode> insnIter = methodNode.instructions.iterator();

    while (insnIter.hasNext()) {
      AbstractInsnNode insnNode = insnIter.next();

      if (insnNode.getOpcode() != opcode) {
        continue;
      }

      if (!(insnNode instanceof MethodInsnNode)) {
        throw new RuntimeException(
            "This seems to be an invoke instruction that we needs to handle " + insnNode);
      }

      MethodInsnNode currentMethodInsnNode = ((MethodInsnNode) insnNode);

      if (!methodInsnNode.owner.equals(currentMethodInsnNode.owner)
          || !methodInsnNode.name.equals(currentMethodInsnNode.name)
          || !methodInsnNode.desc.equals(currentMethodInsnNode.desc)) {
        continue;
      }

      if (methodInsnNode.equals(currentMethodInsnNode)) {
        return methodCallIndex;
      }

      methodCallIndex++;
    }

    throw new RuntimeException(
        "Could not find the index of the method call "
            + InstrumenterUtils.getMethodSignature(methodNode));
  }
}
