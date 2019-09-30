package edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.email;

import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;

import java.util.Set;

public class EmailFM implements FeatureModel {

    @Override
    public boolean isValidProduct(Set<String> conf) {
        if(((!conf.contains("ENCRYPT") || conf.contains("DECRYPT"))
                && (!conf.contains("DECRYPT") || conf.contains("ENCRYPT")))
                && (!conf.contains("ENCRYPT") || conf.contains("KEYS"))
                && ((!conf.contains("SIGN") || conf.contains("VERIFY"))
                && (!conf.contains("VERIFY") || conf.contains("SIGN")))
                && (!conf.contains("SIGN") || conf.contains("KEYS"))
                && (conf.contains("BASE"))) {
            return true;
        }
        else {
            return false;
        }
    }
}
