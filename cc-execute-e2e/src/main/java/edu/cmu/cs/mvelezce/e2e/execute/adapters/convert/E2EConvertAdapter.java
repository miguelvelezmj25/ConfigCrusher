package edu.cmu.cs.mvelezce.e2e.execute.adapters.convert;

import edu.cmu.cs.mvelezce.adapters.convert.BaseConvertAdapter;
import edu.cmu.cs.mvelezce.e2e.execute.executor.E2EExecutor;
import edu.cmu.cs.mvelezce.java.execute.adapters.convert.ConvertExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class E2EConvertAdapter extends ConvertExecutorAdapter {

  private final E2EExecutor executor;

  public E2EConvertAdapter(E2EExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.preProcess();

    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseConvertAdapter.ORIGINAL_CLASS_PATH,
        BaseConvertAdapter.MAIN_CLASS,
        configAsArgs);
  }
}
