package edu.cmu.cs.mvelezce.evaluation.approaches.family.model;

import edu.cmu.cs.mvelezce.evaluation.approaches.ApproachPerformanceModel;
import edu.cmu.cs.mvelezce.evaluation.approaches.ApproachPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.evaluation.approaches.family.featuremodel.FeatureModel;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FamilyModel extends ApproachPerformanceModel {

    private FeatureModel fm;

    public FamilyModel(Double basetime, Map<Set<String>, Double> model, FeatureModel fm) {
        this.setBaseTimeHumanReadable(basetime);
        model.remove(FamilyModelBuilder.BASE);

        Region progRegion= new Region.Builder("program").build();
        Map<Region, Map<Set<String>, Double>> regionMap = new HashMap<>();
        regionMap.put(progRegion, model);

        this.setRegionsToPerformanceTablesHumanReadable(regionMap);

        this.fm = fm;
    }

    @Override
    public double evaluate(Set<String> configuration) {
        double performance = this.getBaseTimeHumanReadable();

        if(!this.fm.isValidProduct(configuration)) {
            return performance;
        }

        for(Map.Entry<Region, Map<Set<String>, Double>> entry : this.getRegionsToPerformanceTablesHumanReadable().entrySet()) {
            for(Map.Entry<Set<String>, Double> optionsToPerformance : entry.getValue().entrySet()) {
                Set<String> options = optionsToPerformance.getKey();

                if(configuration.containsAll(options)) {
                    performance += optionsToPerformance.getValue();
                }
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        String perfString = decimalFormat.format(performance);
        performance = Double.valueOf(perfString);

        return performance;
    }

}
