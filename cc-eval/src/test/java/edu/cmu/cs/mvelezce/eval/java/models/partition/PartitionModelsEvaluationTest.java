package edu.cmu.cs.mvelezce.eval.java.models.partition;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.eval.java.models.ModelsEvaluation;
import edu.cmu.cs.mvelezce.eval.java.models.reader.partition.PartitionPerformanceModelsReader;
import edu.cmu.cs.mvelezce.explorer.idta.partition.Partition;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.model.PerformanceModel;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class PartitionModelsEvaluationTest {

  @Test
  public void berkeleyDB_real() throws IOException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    PartitionPerformanceModelsReader reader =
        new PartitionPerformanceModelsReader(programName, BaseExecutor.REAL);
    Set<PerformanceModel<Partition>> models = reader.read();

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    ModelsEvaluation<Partition> eval =
        new PartitionModelsEvaluation(programName, options, BaseExecutor.REAL, 0.1, 0.1);
    eval.compare(models);
  }
}
