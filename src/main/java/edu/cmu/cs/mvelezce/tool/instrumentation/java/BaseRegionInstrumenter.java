package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

import java.util.Set;

public abstract class BaseRegionInstrumenter extends BaseInstrumenter {

    public static final String DIRECTORY = Options.DIRECTORY + "/instrumentation/java/programs";

    private Set<JavaRegion> regions;

    public BaseRegionInstrumenter(String srcDir, String classDir, Set<JavaRegion> regions) {
        super(srcDir, classDir);
        this.regions = regions;
    }

    public Set<JavaRegion> getRegions() {
        return regions;
    }
}
