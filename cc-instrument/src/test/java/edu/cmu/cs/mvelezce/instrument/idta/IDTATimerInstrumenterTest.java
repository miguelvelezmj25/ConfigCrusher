package edu.cmu.cs.mvelezce.instrument.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapter.adapters.iGen.BaseIGenAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapter.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
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
}
