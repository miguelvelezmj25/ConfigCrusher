package edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.elevator;

import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;

import java.util.Set;

public class ElevatorFM implements FeatureModel {

    @Override
    public boolean isValidProduct(Set<String> conf) {
        if(!conf.contains("BASE")) {
            return false;
        }

        if((!conf.contains("OVERLOADED") || conf.contains("WEIGHT"))
                && (!conf.contains("TWOTHIRDSFULL") || conf.contains("WEIGHT"))) {
            return true;
        }

        return false;
    }
}
