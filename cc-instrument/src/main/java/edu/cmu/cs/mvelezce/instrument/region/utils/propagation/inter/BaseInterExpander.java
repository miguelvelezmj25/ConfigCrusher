package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.InstrumenterUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.analysis.utils.inter.BaseInterAnalysisUtils;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.graphBuilder.MethodGraphBuilder;
import edu.cmu.cs.mvelezce.instrument.region.utils.sootAsmMethodMatcher.SootAsmMethodMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.Edge;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BaseInterExpander<T> extends BlockRegionAnalyzer<T> {

  private final BaseInterAnalysisUtils<T> baseInterAnalysisUtils;
  private final SootAsmMethodMatcher sootAsmMethodMatcher;

  public BaseInterExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData,
      BaseInterAnalysisUtils<T> baseInterAnalysisUtils,
      SootAsmMethodMatcher sootAsmMethodMatcher) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.baseInterAnalysisUtils = baseInterAnalysisUtils;
    this.sootAsmMethodMatcher = sootAsmMethodMatcher;
  }

  protected abstract T mergeData(T firstRegionData, @Nullable T callerData);

  protected abstract boolean containsAll(@Nullable T callerData, T firstRegionData);

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    throw new UnsupportedOperationException("Should not be called");
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    throw new UnsupportedOperationException("Should not be called");
  }

  @Override
  protected String debugFileName(String methodName) {
    throw new UnsupportedOperationException("Implement");
    //    return "expandData/" + methodName;
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

    T firstRegionData = this.getData(firstRegion);

    // Collecting this information will make the propagation step much simpler, since we do not need
    // to recalculate blocks to propagate
    Map<MethodNode, Set<MethodBlock>> methodsToBlocksToPropagate =
        this.getMethodsToBlocksToPropagate(firstRegionData, methodNode);

    // If the map is empty, we cannot propagate
    if (methodsToBlocksToPropagate.isEmpty()) {
      return false;
    }

    return this.expandDataUp(firstRegionData, methodsToBlocksToPropagate);
  }

  private boolean expandDataUp(
      T firstRegionData, Map<MethodNode, Set<MethodBlock>> methodsToBlocksToPropagate) {
    boolean expandedDataUp = false;

    for (Map.Entry<MethodNode, Set<MethodBlock>> entry : methodsToBlocksToPropagate.entrySet()) {
      MethodNode callerMethodNode = entry.getKey();
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions =
          this.getBlockRegionMatcher().getMethodNodesToRegionsInBlocks().get(callerMethodNode);
      Set<MethodBlock> callerBlocks = entry.getValue();

      for (MethodBlock callerBlock : callerBlocks) {
        JavaRegion callerRegion = blocksToRegions.get(callerBlock);
        T callerData = this.getData(callerRegion);

        if (firstRegionData.equals(callerData) || this.containsAll(callerData, firstRegionData)) {
          continue;
        }

        if (callerRegion == null) {
          SootMethod callerSootMethod = this.sootAsmMethodMatcher.getSootMethod(callerMethodNode);

          if (callerSootMethod == null) {
            throw new RuntimeException("Could not find a soot method for " + callerMethodNode.name);
          }

          SootClass callerSootClass = callerSootMethod.getDeclaringClass();

          callerRegion =
              new JavaRegion.Builder(
                      callerSootClass.getPackageName(),
                      callerSootClass.getShortName(),
                      InstrumenterUtils.getSootMethodSignature(callerSootMethod))
                  .build();
          blocksToRegions.put(callerBlock, callerRegion);
        }

        T newData = this.mergeData(firstRegionData, callerData);
        this.addRegionToData(callerRegion, newData);
        expandedDataUp = true;
      }
    }

    return expandedDataUp;
  }

  private Map<MethodNode, Set<MethodBlock>> getMethodsToBlocksToPropagate(
      T firstRegionData, MethodNode methodNode) {
    Map<SootMethod, List<Edge>> callerSootMethodsToEdges =
        this.getCallerSootMethodsToEdges(methodNode);

    if (callerSootMethodsToEdges.isEmpty()) {
      return new HashMap<>();
    }

    return this.baseInterAnalysisUtils.canPropagateUpToAllCallers(
        firstRegionData, callerSootMethodsToEdges);
  }

  private Map<SootMethod, List<Edge>> getCallerSootMethodsToEdges(MethodNode methodNode) {
    SootMethod sootMethod = this.sootAsmMethodMatcher.getSootMethod(methodNode);

    if (sootMethod == null) {
      throw new RuntimeException("Could not find a soot method for " + methodNode.name);
    }

    return this.baseInterAnalysisUtils.getCallerSootMethodsToEdges(sootMethod);
  }
}
