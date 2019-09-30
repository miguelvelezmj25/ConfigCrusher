package edu.cmu.cs.mvelezce.adapter.adapters.regions14;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRegions14Adapter extends BaseAdapter {

  public AbstractRegions14Adapter() {
    this(null, null, null);
  }

  public AbstractRegions14Adapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractRegions14Adapter.getRegions14Options());
  }

  public static List<String> getRegions14Options() {
    String[] options = {"A", "B", "C"};

    return Arrays.asList(options);
  }
}
