package edu.cmu.cs.mvelezce.eval.java.models.constraint;

import de.fosd.typechef.featureexpr.FeatureExpr;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.eval.java.models.ModelsEvaluation;
import edu.cmu.cs.mvelezce.eval.java.models.reader.constraint.ConstraintPerformanceModelsReader;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ConstraintModelsEvaluationTest {

  @Test
  public void berkeleyDB() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    ConstraintPerformanceModelsReader reader = new ConstraintPerformanceModelsReader(programName);
    Set<PerformanceModel<FeatureExpr>> models = reader.read();

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    ModelsEvaluation<FeatureExpr> eval = new ConstraintModelsEvaluation(programName, options, 0.1);
    eval.compare(models);
  }
}
