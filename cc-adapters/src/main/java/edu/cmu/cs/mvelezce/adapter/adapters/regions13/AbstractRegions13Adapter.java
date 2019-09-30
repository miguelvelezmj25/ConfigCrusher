package edu.cmu.cs.mvelezce.adapter.adapters.regions13;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRegions13Adapter extends BaseAdapter {

  public AbstractRegions13Adapter() {
    this(null, null, null);
  }

  public AbstractRegions13Adapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractRegions13Adapter.getRegions13Options());
  }

  public static List<String> getRegions13Options() {
    String[] options = {"A", "B", "C"};

    return Arrays.asList(options);
  }
}
