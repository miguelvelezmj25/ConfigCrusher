package edu.cmu.cs.mvelezce.eval.java.influence;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.eval.java.influence.reader.InfluencePerformanceModelsReader;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class InfluenceEvaluationTest {

  @Test
  public void berkeleyDB() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    InfluencePerformanceModelsReader reader = new InfluencePerformanceModelsReader(programName);
    List<PerformanceModel<Set<String>>> models = reader.read();

    InfluenceEvaluation eval =
        new InfluenceEvaluation(programName, models.get(0), models.get(1), 0.1, 0.1);
    eval.compare();
  }
}
