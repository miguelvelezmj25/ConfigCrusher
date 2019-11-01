package edu.cmu.cs.mvelezce.adapter.adapters.runningexample;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRunningExampleAdapter extends BaseAdapter {

  public AbstractRunningExampleAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getRunningExampleOptions() {
    String[] options = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    return Arrays.asList(options);
  }
}
