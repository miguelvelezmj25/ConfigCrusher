package edu.cmu.cs.mvelezce.allmethodsareregions;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.gt.GTCompression;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllMethodsAreRegionsAnalysisTest {

  private Set<FeatureExpr> getConstraints(Set<Set<String>> configs, List<String> options) {
    Set<FeatureExpr> constraints = new HashSet<>();

    for (Set<String> config : configs) {
      String stringConstraint = ConstraintUtils.parseAsConstraint(config, options);
      constraints.add(MinConfigsGenerator.parseAsFeatureExpr(stringConstraint));
    }

    return constraints;
  }

  @Test
  public void berkeleyDB()
      throws InvocationTargetException, NoSuchMethodException, IOException, IllegalAccessException,
          InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();

    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);
    Set<FeatureExpr> constraints = this.getConstraints(configs, options);

    String mainClass = BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS;
    String classDir = "../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_CLASS_PATH;

    Analysis<Map<JavaRegion, Set<FeatureExpr>>> analysis =
        new AllMethodsAreRegionsAnalysis(programName, options, mainClass, classDir, constraints);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    analysis.analyze(args);
  }
}
