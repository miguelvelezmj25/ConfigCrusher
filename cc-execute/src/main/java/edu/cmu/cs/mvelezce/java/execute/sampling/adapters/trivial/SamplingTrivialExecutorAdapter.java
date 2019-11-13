package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.trivial;

import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.java.execute.Executor;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;

import java.io.IOException;
import java.util.Set;

public class SamplingTrivialExecutorAdapter extends BaseTrivialAdapter implements ExecutorAdapter {

  private final Executor executor;

  public SamplingTrivialExecutorAdapter(Executor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    throw new UnsupportedOperationException("Implement");
  }
}
