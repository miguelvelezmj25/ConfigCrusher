package edu.cmu.cs.mvelezce.adapter.adapters.regions16;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRegions16Adapter extends BaseAdapter {

  public AbstractRegions16Adapter() {
    this(null, null, null);
  }

  public AbstractRegions16Adapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractRegions16Adapter.getRegions16Options());
  }

  public static List<String> getRegions16Options() {
    String[] options = {"A", "B", "C"};

    return Arrays.asList(options);
  }
}
