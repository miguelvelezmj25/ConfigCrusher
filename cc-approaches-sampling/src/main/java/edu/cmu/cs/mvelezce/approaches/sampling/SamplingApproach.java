package edu.cmu.cs.mvelezce.approaches.sampling;

import java.util.List;
import java.util.Set;

public interface SamplingApproach {

  Set<Set<String>> getConfigs(List<String> options);
}
