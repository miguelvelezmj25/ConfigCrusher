package edu.cmu.cs.mvelezce.tool.pipeline.java.analysis;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.*;

/**
 * Created by mvelezce on 7/5/17.
 */
public class PerformanceStatistic {
    private String measured;
    private Set<String> configuration;
    private Map<String, List<Long>> regionIdsToValues;
    private Map<String, Long> regionIdsToMean = new LinkedHashMap<>();
    private Map<String, Long> regionIdsToStd = new LinkedHashMap<>();

    public PerformanceStatistic(String measured, Set<String> configuration, Map<String, List<Long>> regionIdsToValues) {
        if(Boolean.valueOf(measured)) {
            this.measured = "true";
        }
        else {
            this.measured = "null";
        }

        this.configuration = configuration;
        this.regionIdsToValues = regionIdsToValues;
    }

    public PerformanceStatistic(Set<String> configuration, Map<String, List<Long>> regionIdsToValues) {
        this.configuration = configuration;
        this.regionIdsToValues = regionIdsToValues;
    }

    public void calculateMean() {
        for(Map.Entry<String, List<Long>> regionToValues : this.regionIdsToValues.entrySet()) {
            Long[] arrayLong = regionToValues.getValue().toArray(new Long[0]);
            double[] array = new double[arrayLong.length];

            for(int i = 0; i < array.length; i++) {
                array[i] = arrayLong[i];
            }

            Mean mean = new Mean();
            this.regionIdsToMean.put(regionToValues.getKey(), (long) mean.evaluate(array));
        }
    }

    public void calculateStd() {
        for(Map.Entry<String, List<Long>> regionToValues : this.regionIdsToValues.entrySet()) {
            Long[] arrayLong = regionToValues.getValue().toArray(new Long[0]);
            double[] array = new double[arrayLong.length];

            for(int i = 0; i < array.length; i++) {
                array[i] = arrayLong[i];
            }

            StandardDeviation std = new StandardDeviation();
            this.regionIdsToStd.put(regionToValues.getKey(), (long) std.evaluate(array));
        }
    }

    public Set<String> getConfiguration() {
        return this.configuration;
    }

    public Map<String, List<Long>> getRegionIdsToValues() {
        return this.regionIdsToValues;
    }

    public Map<String, Long> getRegionIdsToMean() {
        return this.regionIdsToMean;
    }

    public Map<String, Long> getRegionIdsToStd() {
        return this.regionIdsToStd;
    }

    public String getMeasured() {
        return this.measured;
    }

    public void setMeasured(String measured) {
        this.measured = measured;
    }
}
