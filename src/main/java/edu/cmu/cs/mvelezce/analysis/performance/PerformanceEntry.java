package edu.cmu.cs.mvelezce.analysis.performance;

import edu.cmu.cs.mvelezce.analysis.taint.Region;
import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/10/17.
 */
public class PerformanceEntry {
    private Set<String> configuration;
    private Map<Region, Region> regions;


    public PerformanceEntry(Set<String> configuration, Set<Region> regions) {
        this.configuration = configuration;
        this.regions = new HashedMap<>();

        for(Region region : regions) {
            this.regions.put(region, region);
        }
    }

    public Region getRegion(Region region) {
        return this.regions.get(region);
    }

    public Set<String> getConfiguration() { return this.configuration; }

    public Set<Region> getRegions() { return this.regions.keySet(); }

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
