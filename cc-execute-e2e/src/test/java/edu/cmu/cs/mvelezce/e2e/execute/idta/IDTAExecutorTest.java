package edu.cmu.cs.mvelezce.e2e.execute.idta;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions.IDTASuboptimalGreedyConjunctionsCompression;
import edu.cmu.cs.mvelezce.java.execute.Executor;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class IDTAExecutorTest {

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);
    Executor executor = new IDTAExecutor(programName, configs, 10000);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";
    executor.execute(args);
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);
    Executor executor = new IDTAExecutor(programName, configs, 10000);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";
    executor.execute(args);
  }
}
