package edu.cmu.cs.mvelezce.instrument.region.utils.results;

import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStmtPartitioningPretty;

import java.util.HashSet;
import java.util.Set;

public class RegionPartitionPretty extends ControlFlowStmtPartitioningPretty {

  private final String id;
  private final String startBlock;
  private final Set<String> endBlocks;

  // Dummy constructor for faster xml
  private RegionPartitionPretty() {
    this("", "", "", -1, new HashSet<>(), "", "", new HashSet<>());
  }

  public RegionPartitionPretty(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<String> prettyPartitions,
      String id,
      String startBlock,
      Set<String> endBlocks) {
    super(packageName, className, methodSignature, decisionIndex, prettyPartitions);

    this.id = id;
    this.startBlock = startBlock;
    this.endBlocks = endBlocks;
  }

  public String getId() {
    return id;
  }

  public String getStartBlock() {
    return startBlock;
  }

  public Set<String> getEndBlocks() {
    return endBlocks;
  }
}
