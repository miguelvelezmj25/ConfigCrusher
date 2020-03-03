package edu.cmu.cs.mvelezce.compress.gt;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GTCompressionTest {

  @Test
  public void berkeleyDb() throws IOException, InterruptedException {
    String programName = BaseMeasureDiskOrderedScanAdapter.PROGRAM_NAME;
    List<String> options = BaseMeasureDiskOrderedScanAdapter.getListOfOptions();
    BaseCompression compression = new GTCompression(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }

  @Test
  public void lucene() throws IOException, InterruptedException {
    String programName = BaseIndexFilesAdapter.PROGRAM_NAME;
    List<String> options = BaseIndexFilesAdapter.getListOfOptions();
    BaseCompression compression = new GTCompression(programName, options, 2000);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }

  @Test
  public void convert() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    List<String> options = BaseConvertAdapter.getListOfOptions();
    BaseCompression compression = new GTCompression(programName, options, 2000);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }

  @Test
  public void multithread() throws IOException, InterruptedException {
    String programName = BaseMultithreadAdapter.PROGRAM_NAME;
    List<String> options = BaseMultithreadAdapter.getListOfOptions();
    BaseCompression compression = new GTCompression(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }

  @Test
  public void runBenchC() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    List<String> options = BaseRunBenchCAdapter.getListOfOptions();
    BaseCompression compression = new GTCompression(programName, options);

    String[] args = new String[2];
    args[0] = "-delres";
    args[1] = "-saveres";
    Set<Set<String>> configs = compression.analyze(args);
    System.out.println("Configs " + configs.size());
  }
}
