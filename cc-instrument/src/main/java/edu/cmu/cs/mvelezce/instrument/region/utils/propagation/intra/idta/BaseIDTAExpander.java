package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra.idta;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.idta.partition.TotalPartition;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class BaseIDTAExpander {

  private static final BaseIDTAExpander INSTANCE = new BaseIDTAExpander();
  private final Set<Partition> globalPartitions = new HashSet<>();

  private BaseIDTAExpander() {}

  public static BaseIDTAExpander getInstance() {
    return INSTANCE;
  }

  public String prettyPrintPartitions(@Nullable Partitioning partitioning, Set<String> options) {
    Set<String> prettyPartitions = new HashSet<>();

    if (partitioning == null) {
      throw new RuntimeException("The partitions cannot be null");
    }

    for (Partition partition : partitioning.getPartitions()) {
      String prettyPartition =
          ConstraintUtils.prettyPrintFeatureExpr(partition.getFeatureExpr(), options);
      prettyPartitions.add(prettyPartition);
    }

    return prettyPartitions.toString();
  }

  public void init(Collection<Partitioning> partitionings) {
    if (!this.globalPartitions.isEmpty()) {
      return;
    }

    for (Partitioning partitioning : partitionings) {
      this.globalPartitions.addAll(partitioning.getPartitions());
    }
  }

  /** ∃ c ∈ GlobalPartitions . c ⟹ newPartition */
  public boolean canMergePartitionings(
      Partitioning expandingPartitioning, @Nullable Partitioning currentPartitioning) {
    if (expandingPartitioning.getPartitions().isEmpty()) {
      throw new RuntimeException("Expanding partitions should never be empty");
    }

    if (currentPartitioning == null) {
      return true;
    }

    if (currentPartitioning.getPartitions().isEmpty()) {
      throw new RuntimeException("How can that data be empty, but not null?");
    }

    // Shortcut
    if (currentPartitioning.equals(expandingPartitioning)) {
      return true;
    }

    TotalPartition newPartitioning = expandingPartitioning.merge(currentPartitioning);

    // Shortcut
    if (newPartitioning.equals(expandingPartitioning)) {
      return true;
    }

    // Logic
    for (Partition newPartition : newPartitioning.getPartitions()) {
      boolean existsGlobalPartition = false;

      for (Partition globalPartition : this.globalPartitions) {
        if (globalPartition.getFeatureExpr().implies(newPartition.getFeatureExpr()).isTautology()) {
          existsGlobalPartition = true;
          break;
        }
      }

      if (!existsGlobalPartition) {
        return false;
      }
    }

    return true;

    //    return this.impliesAll(this.globalPartitions, newPartitions);
  }

  /** ∀ dc ∈ ImpliedPartitions . ∃ gc ∈ ImplyingPartitions . gc ⟹ dc */
  public boolean impliesAll(
      @Nullable Partitioning implyingPartitioning, @Nullable Partitioning impliedPartitioning) {
    if (implyingPartitioning == null) {
      return false;
    }

    if (impliedPartitioning == null) {
      throw new RuntimeException("What is this case?");
    }

    for (Partition impliedPartition : impliedPartitioning.getPartitions()) {
      boolean exists = false;

      for (Partition implyingPartition : implyingPartitioning.getPartitions()) {
        if (implyingPartition
            .getFeatureExpr()
            .implies(impliedPartition.getFeatureExpr())
            .isTautology()) {
          exists = true;
          break;
        }
      }

      if (!exists) {
        return false;
      }
    }

    return true;
  }

  public Partitioning mergeData(
      Partitioning thisPartitioning, @Nullable Partitioning thatPartitioning) {
    if (thatPartitioning == null) {
      return thisPartitioning;
    }

    return thisPartitioning.merge(thatPartitioning);
  }

  public Set<Partition> getGlobalPartitions() {
    return globalPartitions;
  }
}
