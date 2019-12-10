package edu.cmu.cs.mvelezce.e2e.execute.adapters.indexFiles;

import edu.cmu.cs.mvelezce.adapters.indexFiles.BaseIndexFilesAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.E2EExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.indexFiles.IndexFilesExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class E2EIndexFilesAdapter extends IndexFilesExecutorAdapter {

  private final E2EExecutor executor;

  public E2EIndexFilesAdapter(E2EExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.preProcess();

    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseIndexFilesAdapter.ORIGINAL_CLASS_PATH,
        ExecutorAdapter.EXECUTOR_MAIN_CLASS_PREFIX + BaseIndexFilesAdapter.PROGRAM_NAME,
        configAsArgs);
  }
}
