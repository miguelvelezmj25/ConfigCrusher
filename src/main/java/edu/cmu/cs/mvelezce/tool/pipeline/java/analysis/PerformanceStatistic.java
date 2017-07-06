package edu.cmu.cs.mvelezce.tool.pipeline.java.analysis;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 7/5/17.
 */
public class PerformanceStatistic {
    private String measured;
    private Set<String> configuration;
    private Map<Region, List<Long>> regionsToValues;
    private Map<Region, Long> regionsToMean = new LinkedHashMap<>();
    private Map<Region, Long> regionsToStd = new LinkedHashMap<>();

    public PerformanceStatistic(String measured, Set<String> configuration, Map<Region, List<Long>> regionsToValues) {
        if(Boolean.valueOf(measured)) {
            this.measured = "true";
        }
        else {
            this.measured = "null";
        }

        this.configuration = configuration;
        this.regionsToValues = regionsToValues;
    }

    public PerformanceStatistic(Set<String> configuration, Map<Region, List<Long>> regionsToValues) {
        this.configuration = configuration;
        this.regionsToValues = regionsToValues;
    }

    public void calculateMean() {
        for(Map.Entry<Region, List<Long>> regionToValues : this.regionsToValues.entrySet()) {
            Long[] arrayLong = regionToValues.getValue().toArray(new Long[0]);
            double[] array = new double[arrayLong.length];

            for(int i = 0; i < array.length; i++) {
                array[i] = arrayLong[i];
            }

            Mean mean = new Mean();
            this.regionsToMean.put(regionToValues.getKey(), (long) mean.evaluate(array));
        }
    }

    public void calculateStd() {
        for(Map.Entry<Region, List<Long>> regionToValues : this.regionsToValues.entrySet()) {
            Long[] arrayLong = regionToValues.getValue().toArray(new Long[0]);
            double[] array = new double[arrayLong.length];

            for(int i = 0; i < array.length; i++) {
                array[i] = arrayLong[i];
            }

            StandardDeviation std = new StandardDeviation();
            this.regionsToStd.put(regionToValues.getKey(), (long) std.evaluate(array));
        }
    }

    public Set<String> getConfiguration() {
        return this.configuration;
    }

    public Map<Region, List<Long>> getRegionsToValues() {
        return this.regionsToValues;
    }

    public Map<Region, Long> getRegionsToMean() {
        return this.regionsToMean;
    }

    public Map<Region, Long> getRegionsToStd() {
        return this.regionsToStd;
    }

    public String getMeasured() {
        return this.measured;
    }

    public void setMeasured(String measured) {
        this.measured = measured;
    }
}
