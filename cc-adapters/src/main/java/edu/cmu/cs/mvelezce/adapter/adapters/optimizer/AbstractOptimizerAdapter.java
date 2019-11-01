package edu.cmu.cs.mvelezce.adapter.adapters.optimizer;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractOptimizerAdapter extends BaseAdapter {

  public AbstractOptimizerAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getOptimizerOptions() {
    String[] options = {"COMPRESSOR", "ITERATIONS", "LOGLEVEL", "COMPRESSIONLEVEL", "REMOVEGAMMA"};

    return Arrays.asList(options);
  }
}
