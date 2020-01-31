package edu.cmu.cs.mvelezce.pretty.partition;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.pretty.BasePrettyBuilder;

import java.util.Collection;

public abstract class BasePartitionPrettyBuilder extends BasePrettyBuilder<Partition> {

  public BasePartitionPrettyBuilder(
      String programName, Collection<String> options, PerformanceModel<Partition> model) {
    super(programName, options, model);
  }
}
