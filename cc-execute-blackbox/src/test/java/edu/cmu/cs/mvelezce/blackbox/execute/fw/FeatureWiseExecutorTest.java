package edu.cmu.cs.mvelezce.blackbox.execute.fw;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.Executor;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FeatureWiseExecutorTest {

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configurations = FeatureWiseExecutor.getConfigs(options);
    Executor executor = new FeatureWiseExecutor(programName, configurations);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";

    executor.execute(args);
  }
}
