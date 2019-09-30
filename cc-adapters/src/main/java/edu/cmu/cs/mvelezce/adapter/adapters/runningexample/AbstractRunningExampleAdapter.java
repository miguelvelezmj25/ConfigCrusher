package edu.cmu.cs.mvelezce.adapter.adapters.runningexample;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRunningExampleAdapter extends BaseAdapter {

  public AbstractRunningExampleAdapter() {
    this(null, null, null);
  }

  public AbstractRunningExampleAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractRunningExampleAdapter.getRunningExampleOptions());
  }

  public static List<String> getRunningExampleOptions() {
    String[] options = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    return Arrays.asList(options);
  }
}
