package edu.cmu.cs.mvelezce.evaluation.approaches.family.model;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FamilyModel extends PerformanceModel {

    public FamilyModel(Double basetime, Map<Set<String>, Double> model) {
        super();

        this.setBaseTimeHumanReadable(basetime);
        model.remove(FamilyModelBuilder.BASE);

        Region progRegion= new Region("program");
        Map<Region, Map<Set<String>, Double>> regionMap = new HashMap<>();
        regionMap.put(progRegion, model);

        this.setRegionsToPerformanceTablesHumanReadable(regionMap);
    }

}
