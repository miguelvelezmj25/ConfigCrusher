package edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel;

import java.util.Set;

public interface FeatureModel {

    String BASE = "BASE";

    boolean isValidProduct(Set<String> conf);

}
