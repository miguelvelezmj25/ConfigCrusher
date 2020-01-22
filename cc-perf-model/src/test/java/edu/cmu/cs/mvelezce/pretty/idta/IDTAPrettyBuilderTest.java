package edu.cmu.cs.mvelezce.pretty.idta;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.MinConfigsGenerator;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.builder.idta.IDTAPerformanceModelBuilder;
import edu.cmu.cs.mvelezce.model.LocalPerformanceModel;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.pretty.BasePrettyBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class IDTAPrettyBuilderTest {

  @Test
  public void test1() throws IOException, InterruptedException {
    Map<FeatureExpr, Double> perfModel = new HashMap<>();
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("!A && !B && !C"), 1.0);
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("A && !B && !C"), 3.0);
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("!A && B && !C"), 4.0);
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("!A && !B && C"), 5.0);
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("A && B && !C"), 12.0);
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("A && !B && C"), 14.0);
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("!A && B && C"), 18.0);
    perfModel.put(MinConfigsGenerator.parseAsFeatureExpr("A && B && C"), 39.0);

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
    analysis.analyze(args);
  }

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
}
