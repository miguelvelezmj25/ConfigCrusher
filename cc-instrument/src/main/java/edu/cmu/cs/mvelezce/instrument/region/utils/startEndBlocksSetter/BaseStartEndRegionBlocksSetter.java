package edu.cmu.cs.mvelezce.instrument.region.utils.startEndBlocksSetter;

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

public abstract class BaseStartEndRegionBlocksSetter<T> extends BlockRegionAnalyzer<T> {

  private final SootAsmMethodMatcher sootAsmMethodMatcher;
  private final CallGraph callGraph;

  public BaseStartEndRegionBlocksSetter(
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
    return "setStartEndRegion/" + methodName;
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      MethodNode methodNode) {
    //    System.out.println(graph.toDotString("test"));
    region.setStartMethodBlock(block);
    this.setRegionEndBlocks(block, region, graph, blocksToRegions);

    this.removeCoveredNestedRegions(
        region.getStartMethodBlock(),
        region.getEndMethodBlocks(),
        graph,
        blocksToRegions,
        this.getData(region),
        methodNode);

    return new HashSet<>();
  }

  private void setRegionEndBlocks(
      MethodBlock startBlock,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    //    if (succBlocks.size() == 1 && succBlocks.iterator().next().equals(exit)) {
    //      throw new UnsupportedOperationException("Implement");
    //    }

    MethodBlock ipd = this.getIPD(startBlock, region, graph, blocksToRegions);
    Set<MethodBlock> ends = this.getEndBlocks(startBlock, ipd, graph);
    region.setEndMethodBlocks(ends);
  }

  private MethodBlock getIPD(
      MethodBlock startBlock,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    MethodBlock exit = graph.getExitBlock();
    MethodBlock ipd = graph.getImmediatePostDominator(startBlock);
    JavaRegion ipdRegion = blocksToRegions.get(ipd);
    T ipdData = this.getData(ipdRegion);
    T regionData = this.getData(region);

    if (regionData == null) {
      throw new RuntimeException("The data at this region cannot be null");
    }

    while (!ipd.equals(exit) && regionData.equals(ipdData)) {
      ipd = graph.getImmediatePostDominator(ipd);
      ipdRegion = blocksToRegions.get(ipd);
      ipdData = this.getData(ipdRegion);
    }

    return ipd;
  }

  private void removeCoveredNestedRegions(
      MethodBlock start,
      Set<MethodBlock> endBlocks,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      T regionData,
      MethodNode methodNode) {
    Set<MethodBlock> reachables = new HashSet<>();

    for (MethodBlock endBlock : endBlocks) {
      reachables.addAll(graph.getReachableBlocks(start, endBlock));
    }

    reachables.removeAll(endBlocks);
    reachables.add(start);

    // If the ends are connected to the exit block, we want to analyze them
    MethodBlock exitBlock = graph.getExitBlock();
    for (MethodBlock endBlock : endBlocks) {
      Set<MethodBlock> endBlockSuccs = endBlock.getSuccessors();

      if (endBlockSuccs.size() == 1 && endBlockSuccs.iterator().next().equals(exitBlock)) {
        reachables.add(endBlock);
      }
    }

    this.removeCoveredRegionsInter(reachables, methodNode, regionData);
    reachables.remove(start);
    this.removeCoveredRegionsIntra(reachables, blocksToRegions, regionData);
  }

  private void removeCoveredRegionsInter(
      Set<MethodBlock> reachables, MethodNode methodNode, T callerData) {
    for (MethodBlock methodBlock : reachables) {
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

        int methodCallIndex = this.getMethodCallIndex(methodInsnNode, methodNode);
        Unit srcUnit = this.getSrcUnit(methodNode, methodInsnNode, methodCallIndex);
        Iterator<Edge> calleeEdges = this.callGraph.edgesOutOf(srcUnit);

        while (calleeEdges.hasNext()) {
          Edge calleeEdge = calleeEdges.next();
          SootMethod targetSootMethod = calleeEdge.tgt();
          this.removeCoveredNestedRegions(targetSootMethod, callerData);
        }
      }
    }
  }

  private void removeCoveredNestedRegions(SootMethod targetSootMethod, T callerData) {
    MethodNode targetMethodNode = this.sootAsmMethodMatcher.getMethodNode(targetSootMethod);
    LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
        this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(targetMethodNode);

    for (Map.Entry<MethodBlock, JavaRegion> entry : blocksToRegions.entrySet()) {
      if (entry.getKey().getInstructions().isEmpty()) {
        continue;
      }

      JavaRegion region = entry.getValue();
      T regionData = this.getData(region);

      if (!this.completelyContainsAll(callerData, regionData)) {
        continue;
      }

      blocksToRegions.put(entry.getKey(), null);
      this.removeRegionToData(region);
    }
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

  private void removeCoveredRegionsIntra(
      Set<MethodBlock> reachables,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions,
      T regionData) {
    for (MethodBlock reachable : reachables) {
      JavaRegion reachableRegion = blocksToRegions.get(reachable);

      if (reachableRegion == null) {
        continue;
      }

      T reachableData = this.getData(reachableRegion);

      boolean equals = regionData.equals(reachableData);
      boolean completelyContainsAll = this.completelyContainsAll(regionData, reachableData);

      if (equals != completelyContainsAll) {
        throw new RuntimeException("The equals and completely contains all checks do not match");
      }

      if (!completelyContainsAll) {
        continue;
      }

      blocksToRegions.put(reachable, null);
      this.removeRegionToData(reachableRegion);
    }
  }

  private Set<MethodBlock> getEndBlocks(MethodBlock start, MethodBlock ipd, MethodGraph graph) {
    Set<MethodBlock> ends = new HashSet<>();

    if (start == ipd) {
      throw new RuntimeException("Start and end equal");
    }

    if (graph.getExitBlock().equals(ipd)) {
      //      this.getEndRegionBlocksWithReturn().addAll(ipd.getPredecessors());
      ends.addAll(ipd.getPredecessors());
    } else if (start.getSuccessors().size() == 1
        && start.getSuccessors().iterator().next().equals(ipd)) {
      //      ends.add(start);
      //      if (graph.getExitBlock() == ipd) {
      //        this.getEndRegionBlocksWithReturn().add(start);
      //      }
      throw new RuntimeException("Handle case");
    } else {
      ends.add(ipd);
    }

    return ends;
  }
}
