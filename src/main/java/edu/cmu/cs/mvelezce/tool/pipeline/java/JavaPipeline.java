package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.ProgramAnalysis;
import edu.cmu.cs.mvelezce.tool.compression.Simple;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Formatter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.Instrumenter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceModelBuilder;
import edu.cmu.cs.mvelezce.tool.pipeline.java.analysis.PerformanceStatistic;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by mvelezce on 4/10/17.
 */
public class JavaPipeline {

    public static final String PM_RES_DIR = Options.DIRECTORY + "/perf_res/java/programs";
    public static final String LOTRACK_DATABASE = "lotrack";
    public static final String LOADTIME_DATABASE = "loadtime";
    public static final String PLAYYPUS_PROGRAM = "platypus";
    public static final String TEST_COLLECTION = "Tests";
    public static final String LANGUAGETOOL_PROGRAM = "Languagetool";

    public static PerformanceModel buildPerformanceModel(String programName, String[] args, String originalSrcDirectory, String originalClassDirectory, String instrumentSrcDirectory, String instrumentClassDirectory, String entryPoint, Map<JavaRegion, Set<String>> partialRegionsToOptions) throws IOException, ParseException, InterruptedException {
        // Format return statements with method calls
        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
        System.out.println("");

        // Configuration compression (Language independent)
        System.out.println("####################### Configurations to execute #######################");
        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(programName, args, relevantOptions);
        JavaPipeline.compressionHelper(partialRegionsToOptions.values(), configurationsToExecute);
        System.out.println("");

        System.out.println("####################### Instrumenting #######################");
        Instrumenter.instrument(args, instrumentSrcDirectory, instrumentClassDirectory, partialRegionsToOptions.keySet());
        System.out.println("");

        System.out.println("####################### Measure performance #######################");
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, instrumentClassDirectory, configurationsToExecute);
        System.out.println("");

        System.out.println("####################### Build performance model #######################");
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
        System.out.println(pm);
        JavaPipeline.savePMPerformance(programName, pm);

        return pm;
    }

    public static PerformanceModel buildPerformanceModelRepeat(String programName, String[] args, String originalSrcDirectory, String originalClassDirectory, String instrumentSrcDirectory, String instrumentClassDirectory, String entryPoint, Map<JavaRegion, Set<String>> partialRegionsToOptions) throws IOException, ParseException, InterruptedException {
        Options.getCommandLine(args);
        // Format return statements with method calls
        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
        System.out.println("");

        // Configuration compression (Language independent)
        System.out.println("####################### Configurations to execute #######################");
        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(programName, args, relevantOptions);
        JavaPipeline.compressionHelper(partialRegionsToOptions.values(), configurationsToExecute);
        System.out.println("");

        System.out.println("####################### Instrumenting #######################");
        Instrumenter.instrument(args, instrumentSrcDirectory, instrumentClassDirectory, partialRegionsToOptions.keySet());
        System.out.println("");

        System.out.println("####################### Measure performance #######################");
        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();

        for(int i = 0; i < Options.getIterations(); i++) {
            executionsPerformance.add(Executor.measureConfigurationPerformance(programName + Executor.UNDERSCORE + i, args, entryPoint, instrumentClassDirectory, configurationsToExecute));
        }

        List<PerformanceStatistic> perfStats = Executor.getExecutionsStats(executionsPerformance);
        Set<PerformanceEntry> measuredPerformance = Executor.averageExecutions(perfStats, executionsPerformance.get(0));

//        Set<PerformanceEntry> measuredPerformance = Executor.repeatMeasureConfigurationPerformance(programName, args, entryPoint, instrumentClassDirectory, configurationsToExecute);
        System.out.println("");

        System.out.println("####################### Build performance model #######################");
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        PerformanceModel pm = PerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
        System.out.println(pm);
        JavaPipeline.savePMPerformance(programName, pm);

        return pm;
    }

    public static PerformanceModel buildPerformanceModel(String programName, String[] args, String srcDirectory, String classDirectory, String entryPoint) throws IOException, ParseException, InterruptedException {
        // Get regions and options
        System.out.println("Region and options");
        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
        System.out.println("");

        // Configuration compression (Language independent)
        System.out.println("Configurations to execute");
        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(programName, args, relevantOptions);
        System.out.println(configurationsToExecute);
        System.out.println("");

        System.out.println("Instrumenting");
        Instrumenter.instrument(args, srcDirectory, classDirectory, partialRegionsToOptions.keySet());
        System.out.println("");

        System.out.println("Measure performance");
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurationsToExecute);
        System.out.println("");

        System.out.println("Build performance model");
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        return PerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
    }

    public static PerformanceModel buildPerformanceModel(String programName, String[] args, String srcDirectory, String classDirectory, String entryPoint, String sdgFile, List<String> features) throws IOException, ParseException, InterruptedException {
        // Get regions and options
        System.out.println("Region and options");
        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, sdgFile, entryPoint, features);
        System.out.println("");

        // Configuration compression (Language independent)
        System.out.println("Configurations to execute");
        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
        Set<Set<String>> configurationsToExecute = Simple.getConfigurationsToExecute(programName, args, relevantOptions);
        System.out.println(configurationsToExecute);
        System.out.println("");

        System.out.println("Instrumenting");
        Instrumenter.instrument(args, srcDirectory, classDirectory, partialRegionsToOptions.keySet());
        System.out.println("");

        System.out.println("Measure performance");
        Set<PerformanceEntry> measuredPerformance = Executor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurationsToExecute);
        System.out.println("");

        System.out.println("Build performance model");
        Map<Region, Set<String>> regionsToOptions = new HashMap<>();

        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
            Region region = Regions.getRegion(entry.getKey().getRegionID());
            regionsToOptions.put(region, entry.getValue());
        }

        return PerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
    }

    public static void compressionHelper(Collection<Set<String>> regionOptions, Set<Set<String>> configurationsToExecute) {
        Set<String> allOptions = new HashSet<>();

        for(Set<String> options : regionOptions) {
            allOptions.addAll(options);
        }

        System.out.println("Number of bf configurations: " + Helper.getConfigurations(allOptions).size());
        System.out.println("Number of configurations to execute: " + configurationsToExecute.size());
        System.out.println(configurationsToExecute);
    }

    public static void savePMPerformance(String programName, PerformanceModel pm) throws IOException {
        File file = new File(JavaPipeline.PM_RES_DIR + "/" + programName + Options.DOT_CSV);

        if(file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("Could not delete " + file);
            }
        }

        String[] args = new String[0];
        Set<Set<String>> measuredConfigurations = Simple.getConfigurationsToExecute(programName, args);
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : measuredConfigurations) {
            options.addAll(configuration);
        }

        Set<Set<String>> configurations = Helper.getConfigurations(options);

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performance");
        result.append("\n");

        for(Set<String> configuration : configurations) {
            if(measuredConfigurations.contains(configuration)) {
                result.append("true");
                result.append(",");
            }
            else {
                result.append("null");
                result.append(",");
            }

            result.append('"');
            result.append(configuration);
            result.append('"');
            result.append(",");
            double perf = pm.evaluate(configuration);
            result.append(perf);
            result.append("\n");
        }

        File directory = new File(JavaPipeline.PM_RES_DIR);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        String outputFile = directory + "/" + programName + Options.DOT_CSV;
        file = new File(outputFile);
        FileWriter writer = new FileWriter(file, true);
        writer.write(result.toString());
        writer.flush();
        writer.close();
    }

}
