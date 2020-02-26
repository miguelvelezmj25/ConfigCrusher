package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.up;

import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.BaseIntraExpander;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseUpIntraExpander<T> extends BaseIntraExpander<T> {

  public BaseUpIntraExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    if (block.getSuccessors().isEmpty() && block.getPredecessors().isEmpty()) {
      // Block is not connected to the rest of the graph
      return new HashSet<>();
    }

    MethodBlock id = graph.getImmediateDominator(block);

    if (id == null || id == graph.getEntryBlock()) {
      return new HashSet<>();
    }

    //    System.out.println(graph.toDotString("test"));

    JavaRegion idRegion = blocksToRegions.get(id);
    T idData = this.getData(idRegion);
    T regionData = this.getData(region);

    if (regionData == null) {
      throw new RuntimeException("The data at this region cannot be null");
    }

    // If the data are the same, we do not want to process anything
    if (regionData.equals(idData) || !this.canExpandDataUp(regionData, idData)) {
      return new HashSet<>();
    }

    return this.expandDataUp(region, block, regionData, blocksToRegions);
  }

  private Set<MethodBlock> expandDataUp(
      JavaRegion region,
      MethodBlock block,
      T regionData,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Set<MethodBlock> predBlocks = block.getPredecessors();

    if (block.getPredecessors().isEmpty()) {
      throw new RuntimeException("The predecessors cannot be empty " + block.getID());
    }

    Set<MethodBlock> updatedBlocks = new HashSet<>();

    for (MethodBlock predBlock : predBlocks) {
      // A block might jump to itself
      if (block == predBlock) {
        continue;
      }

      JavaRegion predRegion = blocksToRegions.get(predBlock);
      T predData = this.getData(predRegion);

      if (regionData.equals(predData)) {
        continue;
      }

      if (!this.canExpandDataUp(regionData, predData)) {
        System.err.println(
            "Might not be able to merge all partitions (e.g., up = {A}, {!A}; down = {A}, {B}; can only merge up {A}, not {B})");
        //        if (predBlock.isCatchWithImplicitThrow()) {
        //          throw new RuntimeException("Why do we check this?");
        //        }

        throw new RuntimeException(
            "Cannot merge data from " + block.getID() + " to " + predBlock.getID());
      }

      if (predRegion == null) {
        predRegion =
            new JavaRegion.Builder(
                    region.getRegionPackage(),
                    region.getRegionClass(),
                    region.getRegionMethodSignature())
                .build();
        blocksToRegions.put(predBlock, predRegion);
      }

      T newData = this.mergeData(regionData, predData);

      if (newData.equals(predData)) {
        continue;
      }

      this.addRegionToData(predRegion, newData);
      updatedBlocks.add(predBlock);
    }

    return updatedBlocks;
  }

  protected abstract T mergeData(T expandingData, @Nullable T upData);

  protected abstract boolean canExpandDataUp(T expandingData, @Nullable T upData);
}
