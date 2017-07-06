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
    private Map<String, List<Double>> regionsToValues;
    private Map<String, Double> regionsToMean = new LinkedHashMap<>();
    private Map<String, Double> regionsToStd = new LinkedHashMap<>();

    public PerformanceStatistic(String measured, Set<String> configuration, Map<String, List<Double>> regionsToValues) {
        if(Boolean.valueOf(measured)) {
            this.measured = "true";
        }
        else {
            this.measured = "null";
        }

        this.configuration = configuration;
        this.regionsToValues = regionsToValues;
    }

    public PerformanceStatistic(Set<String> configuration, Map<String, List<Double>> regionsToValues) {
        this.configuration = configuration;
        this.regionsToValues = regionsToValues;
    }

    public void calculateMean() {
        for(Map.Entry<String, List<Double>> regionToValues : this.regionsToValues.entrySet()) {
            Double[] arrayDouble = regionToValues.getValue().toArray(new Double[0]);
            double[] array = new double[arrayDouble.length];

            for(int i = 0; i < array.length; i++) {
                array[i] = arrayDouble[i];
            }

            Mean mean = new Mean();
            this.regionsToMean.put(regionToValues.getKey(), mean.evaluate(array));
        }
    }

    public void calculateStd() {
        for(Map.Entry<String, List<Double>> regionToValues : this.regionsToValues.entrySet()) {
            Double[] arrayDouble = regionToValues.getValue().toArray(new Double[0]);
            double[] array = new double[arrayDouble.length];

            for(int i = 0; i < array.length; i++) {
                array[i] = arrayDouble[i];
            }

            StandardDeviation std = new StandardDeviation();
            this.regionsToStd.put(regionToValues.getKey(), std.evaluate(array));
        }
    }

    public Set<String> getConfiguration() {
        return this.configuration;
    }

    public Map<String, List<Double>> getRegionsToValues() {
        return this.regionsToValues;
    }

    public Map<String, Double> getRegionsToMean() {
        return this.regionsToMean;
    }

    public Map<String, Double> getRegionsToStd() {
        return this.regionsToStd;
    }

    public String getMeasured() {
        return this.measured;
    }

    public void setMeasured(String measured) {
        this.measured = measured;
    }
}
