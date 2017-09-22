package edu.cmu.cs.mvelezce.tool.performance;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.*;

/**
 * Created by mvelezce on 7/5/17.
 */
public class PerformanceStatistic {
    private boolean measured;
    private Set<String> configuration;
    private Map<Region, Long> regionsToRawMean = new LinkedHashMap<>();
    private Map<Region, Double> regionsToRawStd = new LinkedHashMap<>();
    private Map<Region, Double> regionsToRawMeanHumanReadReadable = new LinkedHashMap<>();
    private Map<Region, Double> regionsToRawStdHumanReadReadable = new LinkedHashMap<>();
    private Map<Region, Long> regionsToProcessedMean = new LinkedHashMap<>();
    private Map<Region, Double> regionsToProcessedStd = new LinkedHashMap<>();
    private Map<Region, Double> regionsToProcessedMeanHumanReadable = new LinkedHashMap<>();
    private Map<Region, Double> regionsToProcessedStdHumanReadable = new LinkedHashMap<>();
    private double rawMean = 0.0;
    private double rawStd = 0.0;
    private double rawMeanHumanReadable = 0.0;
    private double rawStdHumanReadable = 0.0;
    private double processedMean = 0.0;
    private double processedStd = 0.0;
    private double processedMeanHumanReadable = 0.0;
    private double processedStdHumanReadable = 0.0;

    private PerformanceStatistic() { ; }

    public PerformanceStatistic(boolean measured, List<DefaultPerformanceEntry> performanceEntries) {
        this.measured = measured;
        this.configuration = performanceEntries.iterator().next().getConfiguration();

        this.calculateMean(performanceEntries);
        this.calculateStd(performanceEntries);

        this.regionsToRawMeanHumanReadReadable = DefaultPerformanceEntry.toHumanReadable(this.regionsToRawMean);
        this.regionsToProcessedMeanHumanReadable =  DefaultPerformanceEntry.toHumanReadable(this.regionsToProcessedMean);

//        this.regionsToRawStdHumanReadReadable = DefaultPerformanceEntry.toHumanReadable(this.regionsToRawStd);
//        this.regionsToProcessedStdHumanReadable = DefaultPerformanceEntry.toHumanReadable(this.regionsToProcessedStd);
    }

