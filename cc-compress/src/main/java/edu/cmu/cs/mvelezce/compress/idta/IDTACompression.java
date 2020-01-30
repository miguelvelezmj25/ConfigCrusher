package edu.cmu.cs.mvelezce.compress.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IDTACompression extends BaseCompression {

  private final Collection<Partitioning> allPartitions;

  protected IDTACompression(
      String programName, List<String> options, Collection<Partitioning> allPartitions) {
    super(programName, options);

    this.allPartitions = allPartitions;
  }

  protected Set<Partition> getCoveredPartitions(Set<String> config, Set<Partition> partitions) {
    String configStringPartition = ConstraintUtils.parseAsConstraint(config, this.getOptions());
    FeatureExpr configPartition =
        FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, configStringPartition);

    Set<Partition> coveredPartitions = new HashSet<>();

    for (Partition partition : partitions) {
      if (configPartition.implies(partition.getFeatureExpr()).isTautology()) {
        coveredPartitions.add(partition);
      }
    }

    return coveredPartitions;
  }

  protected Set<Partition> expandAllPartitions() {
    Set<Partition> expandedPartitions = new HashSet<>();

    for (Partitioning partitioning : this.allPartitions) {
      expandedPartitions.addAll(partitioning.getPartitions());
    }

    return expandedPartitions;
  }
}
