package edu.cmu.cs.mvelezce.tool.pipeline.java;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.performance.model.PerformanceModel;
import edu.cmu.cs.mvelezce.tool.pipeline.java.analysis.PerformanceStatistic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

    public static PerformanceModel buildPerformanceModel(String programName, String[] args, String originalSrcDirectory, String originalClassDirectory, String instrumentSrcDirectory, String instrumentClassDirectory, String entryPoint, Map<JavaRegion, Set<String>> partialRegionsToOptions) throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        // Format return statements with method calls
//        // TODO compile both original and instrumented
////        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
//        System.out.println("");
//
//        // Configuration compression (Language independent)
//        System.out.println("####################### Configurations to execute #######################");
//        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
//        Compression compression = new SimpleCompression(programName, relevantOptions);
//        Set<Set<String>> configurationsToExecute = compression.compressConfigurations(args);
//        JavaPipeline.compressionHelper(partialRegionsToOptions.values(), configurationsToExecute);
//        System.out.println("");
//
//        System.out.println("####################### Instrumenting #######################");
//        Instrumenter instrumenter = new TimerRegionInstrumenter(originalSrcDirectory, instrumentClassDirectory, partialRegionsToOptions);
//        instrumenter.instrument(args);
//        System.out.println("");
//
//        System.out.println("####################### Measure performancemodel #######################");
//        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, instrumentSrcDirectory, configurationsToExecute);
//        Set<PerformanceEntry2> measuredPerformance = executor.execute(args);
//        System.out.println("");
//
//        System.out.println("####################### Build performancemodel model #######################");
//        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
//
//        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
//            JavaRegion javaRegion = entry.getKey();
//            Region region = new Region(javaRegion.getRegionID());
//            regionsToOptions.put(region, entry.getValue());
//        }
//
////        PerformanceModel pm = ConfigCrusherPerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
////        System.out.println(pm);
////        JavaPipeline.savePMPerformance(programName, pm);
////
////        return pm;
//        // TODO
        return null;
    }

    public static PerformanceModel buildPerformanceModelRepeat(String programName, String[] args, String originalSrcDirectory, String originalClassDirectory, String instrumentSrcDirectory, String instrumentClassDirectory, String entryPoint) throws IOException, InterruptedException {
        // Format return statements with method calls
        // TODO compile both original and instrumented
//        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
        System.out.println("");

//        System.out.println("####################### Partial region and options #######################");
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
//        System.out.println("");
//
//        // Configuration compression (Language independent)
//        System.out.println("####################### Configurations to execute #######################");
//        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
//        Set<Set<String>> configurationsToExecute = compression.compressConfigurations(programName, args, relevantOptions);
//        JavaPipeline.compressionHelper(partialRegionsToOptions.values(), configurationsToExecute);
//        System.out.println("");
//
//        System.out.println("####################### Instrumenting #######################");
//        Instrumenter.instrument(args, instrumentSrcDirectory, instrumentClassDirectory, partialRegionsToOptions);
//        System.out.println("");
//
//        System.out.println("####################### Measure performancemodel #######################");
//        List<Set<PerformanceEntry2>> executionsPerformance = new ArrayList<>();
//
//        Options.getCommandLine(args);
//        for(int i = 0; i < Options.getIterations(); i++) {
//            executionsPerformance.add(BaseExecutor.measureConfigurationPerformance(programName + BaseExecutor.UNDERSCORE + i, args, entryPoint, instrumentClassDirectory, configurationsToExecute));
//        }
//
//        List<PerformanceStatistic> perfStats = BaseExecutor.getExecutionsStats(executionsPerformance);
//        Set<PerformanceEntry2> measuredPerformance = BaseExecutor.averageExecutions(perfStats, executionsPerformance.get(0));
//        System.out.println("");
//
//        System.out.println("####################### Build performancemodel model #######################");
//        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
//
//        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
//            JavaRegion javaRegion = entry.getKey();
//            Region region = new Region(javaRegion.getRegionID());
//            regionsToOptions.put(region, entry.getValue());
//        }
//
//        PerformanceModel pm = ConfigCrusherPerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
//        System.out.println(pm);
//        JavaPipeline.savePMPerformance(programName, pm, perfStats);
//
//        return pm;

        return null; // TODO make change since interface changed
    }

    public static PerformanceModel buildPerformanceModelRepeat(String programName, String[] args, String originalSrcDirectory, String originalClassDirectory, String instrumentSrcDirectory, String instrumentClassDirectory, String entryPoint, Map<JavaRegion, Set<String>> partialRegionsToOptions) throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        // Format return statements with method calls
