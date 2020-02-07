package edu.cmu.cs.mvelezce.compress.random.from.idta.fw;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.idta.suboptimal.greedy.conjunctions.IDTASuboptimalGreedyConjunctionsCompression;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class RandomFWFromIDTACompressionTest {

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BaseCompression compression = new IDTASuboptimalGreedyConjunctionsCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> idtaConfigs = compression.analyze(args);
    compression = new RandomFWFromIDTACompression(programName, options, idtaConfigs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }
}
