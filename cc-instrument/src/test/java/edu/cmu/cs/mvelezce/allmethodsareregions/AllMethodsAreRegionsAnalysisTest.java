package edu.cmu.cs.mvelezce.allmethodsareregions;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.gt.GTCompression;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partitioning;
import edu.cmu.cs.mvelezce.explorer.idta.partition.TotalPartition;
import edu.cmu.cs.mvelezce.explorer.utils.ConstraintUtils;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.region.java.JavaRegion;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllMethodsAreRegionsAnalysisTest {

  private Partitioning getPartitioning(Set<Set<String>> configs, List<String> options) {
    Set<Partition> partitions = new HashSet<>();

    for (Set<String> config : configs) {
      String stringPartition = ConstraintUtils.parseAsConstraint(config, options);
      partitions.add(
          new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, stringPartition)));
    }

    return new TotalPartition(partitions);
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
    Partitioning partitioning = this.getPartitioning(configs, options);

    String mainClass = BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS;
    String classDir = "../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_CLASS_PATH;

    Analysis<Map<JavaRegion, Partitioning>> analysis =
        new AllMethodsAreRegionsAnalysis(programName, options, mainClass, classDir, partitioning);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";

    analysis.analyze(args);
  }
}
