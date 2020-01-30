package edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.basic;

import edu.cmu.cs.mvelezce.compress.idta.IDTACompression;
import edu.cmu.cs.mvelezce.compress.idta.utils.simplify.ImpliedPartitionsRemover;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.*;

/** Sub-optimal algorithm */
public class IDTASuboptimalGreedyBasicCompression extends IDTACompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/"
          + Options.DIRECTORY
          + "/compression/java/programs/idta/suboptimal/greedy/basic";

  public IDTASuboptimalGreedyBasicCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  IDTASuboptimalGreedyBasicCompression(
      String programName, List<String> options, Collection<Partitioning> allPartitions) {
    super(programName, options, allPartitions);
  }

  @Override
  public Set<Set<String>> analyze() {
    Set<Set<String>> configs = new HashSet<>();
    Set<Partition> partitions = this.expandAllPartitions();
    ImpliedPartitionsRemover.removeImpliedPartitions(partitions);

    Set<Partition> coveredPartitions = new HashSet<>();

    for (Partition partition : partitions) {
      if (coveredPartitions.contains(partition)) {
        continue;
      }

      Set<String> config = ConstraintUtils.toConfig(partition.getFeatureExpr(), this.getOptions());
      configs.add(config);

      coveredPartitions.addAll(this.getCoveredPartitions(config, partitions));
    }

    return configs;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}
