package edu.cmu.cs.mvelezce.eval.java;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.blackbox.perfmodel.bf.BruteForcePerformanceModelBuilder;
import edu.cmu.cs.mvelezce.eval.java.constraint.ConstraintEvaluation;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class EvaluationTest {

  @Test
  public void berkeleyDB_BF_Data() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configs = ConfigHelper.getConfigurations(options);

    BaseAnalysis<PerformanceModel<FeatureExpr>> builder =
        new BruteForcePerformanceModelBuilder(programName);
    String[] args = new String[0];
    PerformanceModel<FeatureExpr> model = builder.analyze(args);

    Evaluation<FeatureExpr> eval = new ConstraintEvaluation(programName, options);
    eval.saveConfigsToPerformance(Evaluation.BF, configs, model);
  }
}