    public void calculateMean(List<DefaultPerformanceEntry> performanceEntries) {
        // Raw data
        for(Region region : performanceEntries.iterator().next().getRegionsToRawPerformance().keySet()){
            this.regionsToRawMean.put(region, 0L);
        }

        for(DefaultPerformanceEntry performanceEntry : performanceEntries) {
            Iterator<Map.Entry<Region, Long>> performanceEntryIterator = performanceEntry.getRegionsToRawPerformance().entrySet().iterator();
            Iterator<Map.Entry<Region, Long>> regionsToMeanIterator = this.regionsToRawMean.entrySet().iterator();

            while(performanceEntryIterator.hasNext() && regionsToMeanIterator.hasNext()) {
                Map.Entry<Region, Long> entry1 = performanceEntryIterator.next();
                Map.Entry<Region, Long> entry2 = regionsToMeanIterator.next();

                if(!entry1.getKey().getRegionID().equals(entry2.getKey().getRegionID())) {
                    throw new RuntimeException("The ids do not match");
                }

                long sum = this.regionsToRawMean.get(entry2.getKey());
                sum += entry1.getValue();
                this.regionsToRawMean.put(entry2.getKey(), sum);
            }

            if(performanceEntryIterator.hasNext() && !regionsToMeanIterator.hasNext()) {
                throw new RuntimeException("There were items left in the regions to mean iterator");
            }

            if(!performanceEntryIterator.hasNext() && regionsToMeanIterator.hasNext()) {
                throw new RuntimeException("There were items left in the performance entry iterator");
            }
        }

        for(Map.Entry<Region, Long> regionToMean : this.regionsToRawMean.entrySet()) {
            long sum = regionToMean.getValue();
            sum /= performanceEntries.size();
            this.regionsToRawMean.put(regionToMean.getKey(), sum);
        }

        // Processed data
        for(Region region : performanceEntries.iterator().next().getRegionsToProcessedPerformance().keySet()){
            this.regionsToProcessedMean.put(region, 0L);
        }

        for(DefaultPerformanceEntry performanceEntry : performanceEntries) {
            Iterator<Map.Entry<Region, Long>> performanceEntryIterator = performanceEntry.getRegionsToProcessedPerformance().entrySet().iterator();
            Iterator<Map.Entry<Region, Long>> regionsToMeanIterator = this.regionsToProcessedMean.entrySet().iterator();

            while(performanceEntryIterator.hasNext() && regionsToMeanIterator.hasNext()) {
                Map.Entry<Region, Long> entry1 = performanceEntryIterator.next();
                Map.Entry<Region, Long> entry2 = regionsToMeanIterator.next();

                if(!entry1.getKey().getRegionID().equals(entry2.getKey().getRegionID())) {
                    throw new RuntimeException("The ids do not match");
                }

                long sum = this.regionsToProcessedMean.get(entry2.getKey());
                sum += entry1.getValue();
                this.regionsToProcessedMean.put(entry2.getKey(), sum);
            }

            if(performanceEntryIterator.hasNext() && !regionsToMeanIterator.hasNext()) {
                throw new RuntimeException("There were items left in the regions to mean iterator");
            }

            if(!performanceEntryIterator.hasNext() && regionsToMeanIterator.hasNext()) {
                throw new RuntimeException("There were items left in the performance entry iterator");
            }
        }

        for(Map.Entry<Region, Long> regionToMean : this.regionsToProcessedMean.entrySet()) {
            long sum = regionToMean.getValue();
            sum /= performanceEntries.size();
            this.regionsToProcessedMean.put(regionToMean.getKey(), sum);
        }
    }

