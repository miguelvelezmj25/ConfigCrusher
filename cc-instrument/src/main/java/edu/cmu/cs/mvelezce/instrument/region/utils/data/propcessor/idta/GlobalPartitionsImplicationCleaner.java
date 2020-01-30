package edu.cmu.cs.mvelezce.instrument.region.utils.data.propcessor.idta;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;
import edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta.BaseIDTAExpander;
import edu.cmu.cs.mvelezce.instrumenter.graph.MethodGraph;
import edu.cmu.cs.mvelezce.instrumenter.graph.block.MethodBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class will update the partitions of a region. It will remove implied partitions by other
 * partitions and it will add global partitions that implied the partitions in the region. The
 * latter process potentially (which we have seen in some programs) adds missing interactions not
 * captured by the unsoundness of the idta.
 */
public final class GlobalPartitionsImplicationCleaner extends BlockRegionAnalyzer<Partitioning> {

  private final BaseIDTAExpander baseIDTAExpander;
  private final Set<Partition> cleanedGlobalPartitions;

  public GlobalPartitionsImplicationCleaner(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, Partitioning> regionsToData,
      BaseIDTAExpander baseIDTAExpander) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);

    this.baseIDTAExpander = baseIDTAExpander;
    this.cleanedGlobalPartitions = new HashSet<>(this.baseIDTAExpander.getGlobalPartitions());

    System.err.println("Should we simplify the global set of partitions?");
    System.err.println(
        "Should we simplify the partitions of each region? For debugging? For indicating the performance of the region?");
    //    this.removeImpliedPartitions(this.cleanedGlobalPartitions);
  }

  @Override
  protected Set<MethodBlock> processBlock(
      MethodBlock block,
      JavaRegion region,
      MethodGraph graph,
      LinkedHashMap<MethodBlock, JavaRegion> blocksToRegions) {
    Partitioning data = this.getData(region);

    if (data == null) {
      throw new RuntimeException(
          "We did not expect the partitions to be null for region " + region.getId());
    }

    this.clean(data);

    return new HashSet<>();
  }

  @Override
  protected String getAnalysisName() {
    return "cleanImpliedPartitions";
  }

  @Override
  protected String getPrettyData(@Nullable JavaRegion region) {
    Partitioning partitioning = this.getData(region);
    Set<String> options = this.getOptions();

    return this.baseIDTAExpander.prettyPrintPartitions(partitioning, options);
  }

  private void clean(Partitioning partitioning) {
    this.removeImpliedPartitions(partitioning);
    //    this.addImplyingPartitions(partitions);
    //    this.removeImpliedPartitions(partitions);
  }

  private void removeImpliedPartitions(Partitioning partitioning) {
    Set<Partition> impliedPartitions = new HashSet<>();

    for (Partition currentPartition : partitioning.getPartitions()) {
      if (impliedPartitions.contains(currentPartition)) {
        continue;
      }

      for (Partition partition : partitioning.getPartitions()) {
        if (impliedPartitions.contains(partition)) {
          continue;
        }

        if (currentPartition.getFeatureExpr().equivalentTo(partition.getFeatureExpr())) {
          continue;
        }

        if (!currentPartition.getFeatureExpr().implies(partition.getFeatureExpr()).isTautology()) {
          continue;
        }

        impliedPartitions.add(partition);
      }
    }

    partitioning.getPartitions().removeAll(impliedPartitions);
  }

  //  private void addImplyingPartitions(Set<FeatureExpr> partitions) {
  //    Map<FeatureExpr, Set<FeatureExpr>> partitionsToImplyingPartitions =
  //        this.getEmptyPartitionsToImplyingPartitions(partitions);
  //    this.calculateImplyingPartitions(partitionsToImplyingPartitions);
  //
  //    Map<FeatureExpr, Set<FeatureExpr>> partitionsToReplace =
  //        this.getPartitionsToReplace(partitionsToImplyingPartitions);
  //    this.replacePartitions(partitions, partitionsToReplace);
  //  }
  //
  //  private void replacePartitions(
  //      Set<FeatureExpr> partitions, Map<FeatureExpr, Set<FeatureExpr>> partitionsToReplace) {
  //    for (Map.Entry<FeatureExpr, Set<FeatureExpr>> entry : partitionsToReplace.entrySet()) {
  //      if (entry.getValue().isEmpty()) {
  //        continue;
  //      }
  //
  //      partitions.remove(entry.getKey());
  //      partitions.addAll(entry.getValue());
  //    }
  //  }
  //
  //  private Map<FeatureExpr, Set<FeatureExpr>> getPartitionsToReplace(
  //      Map<FeatureExpr, Set<FeatureExpr>> partitionsToImplyingPartitions) {
  //    Map<FeatureExpr, Set<FeatureExpr>> partitionsToReplace = new HashMap<>();
  //
  //    for (FeatureExpr partition : partitionsToImplyingPartitions.keySet()) {
  //      partitionsToReplace.put(partition, new HashSet<>());
  //    }
  //
  //    for (Map.Entry<FeatureExpr, Set<FeatureExpr>> entry :
  //        partitionsToImplyingPartitions.entrySet()) {
  //      FeatureExpr partition = entry.getKey();
  //      Set<FeatureExpr> implyingPartitions = entry.getValue();
  //
  //      if (implyingPartitions.isEmpty()) {
  //        continue;
  //      }
  //
  //      if (implyingPartitions.size() == 1) {
  //        partitionsToReplace.get(partition).addAll(implyingPartitions);
  //      } else if (this.areAllMex(implyingPartitions)) {
  //        partitionsToReplace.get(partition).addAll(implyingPartitions);
  //      } else {
  //        Set<FeatureExpr> onePartition = new HashSet<>();
  //        onePartition.add(partition);
  //
  //        System.err.println(
  //            "Partition "
  //                + this.baseIDTAExpander.prettyPrintPartitions(onePartition, this.getOptions())
  //                + " is implied by multiple mutually inclusive global partitions "
  //                + this.baseIDTAExpander.prettyPrintPartitions(
  //                    implyingPartitions, this.getOptions())
  //                + ". Therefore, it is not clear which partitions to add in this region");
  //      }
  //    }
  //
  //    return partitionsToReplace;
  //  }
  //
  //  private boolean areAllMex(Set<FeatureExpr> implyingPartitions) {
  //    for (FeatureExpr partition1 : implyingPartitions) {
  //      for (FeatureExpr partition2 : implyingPartitions) {
  //        if (partition1 == partition2) {
  //          continue;
  //        }
  //
  //        if (!partition1.mex(partition2).isTautology()) {
  //          return false;
  //        }
  //      }
  //    }
  //
  //    return true;
  //  }
  //
  //  private void calculateImplyingPartitions(
  //      Map<FeatureExpr, Set<FeatureExpr>> partitionsToImplyingPartitions) {
  //    for (FeatureExpr globalPartition : this.cleanedGlobalPartitions) {
  //      for (Map.Entry<FeatureExpr, Set<FeatureExpr>> entry :
  //          partitionsToImplyingPartitions.entrySet()) {
  //        FeatureExpr partition = entry.getKey();
  //
  //        if (globalPartition == partition) {
  //          continue;
  //        }
  //
  //        if (!globalPartition.implies(partition).isTautology()) {
  //          continue;
  //        }
  //
  //        partitionsToImplyingPartitions.get(partition).add(globalPartition);
  //      }
  //    }
  //  }
  //
  //  private Map<FeatureExpr, Set<FeatureExpr>> getEmptyPartitionsToImplyingPartitions(
  //      Set<FeatureExpr> partitions) {
  //    Map<FeatureExpr, Set<FeatureExpr>> partitionsToImplyingPartitions = new HashMap<>();
  //
  //    for (FeatureExpr partition : partitions) {
  //      partitionsToImplyingPartitions.put(partition, new HashSet<>());
  //    }
  //
  //    return partitionsToImplyingPartitions;
  //  }
}
