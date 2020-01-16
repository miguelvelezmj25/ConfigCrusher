package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.measureDiskOrderedScan.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.measureDiskOrderedScan.MeasureDiskOrderedScanExecutorAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class JProfilerSamplingMeasureDiskOrderedScanAdapter
    extends MeasureDiskOrderedScanExecutorAdapter {

  private static final String J_PROFILER_AGENT_PATH_UBUNTU =
      "-agentpath:/home/miguel/jprofiler10/bin/linux-x64/libjprofilerti.so=port=8849,offline,id=101,config=/home/miguel/.jprofiler10/config.xml";
  private static final String J_PROFILER_AGENT_PATH_OSX =
      "-agentpath:/Applications/JProfiler 10.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib=port=8849,offline,id=116,config=/Users/mvelezce/.jprofiler10/config.xml";

  private final IDTAJProfilerSamplingExecutor executor;

  public JProfilerSamplingMeasureDiskOrderedScanAdapter(IDTAJProfilerSamplingExecutor executor) {
    this.executor = executor;
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.preProcess("../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR);

    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    List<String> tmp = new ArrayList<>();

    for (int i = 0; i < 16; i++) {
      if (i == 0) {
        tmp.add(configAsArgs[0]);
      } else if (i == 1) {
        tmp.add(configAsArgs[1]);
      } else if (i == 4) {
        tmp.add(configAsArgs[2]);
      } else {
        tmp.add("false");
      }
    }

    configAsArgs = tmp.toArray(new String[0]);

    this.executor.executeProgram(
        "../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_CLASS_PATH,
        BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS,
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
