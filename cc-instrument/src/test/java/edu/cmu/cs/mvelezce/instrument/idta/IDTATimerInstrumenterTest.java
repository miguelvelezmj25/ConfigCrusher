package edu.cmu.cs.mvelezce.instrument.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapter.adapters.canExpandConstraintsDown.BaseCanExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.cannotExpandConstraintsDown.BaseCannotExpandConstraintsDownAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.multipleReturns.BaseMultipleReturnsAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.pngtastic.BasePngtasticAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.idta.IDTAAnalysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrumenter.instrument.Instrumenter;
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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BaseTrivialAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseTrivialAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseTrivialAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseTrivialAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BaseIGenAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseIGenAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseIGenAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseIGenAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseMeasureDiskOrderedScanAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseMeasureDiskOrderedScanAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseMeasureDiskOrderedScanAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BaseIndexFilesAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseIndexFilesAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseIndexFilesAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseIndexFilesAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BasePngtasticAdapter.MAIN_CLASS;
    String srcDir = "../" + BasePngtasticAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BasePngtasticAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BasePngtasticAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BaseCannotExpandConstraintsDownAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseCannotExpandConstraintsDownAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseCannotExpandConstraintsDownAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseCannotExpandConstraintsDownAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BaseCanExpandConstraintsDownAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseCanExpandConstraintsDownAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseCanExpandConstraintsDownAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseCanExpandConstraintsDownAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

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

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis = new IDTAAnalysis(programName);
    Map<JavaRegion, Set<FeatureExpr>> regionsToConstraints = analysis.analyze();

    String mainClass = BaseMultipleReturnsAdapter.MAIN_CLASS;
    String srcDir = "../" + BaseMultipleReturnsAdapter.INSTRUMENTED_DIR_PATH;
    String classDir = "../" + BaseMultipleReturnsAdapter.INSTRUMENTED_CLASS_PATH;
    Set<String> options = new HashSet<>(BaseMultipleReturnsAdapter.getListOfOptions());
    Instrumenter instrumenter =
        new IDTATimerInstrumenter(
            programName, mainClass, srcDir, classDir, options, regionsToConstraints);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    instrumenter.instrument(args);
  }
}
