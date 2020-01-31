package edu.cmu.cs.mvelezce.pretty.idta;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.builder.idta.IDTAPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.explorer.idta.IDTA;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.explorer.utils.FeatureExprUtils;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.pretty.BasePrettyBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class IDTAPrettyBuilderTest {

  @Test
  public void berkeleyDB() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseAnalysis<PerformanceModel<Partition>> builder =
        new IDTAPerformanceModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<Partition> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BasePrettyBuilder<Partition> analysis = new IDTAPrettyBuilder(programName, options, model);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    analysis.analyze(args);
  }

  @Test
  public void test1() throws IOException, InterruptedException {
    Partition partition1 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "!A && !B && !C"));
    Partition partition2 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "A && !B && !C"));
    Partition partition3 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "!A && B && !C"));
    Partition partition4 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "!A && !B && C"));
    Partition partition5 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "A && B && !C"));
    Partition partition6 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "A && !B && C"));
    Partition partition7 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "!A && B && C"));
    Partition partition8 =
        new Partition(FeatureExprUtils.parseAsFeatureExpr(IDTA.USE_BDD, "A && B && C"));

    Set<String> config1 = new HashSet<>();
    Set<String> config2 = new HashSet<>();
    config2.add("A");
    Set<String> config3 = new HashSet<>();
    config3.add("B");
    Set<String> config4 = new HashSet<>();
    config4.add("C");
    Set<String> config5 = new HashSet<>();
    config5.add("A");
    config5.add("B");
    Set<String> config6 = new HashSet<>();
    config6.add("A");
    config6.add("C");
    Set<String> config7 = new HashSet<>();
    config7.add("B");
    config7.add("C");
    Set<String> config8 = new HashSet<>();
    config8.add("A");
    config8.add("B");
    config8.add("C");

    Map<Partition, Set<String>> partitionsToConfigs = new HashMap<>();
    partitionsToConfigs.put(partition1, config1);
    partitionsToConfigs.put(partition2, config2);
    partitionsToConfigs.put(partition3, config3);
    partitionsToConfigs.put(partition4, config4);
    partitionsToConfigs.put(partition5, config5);
    partitionsToConfigs.put(partition6, config6);
    partitionsToConfigs.put(partition7, config7);
    partitionsToConfigs.put(partition8, config8);

    Map<Partition, Double> perfModel = new HashMap<>();
    perfModel.put(partition1, 1400000000.0);
    perfModel.put(partition2, 3200000000.0);
    perfModel.put(partition3, 2600000000.0);
    perfModel.put(partition4, 7900000000.0);
    perfModel.put(partition5, 1000000000.0);
    perfModel.put(partition6, 0000000000.0);
    perfModel.put(partition7, 3200000000.0);
    perfModel.put(partition8, 8300000000.0);

    LocalPerformanceModel<Partition> localModel =
        new LocalPerformanceModel<>(
            UUID.randomUUID(),
            perfModel,
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>());

    Set<LocalPerformanceModel<Partition>> localModels = new HashSet<>();
    localModels.add(localModel);
    PerformanceModel<Partition> model = new PerformanceModel<>(localModels);

    String programName = "fake";
    List<String> options = new ArrayList<>();
    options.add("A");
    options.add("B");
    options.add("C");

    BasePrettyBuilder<Partition> analysis = new IDTAPrettyBuilder(programName, options, model);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    PerformanceModel<Set<String>> influenceModel = analysis.analyze(args);

    for (Map.Entry<Partition, Set<String>> entry : partitionsToConfigs.entrySet()) {
      double time = influenceModel.evaluate(entry.getValue(), options);
      double expected = perfModel.get(entry.getKey());

      Assert.assertEquals(expected, time, 0.0);
    }
  }
}
