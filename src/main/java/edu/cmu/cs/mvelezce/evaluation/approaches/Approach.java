package edu.cmu.cs.mvelezce.evaluation.approaches;

import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;

import java.io.IOException;
import java.util.Set;

public abstract class Approach {

    public static final String DATA_DIR = "/data";
    public static final String INTERCEPT = "(Intercept)";

    private String programName;

    public Approach(String programName) {
        this.programName = programName;
    }

    public abstract void generateCSVData(Set<PerformanceEntryStatistic> performanceEntries) throws IOException;

    public String getProgramName() {
        return programName;
    }
}
