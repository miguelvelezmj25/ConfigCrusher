package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.multithread.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.multithread.BaseMultithreadAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.multithread.MultithreadExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;

import java.io.IOException;
import java.util.Set;

public class JProfilerSamplingMultithreadExecutorAdapter extends MultithreadExecutorAdapter {

  private static final String J_PROFILER_AGENT_PATH_UBUNTU =
      "-agentpath:/home/miguel/jprofiler10/bin/linux-x64/libjprofilerti.so=port=8849,offline,id=107,config=/home/miguel/.jprofiler10/config.xml";
  private static final String J_PROFILER_AGENT_PATH_OSX =
      "-agentpath:/Applications/JProfiler 10.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib=port=8849,offline,id=132,config=/Users/mvelezce/.jprofiler10/config.xml";

  private final IDTAJProfilerSamplingExecutor executor;

  public JProfilerSamplingMultithreadExecutorAdapter(IDTAJProfilerSamplingExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.executor.executeProgram(
        "../" + BaseMultithreadAdapter.ORIGINAL_CLASS_PATH,
        BaseMultithreadAdapter.MAIN_CLASS,
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