//// TODO compile both original and instrumented
////        Formatter.format(originalSrcDirectory, originalClassDirectory, instrumentSrcDirectory, instrumentClassDirectory);
//        System.out.println("");
//
//        // Configuration compression (Language independent)
//        System.out.println("####################### Configurations to execute #######################");
//        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
//        Compression compression = new SimpleCompression(programName, relevantOptions);
//        Set<Set<String>> configurationsToExecute = compression.compressConfigurations(args);
//        JavaPipeline.compressionHelper(partialRegionsToOptions.values(), configurationsToExecute);
//        System.out.println("");
//
//        System.out.println("####################### Instrumenting #######################");
//        Instrumenter instrumenter = new TimerRegionInstrumenter(originalSrcDirectory, instrumentClassDirectory, partialRegionsToOptions);
//        instrumenter.instrument(args);
//        System.out.println("");
//
//        System.out.println("####################### Measure performancemodel #######################");
//        Executor executor = new ConfigCrusherExecutor(programName, entryPoint, instrumentSrcDirectory, configurationsToExecute);
//        Set<PerformanceEntry2> measuredPerformance = executor.execute(args);
//
//        // TODO
//
////        List<Set<PerformanceEntry2>> executionsPerformance = new ArrayList<>();
////
////        List<PerformanceStatistic> perfStats = BaseExecutor.getExecutionsStats(executionsPerformance);
////        Set<PerformanceEntry2> measuredPerformance = BaseExecutor.averageExecutions(perfStats, executionsPerformance.get(0));
//        System.out.println("");
//
//        System.out.println("####################### Build performancemodel model #######################");
//        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
//
//        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
//            JavaRegion javaRegion = entry.getKey();
//            Region region = new Region(javaRegion.getRegionID());
//            regionsToOptions.put(region, entry.getValue());
//        }
//
////        PerformanceModel pm = ConfigCrusherPerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
////        System.out.println(pm);
////        JavaPipeline.savePMPerformance(programName, pm, null);
////
////        return pm;
//        // TODO
        return null;
    }

    public static PerformanceModel buildPerformanceModel(String programName, String[] args, String srcDirectory, String classDirectory, String entryPoint) throws IOException, InterruptedException {
//        // Get regions and options
//        System.out.println("Region and options");
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, JavaPipeline.LOADTIME_DATABASE, JavaPipeline.TEST_COLLECTION);
//        System.out.println("");
//
//        // Configuration compression (Language independent)
//        System.out.println("Configurations to execute");
//        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
//        Set<Set<String>> configurationsToExecute = compression.compressConfigurations(programName, args, relevantOptions);
//        System.out.println(configurationsToExecute);
//        System.out.println("");
//
//        System.out.println("Instrumenting");
//        Instrumenter.instrument(args, srcDirectory, classDirectory, partialRegionsToOptions);
//        System.out.println("");
//
//        System.out.println("Measure performancemodel");
//        Set<PerformanceEntry2> measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurationsToExecute);
//        System.out.println("");
//
//        System.out.println("Build performancemodel model");
//        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
//
//        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
//            JavaRegion javaRegion = entry.getKey();
//            Region region = new Region(javaRegion.getRegionID());
//            regionsToOptions.put(region, entry.getValue());
//        }
//
//        return ConfigCrusherPerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
        return null; // TODO make change since interface changed
    }

    public static PerformanceModel buildPerformanceModel(String programName, String[] args, String srcDirectory, String classDirectory, String entryPoint, String sdgFile, List<String> features) throws IOException, InterruptedException {
//        // Get regions and options
//        System.out.println("Region and options");
//        Map<JavaRegion, Set<String>> partialRegionsToOptions = ProgramAnalysis.analyze(programName, args, sdgFile, entryPoint, features);
//        System.out.println("");
//
//        // Configuration compression (Language independent)
//        System.out.println("Configurations to execute");
//        Set<Set<String>> relevantOptions = new HashSet<>(partialRegionsToOptions.values());
//        Set<Set<String>> configurationsToExecute = compression.compressConfigurations(programName, args, relevantOptions);
//        System.out.println(configurationsToExecute);
//        System.out.println("");
//
//        System.out.println("Instrumenting");
//        Instrumenter.instrument(args, srcDirectory, classDirectory, partialRegionsToOptions);
//        System.out.println("");
//
//        System.out.println("Measure performancemodel");
//        Set<PerformanceEntry2> measuredPerformance = BaseExecutor.measureConfigurationPerformance(programName, args, entryPoint, classDirectory, configurationsToExecute);
//        System.out.println("");
//
//        System.out.println("Build performancemodel model");
//        Map<Region, Set<String>> regionsToOptions = new HashMap<>();
//
//        for(Map.Entry<JavaRegion, Set<String>> entry : partialRegionsToOptions.entrySet()) {
//            JavaRegion javaRegion = entry.getKey();
//            Region region = new Region(javaRegion.getRegionID());
//            regionsToOptions.put(region, entry.getValue());
//        }
//
//        return ConfigCrusherPerformanceModelBuilder.createPerformanceModel(programName, args, measuredPerformance, regionsToOptions);
        return null; // TODO make change since interface changed
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
        Compression compression = new SimpleCompression(programName);
        Set<Set<String>> measuredConfigurations = compression.compressConfigurations(args);
        // TODO
        Set<String> options = null;
        Set<Set<String>> configurations = Helper.getConfigurations(options);

        StringBuilder result = new StringBuilder();
        result.append("measured,configuration,performancemodel");
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
            // TODO
//            double perf = pm.evaluate(configuration);
//            result.append(perf);
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

    public static void savePMPerformance(String programName, PerformanceModel pm, List<PerformanceStatistic> perfStats) throws IOException {
        JavaPipeline.savePMPerformance(programName, pm);

        double avgStd = 0;
        int count = 0;

        for(PerformanceStatistic perfStat : perfStats) {
            for(Long std : perfStat.getRegionsToStd().values()) {
                avgStd += std / 1000000000.0;
                count++;
            }
        }

        avgStd = avgStd / count;


        String result = "Average std: " + avgStd + "\n";

        String outputFile = JavaPipeline.PM_RES_DIR + "/" + programName + Options.DOT_CSV;
        File file = new File(outputFile);
        FileWriter writer = new FileWriter(file, true);
        writer.write(result);
        writer.flush();
        writer.close();
    }

}
