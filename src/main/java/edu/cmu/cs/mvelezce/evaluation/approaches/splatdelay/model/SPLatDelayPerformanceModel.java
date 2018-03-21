package edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.model;

import edu.cmu.cs.mvelezce.evaluation.approaches.ApproachPerformanceModel;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.util.Map;
import java.util.Set;

public class SPLatDelayPerformanceModel extends ApproachPerformanceModel {

    public SPLatDelayPerformanceModel(double baseTime, Map<Region, Map<Set<String>, Double>> regionsToPerformanceHumanReadable) {
        this.setBaseTimeHumanReadable(baseTime);
        this.setRegionsToPerformanceTablesHumanReadable(regionsToPerformanceHumanReadable);

        this.setRegionsToPerformanceTables(SPLatDelayPerformanceModel.toNanosecondPerformance(this.getRegionsToPerformanceTablesHumanReadable()));
    }
    
}
