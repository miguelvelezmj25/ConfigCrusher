package edu.cmu.cs.mvelezce.instrument.region.utils.results;

import edu.cmu.cs.mvelezce.explorer.idta.results.statement.info.ControlFlowStatementConstraintsPretty;

import java.util.HashSet;
import java.util.Set;

public class RegionConstraintPretty extends ControlFlowStatementConstraintsPretty {

  private final String id;

  // Dummy constructor for faster xml
  public RegionConstraintPretty() {
    this("", "", "", -1, new HashSet<>(), "");
  }

  public RegionConstraintPretty(
      String packageName,
      String className,
      String methodSignature,
      int decisionIndex,
      Set<String> prettyConstraints,
      String id) {
    super(packageName, className, methodSignature, decisionIndex, prettyConstraints);

    this.id = id;
  }

  public String getId() {
    return id;
  }
}
