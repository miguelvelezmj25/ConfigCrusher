package edu.cmu.cs.mvelezce.compress.random.exclude;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.gt.GTCompression;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class NumberRandomExcludeCompressionTest {

  @Test
  public void berkeleyDb_200() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> gtConfigs = compression.analyze(args);

    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    compression = new NumberRandomExcludeCompression(programName, options, gtConfigs, 200);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }

  @Test
  public void lucene_200() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> gtConfigs = compression.analyze(args);

    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    compression = new NumberRandomExcludeCompression(programName, options, gtConfigs, 200);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }

  @Test
  public void convert_200() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> gtConfigs = compression.analyze(args);

    List<String> options = BaseConvertAdapter.getListOfOptions();
    compression = new NumberRandomExcludeCompression(programName, options, gtConfigs, 200);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }

  @Test
  public void runBenchC_200() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression compression = new GTCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> gtConfigs = compression.analyze(args);

    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    compression = new NumberRandomExcludeCompression(programName, options, gtConfigs, 200);

    args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }
}
