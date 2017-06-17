package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.ProgramAnalysis;
import edu.cmu.cs.mvelezce.tool.compression.Simple;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
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
    public static final String LOADTIME_DATABASE = "loadtime";
    public static final String PLAYYPUS_PROGRAM = "platypus";
    public static final String TEST_COLLECTION = "Tests";
    public static final String LANGUAGETOOL_PROGRAM = "Languagetool";

    public static void buildPerformanceModel(String programName, String[] args) throws IOException, ParseException {
        // Get regions and options
        System.out.println("Region and options");
        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyse(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
        System.out.println("");

        // Configuration compression (Language independent)
        System.out.println("Configurations to execute");
        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(programName, args, relevantOptions);
        System.out.println(configurationsToExecute);
        System.out.println("");

        System.out.println("Instrumenting");

        System.out.println("");
    }


//    // TODO how do we pass the main class and all files of a program?
//    public static PerformanceModel buildPerformanceModel(String programName, String mainClass, String directory, List<String> programFiles, Map<JavaRegion, Set<String>> relevantRegionsToOptions) throws NoSuchFieldException, IOException, NoSuchMethodException, ClassNotFoundException, ParseException {
//        // ProgramAnalysis (Language dependent)
//        // TODO we should get this from Lotrack and not pass it ourselves
//        relevantRegionsToOptions = ProgramAnalysis.analyse(programName, mainClass, programFiles, relevantRegionsToOptions);
//
//        // Configuration compression (Language independent)
//        Set<Set<String>> relevantOptions = new HashSet<>(relevantRegionsToOptions.values());
//        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(relevantOptions);
//
//        // Instrumentation (Language dependent)
//        Instrumenter.instrument(mainClass, directory, programFiles, relevantRegionsToOptions.keySet()); // TODO
//        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, mainClass, null, configurationsToExecute);
////        System.out.println("Executed configurations: " + configurationsToExecute.size());
//
//        // Performance Model (Language independent)
//        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
//
//        for(Map.Entry<JavaRegion, Set<String>> entry : relevantRegionsToOptions.entrySet()) {
//            Region region = Regions.getRegion(entry.getKey().getRegionID());
//            regionsToOptions.put(region, entry.getValue());
//        }
//
//        return PerformanceModelBuilder.createPerformanceModel(measuredPerformance, regionsToOptions);
//    }

}
