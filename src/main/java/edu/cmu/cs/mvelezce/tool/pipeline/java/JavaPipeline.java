package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.ProgramAnalysis;
import edu.cmu.cs.mvelezce.tool.compression.Simple;
import edu.cmu.cs.mvelezce.tool.execute.java.Executer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModelBuilder;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipeline {

    public static final String LOTRACK_DATABASE = "lotrack";
    public static final String PLAYYPUS_PROGRAM = "platypus";
    public static final String LANGUAGETOOL_PROGRAM = "Languagetool";

    // TODO how do we pass the main class and all files of a program?
    public static PerformanceModel buildPerformanceModel(String programName, String mainClass, List<String> programFiles, Map<JavaRegion, Set<String>> relevantRegionsToOptions) throws NoSuchFieldException, IOException, NoSuchMethodException, ClassNotFoundException, ParseException {
        // Reset
        // TODO we need to execute this once we are not passing the regions in the unit tests
//        Regions.reset();

        // ProgramAnalysis (Language dependent)
        // TODO we should get this from Lotrack and not pass it ourselves
        relevantRegionsToOptions = ProgramAnalysis.analyse(programName, mainClass, programFiles, relevantRegionsToOptions);

        // Configuration compression (Language independent)
        Set<Set<String>> relevantOptions = new HashSet<>(relevantRegionsToOptions.values());
        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(relevantOptions);

        // Instrumentation (Language dependent)
        Instrumenter.instrument(programName, mainClass, programFiles, relevantRegionsToOptions.keySet()); // TODO
        Set<PerformanceEntry> measuredPerformance = Executer.measureConfigurationPerformance(programName, mainClass, null, configurationsToExecute);
//        System.out.println("Executed configurations: " + configurationsToExecute.size());

        // Performance Model (Language independent)
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : relevantRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        return PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
    }

}
