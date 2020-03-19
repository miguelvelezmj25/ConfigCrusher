package edu.cmu.cs.mvelezce.instrument.idta;

import edu.cmu.cs.mvelezce.adapters.canExpandConstraintsDown.BaseCanExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.canRemoveNestedConstraintsMultipleCallSites.BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotExpandConstraintsDown.BaseCannotExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapters.cannotRemoveNestedRegions.BaseCannotRemoveNestedRegionsAdapter;
import edu.cmu.cs.mvelezce.adapters.cleanConstraints.BaseCleanConstraintsAdapter;
import edu.cmu.cs.mvelezce.adapters.cleanConstraintsIssue.BaseCleanConstraintsIssueAdapter;
import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.methodCall.BaseMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.multipleReturns.BaseMultipleReturnsAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.adapters.staticMethodCall.BaseStaticMethodCallAdapter;
import edu.cmu.cs.mvelezce.adapters.subtraces.BaseSubtracesAdapter;
import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.idta.IDTAAnalysis;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.instrument.idta.transform.instrumentation.time.IDTAExecutionTimeMethodInstrumenter;
import edu.cmu.cs.mvelezce.instrumenter.instrument.Instrumenter;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IDTATimerInstrumenterTest {

  @Test
  public void trivial()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseTrivialAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseTrivialAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseTrivialAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseTrivialAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void staticMethodCall()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseStaticMethodCallAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseStaticMethodCallAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseStaticMethodCallAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseStaticMethodCallAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseStaticMethodCallAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void cannotRemoveNestedRegions()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseCannotRemoveNestedRegionsAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseCannotRemoveNestedRegionsAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseCannotRemoveNestedRegionsAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseCannotRemoveNestedRegionsAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseCannotRemoveNestedRegionsAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void methodCall()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseMethodCallAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseMethodCallAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseMethodCallAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseMethodCallAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseMethodCallAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void subtraces()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseSubtracesAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseSubtracesAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseSubtracesAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseSubtracesAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseSubtracesAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void canRemoveNestedConstraintsMultipleCallSites()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.MAIN_CLASS;
    String srcDir =
        "../" + BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.INSTRUMENTED_DIR_PATH;
    String classDir =
        "../" + BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options =
        new HashSet<>(BaseCanRemoveNestedConstraintsMultipleCallSitesAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void iGen()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseIGenAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseIGenAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseIGenAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseIGenAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseIGenAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void berkeleyDb()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR;
    String classDir = "../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void lucene()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseIndexFilesAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseIndexFilesAdapter.ORIGINAL_ROOT_DIR;
    String classDir = "../" + BaseIndexFilesAdapter.ORIGINAL_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void lucene_large()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    String workloadSize = "large";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseIndexFilesAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseIndexFilesAdapter.ORIGINAL_ROOT_DIR;
    String classDir = "../" + BaseIndexFilesAdapter.ORIGINAL_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void pngtasticCounter()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BasePngtasticAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BasePngtasticAdapter.MAIN_CLASS;
    String srcDir = "../" + BasePngtasticAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BasePngtasticAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BasePngtasticAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void cannotExpandConstraintsDown()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseCannotExpandConstraintsDownAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseCannotExpandConstraintsDownAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseCannotExpandConstraintsDownAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseCannotExpandConstraintsDownAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseCannotExpandConstraintsDownAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void canExpandConstraintsDown()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseCanExpandConstraintsDownAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseCanExpandConstraintsDownAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseCanExpandConstraintsDownAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseCanExpandConstraintsDownAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseCanExpandConstraintsDownAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void multipleReturns()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseMultipleReturnsAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseMultipleReturnsAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseMultipleReturnsAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseMultipleReturnsAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseMultipleReturnsAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void cleanConstraints()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseCleanConstraintsAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseCleanConstraintsAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseCleanConstraintsAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseCleanConstraintsAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseCleanConstraintsAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void cleanConstraintsIssue()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseCleanConstraintsIssueAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseCleanConstraintsIssueAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseCleanConstraintsIssueAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseCleanConstraintsIssueAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseCleanConstraintsIssueAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void performance()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BasePerformanceAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BasePerformanceAdapter.MAIN_CLASS;
    String srcDir = "../" + BasePerformanceAdapter.ORIGINAL_DIR_PATH;
    String classDir = "../" + BasePerformanceAdapter.ORIGINAL_CLASS_PATH;
    Set<String> options = new HashSet<>(BasePerformanceAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void multithread()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseMultithreadAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseMultithreadAdapter.ORIGINAL_DIR_PATH;
    String classDir = "../" + BaseMultithreadAdapter.ORIGINAL_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseMultithreadAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void convert()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseConvertAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseConvertAdapter.ORIGINAL_DIR_PATH;
    String classDir = "../" + BaseConvertAdapter.ORIGINAL_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseConvertAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }

  @Test
  public void runBenchC()
      throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException {
    System.err.println("Have to delete the META-INF dir in the h2 ./target dir");

    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    String workloadSize = "small";

    Analysis<Map<JavaRegion, Partitioning>> analysis = new IDTAAnalysis(programName, workloadSize);
    Map<JavaRegion, Partitioning> regionsToPartitions = analysis.analyze();

    String mainClass = BaseRunBenchCAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseRunBenchCAdapter.ORIGINAL_DIR_PATH;
    String classDir = "../" + BaseRunBenchCAdapter.ORIGINAL_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseRunBenchCAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName,
            mainClass,
            srcDir,
            classDir,
            options,
            regionsToPartitions,
            new IDTAExecutionTimeMethodInstrumenter());

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }
}
