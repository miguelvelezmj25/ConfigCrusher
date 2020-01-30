package edu.cmu.cs.mvelezce.compress.idta.utils.simplify;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;

import java.util.HashSet;
import java.util.Set;

public final class ImpliedPartitionsRemover {

  public static void removeImpliedPartitions(Set<Partition> partitions) {
    Set<Partition> impliedPartitions = new HashSet<>();

    for (Partition currentPartition : partitions) {
      if (impliedPartitions.contains(currentPartition)) {
        continue;
      }

      for (Partition partition : partitions) {
        if (currentPartition.equals(partition)) {
          continue;
        }

        if (!currentPartition.getFeatureExpr().implies(partition.getFeatureExpr()).isTautology()) {
          continue;
        }

        impliedPartitions.add(partition);
      }
    }

    partitions.removeAll(impliedPartitions);
  }
}
