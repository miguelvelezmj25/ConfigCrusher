package edu.cmu.cs.mvelezce.tool.instrumentation.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public abstract class BaseRegionInstrumenter extends BaseInstrumenter {

    public static final String DIRECTORY = Options.DIRECTORY + "/instrumentation/java/programs";

    private Set<JavaRegion> regions;

    public BaseRegionInstrumenter(String programName, String srcDir, String classDir, Set<JavaRegion> regions) {
        super(programName, srcDir, classDir);
        this.regions = regions;

        File root = new File(BaseRegionInstrumenter.DIRECTORY + "/" + programName);

        if(root.exists()) {
            try {
                FileUtils.forceDelete(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<JavaRegion> getRegions() {
        return regions;
    }
}
