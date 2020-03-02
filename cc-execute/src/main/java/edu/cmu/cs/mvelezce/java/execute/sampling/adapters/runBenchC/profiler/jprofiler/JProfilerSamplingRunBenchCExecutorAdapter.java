package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.runBenchC.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.runBenchC.BaseRunBenchCAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.runBenchC.RunBenchCExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;

import java.io.IOException;
import java.util.Set;

public class JProfilerSamplingRunBenchCExecutorAdapter extends RunBenchCExecutorAdapter {

  private static final String J_PROFILER_AGENT_PATH_UBUNTU =
      "-agentpath:/home/miguel/jprofiler10/bin/linux-x64/libjprofilerti.so=port=8849,offline,id=109,config=/home/miguel/.jprofiler10/config.xml";
  private static final String J_PROFILER_AGENT_PATH_OSX =
      "-agentpath:/Applications/JProfiler 10.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib=port=8849,offline,id=133,config=/Users/mvelezce/.jprofiler10/config.xml";

  private final IDTAJProfilerSamplingExecutor executor;

  public JProfilerSamplingRunBenchCExecutorAdapter(IDTAJProfilerSamplingExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.preProcess();

    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseRunBenchCAdapter.ORIGINAL_CLASS_PATH,
        BaseRunBenchCAdapter.MAIN_CLASS,
        this.getAgentPath(),
        configAsArgs);
  }

  private String getAgentPath() {
    String os = IDTAJProfilerSamplingExecutor.getOS();

    switch (os) {
      case IDTAJProfilerSamplingExecutor.LINUX:
        return J_PROFILER_AGENT_PATH_UBUNTU;
      case IDTAJProfilerSamplingExecutor.MAC_OS_X:
        return J_PROFILER_AGENT_PATH_OSX;
      default:
        throw new RuntimeException("Do not have an agent path for " + os);
    }
  }
}
