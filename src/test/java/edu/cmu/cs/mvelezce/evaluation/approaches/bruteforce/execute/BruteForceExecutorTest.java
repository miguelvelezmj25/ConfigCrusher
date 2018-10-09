package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.Compression;
import edu.cmu.cs.mvelezce.tool.compression.simple.SimpleCompression;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.colorCounter.ColorCounterAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.email.EmailAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.find.FindAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.grep.GrepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.kanzi.KanziAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.optimizer.OptimizerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler.PrevaylerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sort.SortAdapter;
import edu.cmu.cs.mvelezce.tool.performance.entry.PerformanceEntryStatistic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class BruteForceExecutorTest {

  //    @Test
  //    public void testElevator() throws IOException, ParseException, InterruptedException {
  //        String programName = "elevator";
  //        String srcDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/";
  //        String classDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/out/production/elevator/";
  //        String entryPoint = "edu.cmu.cs.mvelezce.PL_Interface_impl";
  //
  //        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5,
  // srcDir, classDir, entryPoint);
  ////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
  //    }
  //
  //    @Test
  //    public void testPngtastic() throws IOException, ParseException, InterruptedException {
  //        String programName = "pngtastic";
  //        String srcDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/";
  //        String classDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic/target/classes/";
  //        String entryPoint = "com.googlecode.pngtastic.PngtasticColorCounter";
  //
  //        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1,
  // srcDir, classDir, entryPoint);
  ////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
  //    }
  //
  //    @Test
  //    public void testSleep1() throws IOException, ParseException, InterruptedException {
  //        String programName = "sleep1";
  //        String srcDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
  //        String classDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
  //        String entryPoint = "edu.cmu.cs.mvelezce.Sleep1";
  //
  //        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5,
  // srcDir, classDir, entryPoint);
  ////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
  //    }
  //
  //    @Test
  //    public void testSleep26() throws IOException, ParseException, InterruptedException {
  //        String programName = "sleep26";
  //        String srcDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
  //        String classDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
  //        String entryPoint = "edu.cmu.cs.mvelezce.Sleep26";
  //
  //        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1,
  // srcDir, classDir, entryPoint);
  ////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
  //    }
  //
  //    @Test
  //    public void testSleep29() throws IOException, ParseException, InterruptedException {
  //        String programName = "sleep29";
  //        String srcDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/";
  //        String classDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy/";
  //        String entryPoint = "edu.cmu.cs.mvelezce.Sleep29";
  //
  //        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1,
  // srcDir, classDir, entryPoint);
  ////        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 5);
  //    }
  //
  //    @Test
  //    public void testZipme() throws IOException, ParseException, InterruptedException {
  //        String programName = "zipme";
  //        String srcDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/";
  //        String classDir = BaseAdapter.USER_HOME +
  // "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/zipme/out/production/zipme/";
  //        String entryPoint = "edu.cmu.cs.mvelezce.ZipMain";
  //
  //        Set<PerformanceEntry> measuredPerf = BruteForce.repeatProcessMeasure(programName, 1,
  // srcDir, classDir, entryPoint);
  //    }

  @Test
  public void runningExample() throws IOException, InterruptedException {
    String programName = RunningExampleMain.PROGRAM_NAME;
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/running-example/target/classes";
    String entryPoint = "edu.cmu.cs.mvelezce.Example";
    String iterations = "5";

    List<String> argsList = new ArrayList<>();
    argsList.add(programName);
    argsList.add(classDirectory);
    argsList.add(entryPoint);
    argsList.add(iterations);

    String[] args = new String[argsList.size()];
    args = argsList.toArray(args);

    BruteForceExecutor.main(args);
  }

  @Test
  public void berkeley() throws IOException, InterruptedException {
    String programName = "berkeley-db";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/berkeley-db/target/classes";
    String entryPoint = "com.sleepycat.analysis.Run";
    String iterations = "5";

    List<String> argsList = new ArrayList<>();
    argsList.add(programName);
    argsList.add(classDirectory);
    argsList.add(entryPoint);
    argsList.add(iterations);

    String[] args = new String[argsList.size()];
    args = argsList.toArray(args);

    BruteForceExecutor.main(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = "lucene";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/lucene/demo/target/classes"
            + BaseAdapter.PATH_SEPARATOR
            + BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/lucene/analysis/common/target/classes"
            + BaseAdapter.PATH_SEPARATOR
            + BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/lucene/codecs/target/classes"
            + BaseAdapter.PATH_SEPARATOR
            + BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/lucene/jars/core/lucene-core-7.4.0.jar";
    String entryPoint = "org.apache.lucene.demo.IndexFiles";
    String iterations = "5";

    List<String> argsList = new ArrayList<>();
    argsList.add(programName);
    argsList.add(classDirectory);
    argsList.add(entryPoint);
    argsList.add(iterations);

    String[] args = new String[argsList.size()];
    args = argsList.toArray(args);

    BruteForceExecutor.main(args);
  }

  @Test
  public void colorCounter() throws IOException, InterruptedException {
    String programName = "pngtasticColorCounter";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-counter/out/production/pngtastic-counter";
    String entryPoint = "counter.com.googlecode.pngtastic.Run";

    Set<String> options = new HashSet<>(ColorCounterAdapter.getColorCounterOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void optimizer() throws IOException, InterruptedException {
    String programName = "pngtasticOptimizer";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/pngtastic-optimizer/out/production/pngtastic-optimizer";
    String entryPoint = "optimizer.com.googlecode.pngtastic.Run";

    Set<String> options = new HashSet<>(OptimizerAdapter.getOptimizerOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void prevayler() throws IOException, InterruptedException {
    String programName = "prevayler";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/target/classes";
    String entryPoint = "org.prevayler.demos.demo1.PrimeNumbers";

    Set<String> options = new HashSet<>(PrevaylerAdapter.getPrevaylerOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void prevayler1() throws IOException, InterruptedException {
    String programName = "prevayler";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/prevayler/target/classes";
    String entryPoint = "org.prevayler.demos.demo1.PrimeNumbers";

    Set<String> options = new HashSet<>(PrevaylerAdapter.getPrevaylerOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);

    configurations.clear();
    Set<String> n = new HashSet<>();
    n.add("FILEAGETHRESHOLD");
    //        n.add("DEEPCOPY");
    //        n.add("MONITOR");
    //        n.add("DISKSYNC");
    n.add("SNAPSHOTSERIALIZER");
    //        n.add("CLOCK");
    configurations.add(n);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void kanzi() throws IOException, InterruptedException {
    String programName = "kanzi";
    String entryPoint = "kanzi.Run";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/kanzi/target/classes";

    Set<String> options = new HashSet<>(KanziAdapter.getKanziOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void regions12() throws IOException, InterruptedException {
    String programName = "regions12";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ;
    String entryPoint = "edu.cmu.cs.mvelezce.Regions12";

    // Program arguments
    String[] args = new String[0];

    Compression compression = new SimpleCompression(programName);
    Set<Set<String>> configurations = compression.compressConfigurations(args);
    configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
    System.out.println("Configurations to sample: " + configurations.size());

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void regions16() throws IOException, InterruptedException {
    String programName = "regions16";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/dummy/out/production/dummy";
    ;
    String entryPoint = "edu.cmu.cs.mvelezce.Regions16";

    // Program arguments
    String[] args = new String[0];

    Compression compression = new SimpleCompression(programName);
    Set<Set<String>> configurations = compression.compressConfigurations(args);
    configurations = BruteForceExecutor.getBruteForceConfigurations(configurations);
    System.out.println("Configurations to sample: " + configurations.size());

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void grep() throws IOException, InterruptedException {
    String programName = "grep";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/grep/target/classes";

    String entryPoint = "org.unix4j.grep.Main";

    Set<String> options = new HashSet<>(GrepAdapter.getGrepOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void sort1() throws IOException, InterruptedException {
    String programName = "sort";
    String classDirectory =
        "/Users/miguelvelez/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/sort/target/classes";

    String entryPoint = "org.unix4j.sort.Main";

    Set<String> options = new HashSet<>(SortAdapter.getSortOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    configurations.clear();

    Set<String> conf = new HashSet<>();
    conf.add("NUMERICSORT");

    configurations.add(conf);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void find() throws IOException, InterruptedException {
    String programName = "find";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/find/target/classes";

    String entryPoint = "org.unix4j.find.Main";

    Set<String> options = new HashSet<>(FindAdapter.getFindOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void elevator() throws IOException, InterruptedException {
    String programName = "elevator";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/target/classes";

    String entryPoint = "family.PL_Interface_impl";

    Set<String> options = new HashSet<>(ElevatorAdapter.getElevatorOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void email() throws IOException, InterruptedException {
    String programName = "email";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/email/target/classes";

    String entryPoint = "family.PL_Interface_impl";

    Set<String> options = new HashSet<>(EmailAdapter.getEmailOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void density1() throws Exception {
    String programName = "density";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/density/target/classes";
    String entryPoint = "at.favre.tools.dconvert.Main";

    // Program arguments
    String[] args = new String[0];

    Compression compression = new SimpleCompression(programName);
    Set<Set<String>> configurations = compression.compressConfigurations(args);

    configurations.clear();
    Set<String> n = new HashSet<>();

    n.add("PLATFORM");
    n.add("UPSCALINGALGORITHM");
    n.add("OUTPUTCOMPRESSIONGMODE");
    n.add("DOWNSCALINGALGORITHM");

    //        n.add("SKIPUPSCALING");
    //        n.add("SCALEMODE");

    configurations.add(n);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }

  @Test
  public void elevator1() throws IOException, InterruptedException {
    String programName = "elevator";
    String classDirectory =
        BaseAdapter.USER_HOME
            + "/Documents/Programming/Java/Projects/performance-mapper-evaluation/original/elevator/target/classes";

    String entryPoint = "family.PL_Interface_impl";

    Set<String> options = new HashSet<>(ElevatorAdapter.getElevatorOptions());
    Set<Set<String>> configurations =
        BruteForceExecutor.getBruteForceConfigurationsFromOptions(options);
    System.out.println("Configurations to sample: " + configurations.size());

    configurations.clear();
    Set<String> conf = new HashSet<>();
    configurations.add(conf);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i1";

    Executor executor =
        new BruteForceExecutor(programName, entryPoint, classDirectory, configurations);
    Set<PerformanceEntryStatistic> measuredPerformance = executor.execute(args);
  }
}