    public void calculateStd(List<DefaultPerformanceEntry> performanceEntries) {
//        // Raw data
//        for(Region region : performanceEntries.iterator().next().getRegionsToRawPerformance().keySet()){
//            this.regionsToRawStd.put(region, 0.0);
//        }
//
//        for(DefaultPerformanceEntry performanceEntry : performanceEntries) {
//            Iterator<Map.Entry<Region, Long>> performanceEntryIterator = performanceEntry.getRegionsToRawPerformance().entrySet().iterator();
//            Iterator<Map.Entry<Region, Double>> regionsToStdIterator = this.regionsToRawStd.entrySet().iterator();
//
//            List<Double> values = new ArrayList<>();
//
//            while(performanceEntryIterator.hasNext() && regionsToStdIterator.hasNext()) {
//                Map.Entry<Region, Long> entry1 = performanceEntryIterator.next();
//                Region region2 = regionsToStdIterator.next().getKey();
//
//                if(!entry1.getKey().getRegionID().equals(region2.getRegionID())) {
//                    throw new RuntimeException("The ids do not match");
//                }
//
//                values.add(entry1.getValue().doubleValue());
//            }
//
//            if(performanceEntryIterator.hasNext() && !regionsToStdIterator.hasNext()) {
//                throw new RuntimeException("There were items left in the regions to mean iterator");
//            }
//
//            if(!performanceEntryIterator.hasNext() && regionsToStdIterator.hasNext()) {
//                throw new RuntimeException("There were items left in the performance entry iterator");
//            }
//
//
//
//        }
//
//        double mean = this.getRegionsToRawMean().get(performanceEnt);
//        double[] values;
//
//        StandardDeviation std = new StandardDeviation();
//        std.evaluate(values, mean);
//
//        for(Map.Entry<Region, Long> regionToMean : this.regionsToRawMean.entrySet()) {
//            long sum = regionToMean.getValue();
//            sum /= performanceEntries.size();
//            this.regionsToRawMean.put(regionToMean.getKey(), sum);
//        }
//
//
//
//        // Processed data
//        for(Region region : performanceEntries.iterator().next().getRegionsToProcessedPerformance().keySet()){
//            this.regionsToProcessedMean.put(region, 0L);
//        }
//
//        for(DefaultPerformanceEntry performanceEntry : performanceEntries) {
//            Iterator<Map.Entry<Region, Long>> performanceEntryIterator = performanceEntry.getRegionsToProcessedPerformance().entrySet().iterator();
//            Iterator<Map.Entry<Region, Long>> regionsToMeanIterator = this.regionsToProcessedMean.entrySet().iterator();
//
//            while(performanceEntryIterator.hasNext() && regionsToMeanIterator.hasNext()) {
//                Map.Entry<Region, Long> entry1 = performanceEntryIterator.next();
//                Map.Entry<Region, Long> entry2 = regionsToMeanIterator.next();
//
//                if(!entry1.getKey().getRegionID().equals(entry2.getKey().getRegionID())) {
//                    throw new RuntimeException("The ids do not match");
//                }
//
//                long sum = this.regionsToProcessedMean.get(entry2.getKey());
//                sum += entry1.getValue();
//                this.regionsToProcessedMean.put(entry2.getKey(), sum);
//            }
//
//            if(performanceEntryIterator.hasNext() && !regionsToMeanIterator.hasNext()) {
//                throw new RuntimeException("There were items left in the regions to mean iterator");
//            }
//
//            if(!performanceEntryIterator.hasNext() && regionsToMeanIterator.hasNext()) {
//                throw new RuntimeException("There were items left in the performance entry iterator");
//            }
//        }
//
//        for(Map.Entry<Region, Long> regionToMean : this.regionsToProcessedMean.entrySet()) {
//            long sum = regionToMean.getValue();
//            sum /= performanceEntries.size();
//            this.regionsToProcessedMean.put(regionToMean.getKey(), sum);
//        }
//
//
////        for(Map.Entry<Region, List<Long>> regionToValues : this.regionsToValues.entrySet()) {
////            Long[] arrayLong = regionToValues.getValue().toArray(new Long[0]);
////            double[] array = new double[arrayLong.length];
////
////            for(int i = 0; i < array.length; i++) {
////                array[i] = arrayLong[i];
////            }
////
////            this.regionsToStd.put(regionToValues.getKey(), (long) std.evaluate(array));
////        }
    }

    public boolean isMeasured() {
        return measured;
    }

    public Set<String> getConfiguration() {
        return configuration;
    }

    public Map<Region, Long> getRegionsToRawMean() {
        return regionsToRawMean;
    }

    public Map<Region, Double> getRegionsToRawStd() {
        return regionsToRawStd;
    }

    public Map<Region, Double> getRegionsToRawMeanHumanReadReadable() {
        return regionsToRawMeanHumanReadReadable;
    }

    public Map<Region, Double> getRegionsToRawStdHumanReadReadable() {
        return regionsToRawStdHumanReadReadable;
    }

    public Map<Region, Long> getRegionsToProcessedMean() {
        return regionsToProcessedMean;
    }

    public Map<Region, Double> getRegionsToProcessedStd() {
        return regionsToProcessedStd;
    }

    public Map<Region, Double> getRegionsToProcessedMeanHumanReadable() {
        return regionsToProcessedMeanHumanReadable;
    }

    public Map<Region, Double> getRegionsToProcessedStdHumanReadable() {
        return regionsToProcessedStdHumanReadable;
    }

    public double getRawMean() {
        return rawMean;
    }

    public double getRawStd() {
        return rawStd;
    }

    public double getRawMeanHumanReadable() {
        return rawMeanHumanReadable;
    }

    public double getRawStdHumanReadable() {
        return rawStdHumanReadable;
    }

    public double getProcessedMean() {
        return processedMean;
    }

    public double getProcessedStd() {
        return processedStd;
    }

    public double getProcessedMeanHumanReadable() {
        return processedMeanHumanReadable;
    }

    public double getProcessedStdHumanReadable() {
        return processedStdHumanReadable;
    }
}
