package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.Adapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.programs.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.transformer.JavaRegionClassTransformerTimer;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
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

//        // Taint Analysis (Language dependent)
//        // TODO call Lotrack
//        Map<JavaRegion, Set<String>> regionsToOptions = LotrackProcessor.getRegionsToOptions(JavaPipeline.LOTRACK_DATABASE, program);
//        for(Map.Entry<JavaRegion, Set<String>> regionToOptions : regionsToOptions.entrySet()) {
//            if(regionToOptions.getKey().getRegionMethod().equals("getCommandData")) {
//
//            }
//        }

        return null;
    }

    // TODO how do we know what files we need to instrument from a program?
    public static Set<ClassNode> instrumentRelevantRegions(List<String> programFiles) throws IOException {
        Set<ClassNode> classNodes = new HashSet<>();

        for(String file : programFiles) {
            JavaRegionClassTransformerTimer printer = new JavaRegionClassTransformerTimer(file);
            ClassNode classNode = printer.readClass();
            printer.transform(classNode);

            classNodes.add(classNode);
        }

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
            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram()));
        }

        return configurationsToPerformance;
    }
}
