package edu.cmu.cs.mvelezce.e2e.execute.time.random;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.compress.BaseCompression;
import edu.cmu.cs.mvelezce.compress.random.exclude.NumberRandomExcludeCompression;
import edu.cmu.cs.mvelezce.java.execute.Executor;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RandomTimeExecutorTest {

  private static Set<Set<String>> getNumberOfConfigs(
      Set<Set<String>> allConfigs, int numberOfConfigs) {
    if (allConfigs.size() < numberOfConfigs) {
      throw new RuntimeException(
          "Cannot get " + numberOfConfigs + " configs when there are only " + allConfigs.size());
    }

    Set<Set<String>> configsToReturn = new HashSet<>();
    Iterator<Set<String>> configsIter = allConfigs.iterator();

    for (int i = 0; i < numberOfConfigs; i++) {
      configsToReturn.add(configsIter.next());
    }

    return configsToReturn;
  }

  @Test
  public void convert_200() throws IOException, InterruptedException {
    String programName = BaseConvertAdapter.PROGRAM_NAME;
    BaseCompression compression = new NumberRandomExcludeCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);
    configs = getNumberOfConfigs(configs, 200);

    Executor executor = new RandomTimeExecutor(programName, configs, 30000);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";
    executor.execute(args);
  }

  @Test
  public void runBenchC_200() throws IOException, InterruptedException {
    String programName = BaseRunBenchCAdapter.PROGRAM_NAME;
    BaseCompression compression = new NumberRandomExcludeCompression(programName);
    String[] args = new String[0];
    Set<Set<String>> configs = compression.analyze(args);
    configs = getNumberOfConfigs(configs, 200);

    Executor executor = new RandomTimeExecutor(programName, configs, 30000);

    args = new String[3];
    args[0] = "-delres";
    args[1] = "-saveres";
    args[2] = "-i5";
    executor.execute(args);
  }
}
