package edu.cmu.cs.mvelezce.java.execute.adapters.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.adapters.measureDiskOrderedScan.BaseMeasureDiskOrderedScanAdapter;
import edu.cmu.cs.mvelezce.java.execute.adapters.ExecutorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class MeasureDiskOrderedScanExecutorAdapter
    extends BaseMeasureDiskOrderedScanAdapter implements ExecutorAdapter {

  protected String[] padOptions(Set<String> configuration) {
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

    return tmp.toArray(new String[0]);
  }
}
