package edu.cmu.cs.mvelezce.analysis.mapper;

import edu.cmu.cs.mvelezce.analysis.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.analysis.taint.Processor;
import edu.cmu.cs.mvelezce.analysis.taint.Region;

import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipeline {

    public static final String PLAYYPUS = "platypus";

    public static PerformanceModel getPerformanceModel() {
        Map<Region, Set<String>> regionsToOptions = Processor.getRegionsToOptions(JavaPipeline.PLAYYPUS);


        return null;
    }
}
