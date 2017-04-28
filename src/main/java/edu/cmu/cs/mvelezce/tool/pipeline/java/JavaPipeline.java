package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Adapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformer;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.pipeline.Pipeline;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

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
    public static PerformanceModel buildPerformanceModel(String mainClass, List<String> programFiles, Map<JavaRegion, Set<String>> relevantRegionsToOptions) throws NoSuchFieldException, IOException, NoSuchMethodException, ClassNotFoundException {
        // Reset
        // TODO we need to execute this once we are not passing the regions in the unit tests
//        Regions.reset();
        PerformanceEntry.reset();

        // ProgramAnalysis (Language dependent)
//        // TODO call Lotrack
//        Map<JavaRegion, Set<String>> regionsToOptions = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
//        for(Map.Entry<JavaRegion, Set<String>> regionToOptions : regionsToOptions.entrySet()) {
//            if(regionToOptions.getKey().getRegionMethod().equals("getCommandData")) {
//
//            }
//        }

        // TODO we should get this from Lotrack and not pass it ourselves
//        Map<JavaRegion, Set<String>> relevantRegionsToOptions = null;

        // Configuration compression (Language independent)
        Set<Set<String>> relevantOptions = new HashSet<>(relevantRegionsToOptions.values());
        Set<Set<String>> configurationsToExecute = Pipeline.getConfigurationsToExecute(relevantOptions);

        // Instrumentation (Language dependent)
        Set<ClassNode> instrumentedClasses = JavaPipeline.instrumentRelevantRegions(mainClass, programFiles);
        Set<PerformanceEntry> measuredPerformance = JavaPipeline.measureConfigurationPerformance(mainClass, instrumentedClasses, configurationsToExecute);
//        System.out.println("Executed configurations: " + configurationsToExecute.size());

        // Performance Model (Language independent)
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : relevantRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        return Pipeline.createPerformanceModel(measuredPerformance, regionsToOptions);
    }

    // TODO how do we know what files we need to instrument from a program?
    public static Set<ClassNode> instrumentRelevantRegions(String mainClass, List<String> programFiles) throws IOException {
        Set<ClassNode> classNodes = new HashSet<>();

        for(String file : programFiles) {
            JavaRegionClassTransformerTimer printer = new JavaRegionClassTransformerTimer(file);
            ClassNode classNode = printer.readClass();
            printer.transform(classNode);

            classNodes.add(classNode);
        }

        JavaRegionClassTransformer.setMainClass(mainClass);

        return classNodes;
    }

    public static Set<PerformanceEntry> measureConfigurationPerformance(String mainClass, Set<ClassNode> instrumentedClasses, Set<Set<String>> configurationsToExecute) throws NoSuchMethodException, ClassNotFoundException {
        Adapter.setInstrumentedClassNodes(instrumentedClasses);
        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for(Set<String> configuration : configurationsToExecute) {
            Regions.resetRegions();

            // TODO factory pattern or switch statement to create the right adapter
            Adapter adapter = new SleepAdapter(mainClass);
            adapter.execute(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }

            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram()));
        }

        return configurationsToPerformance;
    }
}
