package edu.cmu.cs.mvelezce.pretty.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.builder.idta.IDTAPerformanceModelBuilder;
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
    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new IDTAPerformanceModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BasePrettyBuilder<FeatureExpr> analysis = new IDTAPrettyBuilder(programName, options, model);
    analysis.analyze(args);
  }

  @Test
  public void test1() throws IOException, InterruptedException {
    FeatureExpr constraint1 = MinConfigsGenerator.parseAsFeatureExpr("!A && !B && !C");
    FeatureExpr constraint2 = MinConfigsGenerator.parseAsFeatureExpr("A && !B && !C");
    FeatureExpr constraint3 = MinConfigsGenerator.parseAsFeatureExpr("!A && B && !C");
    FeatureExpr constraint4 = MinConfigsGenerator.parseAsFeatureExpr("!A && !B && C");
    FeatureExpr constraint5 = MinConfigsGenerator.parseAsFeatureExpr("A && B && !C");
    FeatureExpr constraint6 = MinConfigsGenerator.parseAsFeatureExpr("A && !B && C");
    FeatureExpr constraint7 = MinConfigsGenerator.parseAsFeatureExpr("!A && B && C");
    FeatureExpr constraint8 = MinConfigsGenerator.parseAsFeatureExpr("A && B && C");

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

    Map<FeatureExpr, Set<String>> constraintsToConfigs = new HashMap<>();
    constraintsToConfigs.put(constraint1, config1);
    constraintsToConfigs.put(constraint2, config2);
    constraintsToConfigs.put(constraint3, config3);
    constraintsToConfigs.put(constraint4, config4);
    constraintsToConfigs.put(constraint5, config5);
    constraintsToConfigs.put(constraint6, config6);
    constraintsToConfigs.put(constraint7, config7);
    constraintsToConfigs.put(constraint8, config8);

    Map<FeatureExpr, Double> perfModel = new HashMap<>();
    perfModel.put(constraint1, 1400000000.0);
    perfModel.put(constraint2, 3200000000.0);
    perfModel.put(constraint3, 2600000000.0);
    perfModel.put(constraint4, 7900000000.0);
    perfModel.put(constraint5, 1000000000.0);
    perfModel.put(constraint6, 0000000000.0);
    perfModel.put(constraint7, 3200000000.0);
    perfModel.put(constraint8, 8300000000.0);

    LocalPerformanceModel<FeatureExpr> localModel =
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

    Set<LocalPerformanceModel<FeatureExpr>> localModels = new HashSet<>();
    localModels.add(localModel);
    PerformanceModel<FeatureExpr> model = new PerformanceModel<>(localModels);

    String programName = "fake";
    List<String> options = new ArrayList<>();
    options.add("A");
    options.add("B");
    options.add("C");

    BasePrettyBuilder<FeatureExpr> analysis = new IDTAPrettyBuilder(programName, options, model);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    PerformanceModel<Set<String>> influenceModel = analysis.analyze(args);

    for (Map.Entry<FeatureExpr, Set<String>> entry : constraintsToConfigs.entrySet()) {
      double time = influenceModel.evaluate(entry.getValue(), options);
      double expected = perfModel.get(entry.getKey());

      Assert.assertEquals(expected, time, 0.0);
    }
  }
}
