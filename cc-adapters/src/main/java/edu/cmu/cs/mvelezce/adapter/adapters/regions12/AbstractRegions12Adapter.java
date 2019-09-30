package edu.cmu.cs.mvelezce.adapter.adapters.regions12;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRegions12Adapter extends BaseAdapter {

  public AbstractRegions12Adapter() {
    this(null, null, null);
  }

  public AbstractRegions12Adapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractRegions12Adapter.getRegions12Options());
  }

  public static List<String> getRegions12Options() {
    String[] options = {"A", "B", "C"};

    return Arrays.asList(options);
  }
}
