package edu.cmu.cs.mvelezce.compress.random.from.gt.pw;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.gt.GTCompression;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class RandomPWFromGTCompressionTest {

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> gtConfigs = compression.analyze(args);
    compression = new RandomPWFromGTCompression(programName, options, gtConfigs);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }
}
