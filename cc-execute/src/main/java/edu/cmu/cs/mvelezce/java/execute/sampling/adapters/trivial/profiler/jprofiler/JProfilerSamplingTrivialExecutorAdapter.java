package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.trivial.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.trivial.BaseTrivialAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.adapters.trivial.SamplingTrivialExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAExecutor;

import java.io.IOException;
import java.util.Set;

public class JProfilerSamplingTrivialExecutorAdapter extends SamplingTrivialExecutorAdapter {

  private static final String J_PROFILER_AGENT_PATH =
      "-agentpath:/Applications/JProfiler 10.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib=port=8849,offline,id=115,config=/Users/mvelezce/.jprofiler10/config.xml";

  public JProfilerSamplingTrivialExecutorAdapter(IDTAExecutor executor) {
    super(executor);
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.getExecutor()
        .executeProgram(
            "../" + BaseTrivialAdapter.ORIGINAL_CLASS_PATH,
            BaseTrivialAdapter.MAIN_CLASS,
            J_PROFILER_AGENT_PATH,
            configAsArgs);
  }
}
