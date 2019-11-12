package edu.cmu.cs.mvelezce.adapters.regions13;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRegions13Adapter extends BaseAdapter {

  public AbstractRegions13Adapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getRegions13Options() {
    String[] options = {"A", "B", "C"};

    return Arrays.asList(options);
  }
}
