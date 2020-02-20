package edu.cmu.cs.mvelezce.e2e.execute.adapters.multithread;

import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.executor.E2EExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.multithread.MultithreadExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class E2EMultithreadAdapter extends MultithreadExecutorAdapter {

  private final E2EExecutor executor;

  public E2EMultithreadAdapter(E2EExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseMultithreadAdapter.ORIGINAL_CLASS_PATH,
        ExecutorAdapter.EXECUTOR_MAIN_CLASS_PREFIX + BaseMultithreadAdapter.PROGRAM_NAME,
        configAsArgs);
  }
}
