package edu.cmu.cs.mvelezce.approaches.sampling;

import de.fosd.typechef.featureexpr.FeatureExpr;

import java.util.List;
import java.util.Set;

public interface SamplingApproach {

  String getLinearModelType();

  Set<FeatureExpr> getConfigsAsConstraints(List<String> options);

  Set<FeatureExpr> getLinearModelConstraints(List<String> options);

  Set<Set<String>> getConfigs(List<String> options);

  String getName();
}
