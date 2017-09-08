package edu.cmu.cs.mvelezce.tool.analysis.taint.java.serialize;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

import java.util.Set;

public class DecisionAndOptions {

    private JavaRegion region;
    private Set<Set<String>> options;

    private DecisionAndOptions() {
        ;
    }

    public DecisionAndOptions(JavaRegion region, Set<Set<String>> options) {
        this.region = region;
        this.options = options;
    }

    public JavaRegion getRegion() {
        return region;
    }

    public void setRegion(JavaRegion region) {
        this.region = region;
    }

    public Set<Set<String>> getOptions() {
        return options;
    }

    public void setOptions(Set<Set<String>> options) {
        this.options = options;
    }

}
