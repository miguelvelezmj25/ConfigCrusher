package edu.cmu.cs.mvelezce.java.execute.sampling.adapters.measureDiskOrderedScan.profiler.jprofiler;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.adapters.measureDiskOrderedScan.SamplingMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.sampling.idta.profiler.jprofiler.IDTAJProfilerSamplingExecutor;

import java.io.IOException;
import java.util.Set;

public class JProfilerSamplingMeasureDiskOrderedScanAdapter
    extends SamplingMeasureDiskOrderedScanAdapter {

  private static final String J_PROFILER_AGENT_PATH =
      "-agentpath:/Applications/JProfiler 10.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib=port=8849,offline,id=116,config=/Users/mvelezce/.jprofiler10/config.xml";

  public JProfilerSamplingMeasureDiskOrderedScanAdapter(IDTAJProfilerSamplingExecutor executor) {
    super(executor);
  }

  @Override
  public void execute(Set<String> configuration) throws IOException, InterruptedException {
    this.preProcess("../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_ROOT_DIR);

    String[] configAsArgs = this.configurationAsMainArguments(configuration);
    this.getExecutor()
        .executeProgram(
            "../" + BaseMeasureDiskOrderedScanAdapter.ORIGINAL_CLASS_PATH,
            BaseMeasureDiskOrderedScanAdapter.MAIN_CLASS,
            J_PROFILER_AGENT_PATH,
            configAsArgs);
  }
}
