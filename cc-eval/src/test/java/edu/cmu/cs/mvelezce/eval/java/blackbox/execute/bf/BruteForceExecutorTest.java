package edu.cmu.cs.mvelezce.eval.java.blackbox.execute.bf;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.Executor;
import edu.cmu.cs.mvelezce.utils.configurations.ConfigHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BruteForceExecutorTest {

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    Set<Set<String>> configurations = ConfigHelper.getConfigurations(options);
    configurations.clear();
    configurations.add(new HashSet<>());

    Executor executor = new BruteForceExecutor(programName, configurations);

    String[] args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i2";

    executor.execute(args);
  }
}
