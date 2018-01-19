package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.colorcounter.BFColorCounterAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.grep.BFGrepAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.kanzi.BFKanziAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.optimizer.BFOptimizerAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.prevayler.BFPrevaylerAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions12.BFRegions12Adapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions16.BFRegions16Adapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.runningexample.BFRunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.region.RegionsCounter;
import edu.cmu.cs.mvelezce.tool.execute.java.BaseExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BruteForceExecutor extends BaseExecutor {

    // TODO this is weird and creating a lot of bugs
    static {
        DIRECTORY = BaseExecutor.DIRECTORY + "/bruteforce/programs";
    }

    public BruteForceExecutor(String programName) {
        this(programName, null, null, null);
    }

    public BruteForceExecutor(String programName, String entryPoint, String dir, Set<Set<String>> configurations) {
        super(programName, entryPoint, dir, configurations);
    }

    public static Set<Set<String>> getBruteForceConfigurations(Set<Set<String>> configurations) {
        Set<String> options = new HashSet<>();

        for(Set<String> configuration : configurations) {
            options.addAll(configuration);
        }

        return Helper.getConfigurations(options);
    }

    public Map<String, Long> getResults() {
        Map<String, Long> result = RegionsCounter.getRegionsToCount();

        if(!result.isEmpty()) {
            return result;
        }

        result = Regions.getRegionsToProcessedPerformance();

        if(!result.isEmpty()) {
            return result;
        }

        throw new RuntimeException("No data is available");
    }

//    public static Set<PerformanceEntry> repeatProcessMeasure(String programName, int iterations, String srcDir, String classDir, String entryPoint) throws IOException, ParseException, InterruptedException {
//// TODO compile both original and instrumented
//        //        Formatter.compile(srcDir, classDir);
////        Formatter.formatReturnWithMethod(classDir);
//
//        String[] args = new String[0];
//        Options.getCommandLine(args);
//
//        Set<String> options = BaseExecutor.getOptions(programName);
//        Set<Set<String>> configurations = Helper.getConfigurations(options);
//
//        programName += "-bf";
//        args = new String[3];
//        args[0] = "-delres";
//        args[1] = "-saveres";
//        args[2] = "-i" + iterations;
//        Options.getCommandLine(args);
//
//        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();
//
//        for(int i = 0; i < Options.getIterations(); i++) {
//            executionsPerformance.add(BaseExecutor.measureConfigurationPerformance(programName + BaseExecutor.UNDERSCORE + i, args, entryPoint, classDir, configurations));
//        }
//
//        List<PerformanceEntryStatistic> perfStats = BaseExecutor.getExecutionsStats(executionsPerformance);
//        Set<PerformanceEntry> measuredPerformance = BaseExecutor.averageExecutions(perfStats, executionsPerformance.get(0));
//        programName = programName.substring(0, programName.indexOf("-"));
//        BruteForce.saveBFPerformance(programName, perfStats);
//
//        return measuredPerformance;
//    }

//    public static void saveBFPerformance(String programName, List<PerformanceEntryStatistic> perfStats) throws IOException {
//        File file = new File(BruteForce.BF_RES_DIR + "/" + programName + Options.DOT_CSV);
//
//        if(file.exists()) {
//            if(!file.delete()) {
//                throw new RuntimeException("Could not delete " + file);
//            }
//        }
//
//        StringBuilder result = new StringBuilder();
//        result.append("measured,configuration,performancemodel,std");
//        result.append("\n");
//
//        for(PerformanceEntryStatistic perfStat : perfStats) {
//            if(perfStat.getRegionsToMean().size() != 1) {
//                throw new RuntimeException("The performancemodel entry should only have measured the entire program " + perfStat.getRegionsToMean().keySet());
//            }
//
////            perfStat.setMeasured("true");
//            result.append("true");
//            result.append(",");
//            result.append('"');
//            result.append(perfStat.getConfiguration());
//            result.append('"');
//            result.append(",");
//            result.append(perfStat.getRegionsToMean().values().iterator().next() / 1000000000.0);
//            result.append(",");
//            result.append(perfStat.getRegionsToStd().values().iterator().next() / 1000000000.0);
//            result.append("\n");
//        }
//
//        File directory = new File(BruteForce.BF_RES_DIR);
//
//        if(!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String outputFile = directory + "/" + programName + Options.DOT_CSV;
//        file = new File(outputFile);
//        FileWriter writer = new FileWriter(file, true);
//        writer.write(result.toString());
//        writer.flush();
//        writer.close();
//    }

    @Override
    public Set<DefaultPerformanceEntry> execute(int iteration) throws IOException, InterruptedException {
        // TODO factory pattern or switch statement to create the right adapter
        Adapter adapter;

        if(this.getProgramName().contains("running-example")) {
            adapter = new BFRunningExampleAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("pngtasticColorCounter")) {
            adapter = new BFColorCounterAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("regions12")) {
            adapter = new BFRegions12Adapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("regions16")) {
            adapter = new BFRegions16Adapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("pngtasticOptimizer")) {
            adapter = new BFOptimizerAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("prevayler")) {
            adapter = new BFPrevaylerAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("kanzi")) {
            adapter = new BFKanziAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("grep")) {
            adapter = new BFGrepAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else {
            throw new RuntimeException("Could not create an adapter for " + this.getProgramName());
        }

        for(Set<String> configuration : this.getConfigurations()) {
            adapter.execute(configuration, iteration);
        }

        String outputDir = BaseExecutor.DIRECTORY + "/" + this.getProgramName() + "/" + iteration;
        File outputFile = new File(outputDir);

        if(!outputFile.exists()) {
            throw new RuntimeException("The output file could not be found " + outputDir);
        }

        Set<DefaultPerformanceEntry> performanceEntries = this.aggregateExecutions(outputFile);
        return performanceEntries;


//        programName += "-bf";
//        String[] args = new String[1];
//        args[0] = "-i" + iterations;
//        Options.getCommandLine(args);
//
//        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();
//
//        for(int i = 0; i < Options.getIterations(); i++) {
//            executionsPerformance.add(BaseExecutor.measureConfigurationPerformance(programName + BaseExecutor.UNDERSCORE + i, args));
//        }
//
//        List<PerformanceEntryStatistic> perfStats = BaseExecutor.getExecutionsStats(executionsPerformance);
//        Set<PerformanceEntry> measuredPerformance = BaseExecutor.averageExecutions(perfStats, executionsPerformance.get(0));
//        programName = programName.substring(0, programName.indexOf("-"));
//        BruteForce.saveBFPerformance(programName, perfStats);
//
//        return measuredPerformance;
    }

}
