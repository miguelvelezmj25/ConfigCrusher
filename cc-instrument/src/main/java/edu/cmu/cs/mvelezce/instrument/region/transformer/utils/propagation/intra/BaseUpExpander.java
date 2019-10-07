package edu.cmu.cs.mvelezce.instrument.region.transformer.utils.propagation.intra;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.transformer.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseUpExpander<T> extends BlockRegionAnalyzer<T> {

  public BaseUpExpander(BlockRegionMatcher blockRegionMatcher, Map<JavaRegion, T> regionsToData) {
    super(blockRegionMatcher, regionsToData);
  }

  @Override
  protected void processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    System.err.println("Have to continue expanding regions until a fix point");
    MethodBlock id = graph.getImmediateDominator(block);

    if (id == null || id == graph.getEntryBlock()) {
      return;
    }

    System.out.println(graph.toDotString("test"));

    JavaRegion idRegion = blocksToRegions.get(id);
    T idData = this.getData(idRegion);
    T regionData = this.getData(region);

    if (!this.canPropagateUp(regionData, idData)) {
      return;
    }

    throw new UnsupportedOperationException("Implement");

    //
    //    if (!(blockDecision.containsAll(idDecision) && !blockDecision.equals(idDecision))) {
    ////            this.debugBlockDecisions(methodNode);
    ////                System.out.println("Cannot push up to id in " + methodNode.name + " " +
    // bDecision + " -> " + aDecision);
    //      return updatedBlocks;
    //    }
    //
    //    // Check
    //    if (block.getPredecessors().isEmpty()) {
    //      throw new RuntimeException("The predecessors cannot be empty " + block.getID());
    //    }
    //
    //    for (MethodBlock pred : block.getPredecessors()) {
    //      // A block might jump to itself
    //      if (block == pred) {
    //        continue;
    //      }
    //
    //      JavaRegion predRegion = blocksToRegions.get(pred);
    //      Set<String> predDecision = this.getSingleDecision(predRegion);
    //
    //      if (!(blockDecision.containsAll(predDecision) || blockDecision.equals(predDecision))) {
    //        if (pred.isCatchWithImplicitThrow()) {
    //          continue;
    //        }
    //
    //        this.debugBlockDecisions(methodNode);
    //        throw new RuntimeException(
    //                "Cannot push up decisions from " + block.getID() + " to " + pred.getID());
    //
    //////                    System.out.println("Cannot push up to predecessor in " +
    // methodNode.name + " " + bDecision + " -> " + aDecision);
    ////                    continue;
    //      }
    //
    //      JavaRegion newRegion = new JavaRegion.Builder(blockRegion.getRegionPackage(),
    //              blockRegion.getRegionClass(), blockRegion.getRegionMethod()).build();
    //      int index;
    //
    //      this.debugBlocksAndRegions(methodNode);
    ////            this.debugBlockDecisions(methodNode);
    //
    //      if (predRegion == null) {
    //        index = methodNode.instructions.indexOf(id.getInstructions().get(0));
    //      }
    //      else {
    //        index = predRegion.getStartRegionIndex();
    //        this.getRegionsToData().remove(predRegion);
    //      }
    //
    //      newRegion.setStartRegionIndex(index);
    //      blocksToRegions.put(pred, newRegion);
    //
    //      Set<Set<String>> newOptionSet = new HashSet<>();
    //      newOptionSet.add(blockDecision);
    //      this.getRegionsToData().put(newRegion, newOptionSet);
    //
    //      this.debugBlocksAndRegions(methodNode);
    ////            this.debugBlockDecisions(methodNode);
    //
    //      updatedBlocks.add(0, pred);
    //    }
    //
    //    return updatedBlocks;
  }

  protected abstract boolean canPropagateUp(T regionData, T idData);
}
