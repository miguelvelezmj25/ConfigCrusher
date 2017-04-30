package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.analysis.Region;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/10/17.
 */
public class PerformanceEntry {
    private static long baseTime = -1;

    private Set<String> configuration;
    private Map<Region, Region> regions; // TODO make set
    private Region program;

    public PerformanceEntry(Set<String> configuration, Set<Region> regions, Region program) {
        this.configuration = configuration;
        this.regions = new HashMap<>();

        for(Region region : regions) {
            if(region.getStartTime() > region.getEndTime()) {
                throw new RuntimeException("A region has a negative execution time. This might be caused by incorrect instrumentation");
            }

            Region clonedRegion = region.clone();
            this.regions.put(clonedRegion, clonedRegion);
        }

        this.program = program.clone();
    }

    public static void reset() {
        PerformanceEntry.baseTime = -1;
    }

    public Region getRegion(Region region) {
        return this.regions.get(region);
    }

    public Set<String> getConfiguration() { return this.configuration; }

    public Set<Region> getRegions() { return this.regions.keySet(); }

    public Region getProgram() { return this.program; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PerformanceEntry that = (PerformanceEntry) o;

        if (!configuration.equals(that.configuration)) return false;
        return regions.equals(that.regions);
    }

    @Override
    public int hashCode() {
        int result = configuration.hashCode();
        result = 31 * result + regions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PerformanceEntry{" +
                "configuration=" + configuration +
                ", regions=" + regions +
                '}';
    }
}
