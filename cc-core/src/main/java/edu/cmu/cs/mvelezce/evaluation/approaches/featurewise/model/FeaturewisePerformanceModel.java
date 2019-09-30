package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.model;

import edu.cmu.cs.mvelezce.evaluation.approaches.ApproachPerformanceModel;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FeaturewisePerformanceModel extends ApproachPerformanceModel {

    public FeaturewisePerformanceModel(double baseTime, Map<Region, Map<Set<String>, Double>> regionsToPerformanceHumanReadable) {
        this.setBaseTimeHumanReadable(baseTime);
        this.setRegionsToPerformanceTablesHumanReadable(regionsToPerformanceHumanReadable);

        this.setRegionsToPerformanceTables(FeaturewisePerformanceModel.toNanosecondPerformance(this.getRegionsToPerformanceTablesHumanReadable()));
    }

}
