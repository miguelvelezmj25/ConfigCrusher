package edu.cmu.cs.mvelezce.instrument.region.utils.results;

import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStatementConstraintsPretty;

import java.util.HashSet;
import java.util.Set;

public class RegionConstraintPretty extends ControlFlowStatementConstraintsPretty {

  private final String id;
  private final String startBlock;
  private final Set<String> endBlocks;

  // Dummy constructor for faster xml
  public RegionConstraintPretty() {
    this("", "", "", -1, new HashSet<>(), "", "", new HashSet<>());
  }

  public RegionConstraintPretty(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<String> prettyConstraints,
      String id,
      String startBlock,
      Set<String> endBlocks) {
    super(packageName, className, methodSignature, decisionIndex, prettyConstraints);

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
