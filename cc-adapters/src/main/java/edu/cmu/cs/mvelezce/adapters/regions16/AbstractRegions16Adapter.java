package edu.cmu.cs.mvelezce.adapters.regions16;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRegions16Adapter extends BaseAdapter {

  public AbstractRegions16Adapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getRegions16Options() {
    String[] options = {"A", "B", "C"};

    return Arrays.asList(options);
  }
}
