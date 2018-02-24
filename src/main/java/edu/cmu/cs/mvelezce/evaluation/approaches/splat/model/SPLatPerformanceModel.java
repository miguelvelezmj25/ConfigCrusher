package edu.cmu.cs.mvelezce.evaluation.approaches.splat.model;

import edu.cmu.cs.mvelezce.evaluation.approaches.ApproachPerformanceModel;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SPLatPerformanceModel extends ApproachPerformanceModel {

    public SPLatPerformanceModel(double baseTime, Map<Region, Map<Set<String>, Double>> regionsToPerformanceHumanReadable) {
        this.setBaseTimeHumanReadable(baseTime);
        this.setRegionsToPerformanceTablesHumanReadable(regionsToPerformanceHumanReadable);

        this.setRegionsToPerformanceTables(SPLatPerformanceModel.toNanosecondPerformance(this.getRegionsToPerformanceTablesHumanReadable()));
    }
    
}
