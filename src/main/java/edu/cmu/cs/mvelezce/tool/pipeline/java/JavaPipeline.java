package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;

import java.util.Map;
import java.util.Set;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipeline {

    public static final String LOTRACK_DATABASE = "lotrack";
    public static final String PLAYYPUS_PROGRAM = "platypus";
    public static final String LANGUAGETOOL_PROGRAM = "Languagetool";

    public static PerformanceModel buildPerformanceModel(String program) throws NoSuchFieldException {
        // Reset
        Regions.reset();
        PerformanceEntry.reset();

        // Taint Analysis (Language dependent)
        // TODO call Lotrack
        Map<JavaRegion, Set<String>> regionsToOptions = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
        for(Map.Entry<JavaRegion, Set<String>> regionToOptions : regionsToOptions.entrySet()) {
            if(regionToOptions.getKey().getRegionMethod().equals("getCommandData")) {

            }
        }

        // Configuration compression (Language independent)
//        Set<Set<String>> configurationsToExecute = Pipeline.getConfigurationsToExecute(relevantOptions);

        // Instrumentation (Language dependent)

        // Performance Model (Language independent)

//        return Pipeline.createPerformanceModel(measuredPerformance, regionsToOptions);

//        try {
//            Map<Region, Set<String>> regionsToOptions = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, JavaPipeline.PLAYYPUS_PROGRAM);
//        } catch (NoSuchFieldException nsfe) {
//            throw nsfe;
////            throw new NoSuchFieldException("s", nsfe);
//        }


        return null;
    }
}
