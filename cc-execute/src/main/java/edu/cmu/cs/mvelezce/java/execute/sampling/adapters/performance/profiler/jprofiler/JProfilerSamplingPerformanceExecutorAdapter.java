package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.performance.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.performance.BasePerformanceAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.performance.PerformanceExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;

import java.io.IOException;
import java.util.Set;

public class JProfilerSamplingPerformanceExecutorAdapter extends PerformanceExecutorAdapter {

  private static final String J_PROFILER_AGENT_PATH =
      "-agentpath:/Applications/JProfiler 10.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib=port=8849,offline,id=123,config=/Users/mvelezce/.jprofiler10/config.xml";

  private final IDTAJProfilerSamplingExecutor executor;

  public JProfilerSamplingPerformanceExecutorAdapter(IDTAJProfilerSamplingExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BasePerformanceAdapter.ORIGINAL_CLASS_PATH,
        BasePerformanceAdapter.MAIN_CLASS,
        J_PROFILER_AGENT_PATH,
        configAsArgs);
  }
}
