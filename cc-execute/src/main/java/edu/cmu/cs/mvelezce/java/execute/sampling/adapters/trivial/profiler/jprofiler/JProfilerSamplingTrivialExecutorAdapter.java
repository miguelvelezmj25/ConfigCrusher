package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.trivial.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.trivial.TrivialExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;

import java.io.IOException;
import java.util.Set;

public class JProfilerSamplingTrivialExecutorAdapter extends TrivialExecutorAdapter {

  private static final String J_PROFILER_AGENT_PATH =
      "-agentpath:/Applications/JProfiler 10.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib=port=8849,offline,id=115,config=/Users/mvelezce/.jprofiler10/config.xml";

  private final IDTAJProfilerSamplingExecutor executor;

  public JProfilerSamplingTrivialExecutorAdapter(IDTAJProfilerSamplingExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseTrivialAdapter.ORIGINAL_CLASS_PATH,
        BaseTrivialAdapter.MAIN_CLASS,
        J_PROFILER_AGENT_PATH,
        configAsArgs);
  }
}
