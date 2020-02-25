package edu.cmu.cs.mvelezce.e2e.execute.adapters.runBenchC;

import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.executor.E2EExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.runBenchC.RunBenchCExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class E2ERunBenchCAdapter extends RunBenchCExecutorAdapter {

  private final E2EExecutor executor;

  public E2ERunBenchCAdapter(E2EExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.preProcess();

    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseRunBenchCAdapter.ORIGINAL_CLASS_PATH,
        BaseRunBenchCAdapter.MAIN_CLASS,
        configAsArgs);
  }
}
