package edu.cmu.cs.mvelezce.tool.performancemodel;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface PerformanceModelBuilder {

    public PerformanceModel createModel(Set<PerformanceEntry2> measuredPerformance, Map<Region, Set<String>> regionsToOptions);

    public PerformanceModel createModel(String[] args, Set<PerformanceEntry2> measuredPerformance, Map<Region, Set<String>> regionsToOptions) throws IOException;

    public PerformanceModel createModel(String[] args) throws IOException;

    public void writeToFile(PerformanceModel performanceModel) throws IOException;

    public PerformanceModel readFromFile(File file) throws IOException;
}
