package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.berkeley.BFBerkeleyAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.colorcounter.BFColorCounterAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.density.BFDensityAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.elevator.BFElevatorAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.email.BFEmailAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.find.BFFindAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.grep.BFGrepAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.kanzi.BFKanziAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.lucene.BFLuceneAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.optimizer.BFOptimizerAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.prevayler.BFPrevaylerAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions12.BFRegions12Adapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions16.BFRegions16Adapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.runningexample.BFRunningExampleAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.sort.BFSortAdapter;
import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.trivial.BFTrivialAdapter;
import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.analysis.region.RegionsCounter;
import edu.cmu.cs.mvelezce.tool.execute.java.BaseExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.berkeley.BerkeleyAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.berkeley.BerkeleyMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.lucene.LuceneAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.lucene.LuceneMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.trivial.TrivialAdapter;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BruteForceExecutor extends BaseExecutor {

  public static void main(String[] args) throws IOException, InterruptedException {
    String program = args[0];
    String classDirectory = args[1];
    String entryPoint = args[2];
    String iterations = args[3];

    if (program.equals(RunningExampleMain.PROGRAM_NAME)) {
      executeRunningExample(classDirectory, entryPoint, iterations);
    }
    else if (program.equals(BerkeleyMain.PROGRAM_NAME)) {
      executeBerkeley(classDirectory, entryPoint, iterations);
    }
    else if (program.equals(LuceneMain.PROGRAM_NAME)) {
      executeLucene(classDirectory, entryPoint, iterations);
    }
    else {
      throw new RuntimeException("Could not find the program " + program + " to run");
    }
  }

  private static void executeLucene(String classDirectory, String entryPoint, String iterations)
      throws IOException, InterruptedException {
    Set<String> options = new HashSet<>(LuceneAdapter.getLuceneOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    // Program arguments
    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i" + iterations;

    Executor executor =
        new BruteForceExecutor(
            LuceneMain.PROGRAM_NAME, entryPoint, classDirectory, configurations);
    executor.execute(args);
  }

  private static void executeBerkeley(String classDirectory, String entryPoint, String iterations)
      throws IOException, InterruptedException {
    Set<String> options = new HashSet<>(BerkeleyAdapter.getBerkeleyOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    // Program arguments
    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i" + iterations;

    Executor executor =
        new BruteForceExecutor(
            BerkeleyMain.PROGRAM_NAME, entryPoint, classDirectory, configurations);
    executor.execute(args);
  }

  private static void executeRunningExample(String classDirectory, String entryPoint,
      String iterations)
      throws IOException, InterruptedException {
    Set<String> options = new HashSet<>(RunningExampleAdapter.getRunningExampleOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    // Program arguments
    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i" + iterations;

    Executor executor =
        new BruteForceExecutor(
            RunningExampleMain.PROGRAM_NAME, entryPoint, classDirectory, configurations);
    executor.execute(args);
  }

  public BruteForceExecutor(String programName) {
    this(programName, null, null, null);
  }

  public BruteForceExecutor(
      String programName, String entryPoint, String dir, Set<Set<String>> configurations) {
    super(programName, entryPoint, dir, configurations);
  }

  public static Set<Set<String>> getBruteForceConfigurations(Set<Set<String>> configurations) {
    Set<String> options = new HashSet<>();

    for (Set<String> configuration : configurations) {
      options.addAll(configuration);
    }

    return BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
  }

  public static Set<Set<String>> getBruteForceConfigurationsFromOptions(Set<String> options) {
    return Helper.getConfigurations(options);
  }

  public Map<String, Long> getResults() {
    Map<String, Long> result = RegionsCounter.getRegionsToCount();

    if (!result.isEmpty()) {
      return result;
    }

    result = Regions.getRegionsToProcessedPerformance();

    if (!result.isEmpty()) {
      return result;
    }

    throw new RuntimeException("No data is available");
  }

  //    public static Set<PerformanceEntry> repeatProcessMeasure(String programName, int iterations,
  // String srcDir, String classDir, String entryPoint) throws IOException, ParseException,
  // InterruptedException {
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
  //            executionsPerformance.add(BaseExecutor.measureConfigurationPerformance(programName +
  // BaseExecutor.UNDERSCORE + i, args, entryPoint, classDir, configurations));
  //        }
  //
  //        List<PerformanceEntryStatistic> perfStats =
  // BaseExecutor.getExecutionsStats(executionsPerformance);
  //        Set<PerformanceEntry> measuredPerformance = BaseExecutor.averageExecutions(perfStats,
  // executionsPerformance.get(0));
  //        programName = programName.substring(0, programName.indexOf("-"));
  //        BruteForce.saveBFPerformance(programName, perfStats);
  //
  //        return measuredPerformance;
  //    }

  //    public static void saveBFPerformance(String programName, List<PerformanceEntryStatistic>
  // perfStats) throws IOException {
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
  //                throw new RuntimeException("The performancemodel entry should only have measured
  // the entire program " + perfStat.getRegionsToMean().keySet());
  //            }
  //
  ////            perfStat.setMeasured("true");
  //            result.append("true");
  //            result.append(",");
  //            result.append('"');
  //            result.append(perfStat.getConfiguration());
  //            result.append('"');
  //            result.append(",");
  //            result.append(perfStat.getRegionsToMean().values().iterator().next() /
  // 1000000000.0);
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
  public Set<DefaultPerformanceEntry> execute(int iteration)
      throws IOException, InterruptedException {
    // TODO factory pattern or switch statement to create the right adapter

    String programName = this.getProgramName();
    Adapter adapter;

    if (programName.contains(RunningExampleMain.PROGRAM_NAME)) {
      adapter = new BFRunningExampleAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("pngtasticColorCounter")) {
      adapter = new BFColorCounterAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("regions12")) {
      adapter = new BFRegions12Adapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("regions16")) {
      adapter = new BFRegions16Adapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("pngtasticOptimizer")) {
      adapter = new BFOptimizerAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("prevayler")) {
      adapter = new BFPrevaylerAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("kanzi")) {
      adapter = new BFKanziAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("grep")) {
      adapter = new BFGrepAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("find")) {
      adapter = new BFFindAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("sort")) {
      adapter = new BFSortAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("density")) {
      adapter = new BFDensityAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("elevator")) {
      adapter = new BFElevatorAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.contains("email")) {
      adapter = new BFEmailAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.equals(BerkeleyMain.PROGRAM_NAME)) {
      adapter = new BFBerkeleyAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.equals(LuceneMain.PROGRAM_NAME)) {
      adapter = new BFLuceneAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else if (programName.equals(TrivialAdapter.PROGRAM_NAME)) {
      adapter = new BFTrivialAdapter(programName, this.getEntryPoint(), this.getClassDir());
    }
    else {
      throw new RuntimeException("Could not create an adapter for " + programName);
    }

    for (Set<String> configuration : this.getConfigurations()) {
      adapter.execute(configuration, iteration);

      System.gc();
      Thread.sleep(5000);
    }

    String outputDir = this.getOutputDir() + "/" + programName + "/" + iteration;
    File outputFile = new File(outputDir);

    if (!outputFile.exists()) {
      throw new RuntimeException("The output file could not be found " + outputDir);
    }

    return this.aggregateExecutions(outputFile);

    //        programName += "-bf";
    //        String[] args = new String[1];
    //        args[0] = "-i" + iterations;
    //        Options.getCommandLine(args);
    //
    //        List<Set<PerformanceEntry>> executionsPerformance = new ArrayList<>();
    //
    //        for(int i = 0; i < Options.getIterations(); i++) {
    //            executionsPerformance.add(BaseExecutor.measureConfigurationPerformance(programName
    // + BaseExecutor.UNDERSCORE + i, args));
    //        }
    //
    //        List<PerformanceEntryStatistic> perfStats =
    // BaseExecutor.getExecutionsStats(executionsPerformance);
    //        Set<PerformanceEntry> measuredPerformance = BaseExecutor.averageExecutions(perfStats,
    // executionsPerformance.get(0));
    //        programName = programName.substring(0, programName.indexOf("-"));
    //        BruteForce.saveBFPerformance(programName, perfStats);
    //
    //        return measuredPerformance;
  }

  @Override
  public String getOutputDir() {
    return BaseExecutor.DIRECTORY + "/bruteforce/programs";
  }
}
