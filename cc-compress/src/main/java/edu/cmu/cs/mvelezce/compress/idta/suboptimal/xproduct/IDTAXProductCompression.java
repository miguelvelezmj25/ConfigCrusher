package edu.cmu.cs.mvelezce.compress.idta.suboptimal.xproduct;

import edu.cmu.cs.mvelezce.compress.idta.IDTACompression;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.idta.partition.TotalPartition;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.util.*;

/**
 * Cross product of partitions. Underapproximation of SPLat as there might be options that are read
 * by SPLat, but never userd in control-flow statements
 */
public class IDTAXProductCompression extends IDTACompression {

  private static final String OUTPUT_DIR =
      "../cc-compress/" + Options.DIRECTORY + "/compression/java/programs/idta/suboptimal/xproduct";

  public IDTAXProductCompression(String programName) {
    this(programName, new ArrayList<>(), new HashSet<>());
  }

  IDTAXProductCompression(
      String programName, List<String> options, Collection<Partitioning> allPartitions) {
    super(programName, options, allPartitions);
  }

  @Override
  public Set<Set<String>> analyze() {
    Partitioning xProductPartition = new TotalPartition();

    for (Partitioning partitioning : this.getAllPartitions()) {
      xProductPartition = xProductPartition.merge(partitioning);
    }

    Set<Set<String>> configs = new HashSet<>();

    for (Partition partition : xProductPartition.getPartitions()) {
      Set<String> config = ConstraintUtils.toConfig(partition.getFeatureExpr(), this.getOptions());
      configs.add(config);
    }

    return configs;
  }

  @Override
  public String outputDir() {
    return OUTPUT_DIR + "/" + this.getProgramName();
  }
}
