package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.inter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
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
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

import java.util.*;

public abstract class BaseInterExpander<T> extends BlockRegionAnalyzer<T> {

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

    Set<Edge> callerEdges = this.getCallerEdges(sootMethod);

    if (callerEdges.isEmpty()) {
      return false;
    }

    throw new UnsupportedOperationException("Implement");
  }

  private Set<Edge> getCallerEdges(SootMethod sootMethod) {
    Set<Edge> callerEdges = new HashSet<>();
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

      callerEdges.add(edge);
    }

    return callerEdges;
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
}
