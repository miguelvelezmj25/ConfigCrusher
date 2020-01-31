package edu.cmu.cs.mvelezce.approaches.sampling;

import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;

import java.util.List;
import java.util.Set;

public interface SamplingApproach {

  String getLinearModelType();

  Set<Partition> getConfigsAsPartitions(List<String> options);

  Set<Partition> getLinearModelPartitions(List<String> options);

  Set<Set<String>> getConfigs(List<String> options);

  String getName();
}
