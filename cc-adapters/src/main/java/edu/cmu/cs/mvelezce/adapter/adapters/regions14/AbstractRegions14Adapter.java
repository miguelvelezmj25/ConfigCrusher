package edu.cmu.cs.mvelezce.adapter.adapters.regions14;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRegions14Adapter extends BaseAdapter {

  public AbstractRegions14Adapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getRegions14Options() {
    String[] options = {"A", "B", "C"};

    return Arrays.asList(options);
  }
}
