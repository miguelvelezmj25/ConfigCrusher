package edu.cmu.cs.mvelezce.adapter.adapters.colorCounter;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractColorCounterAdapter extends BaseAdapter {

  public AbstractColorCounterAdapter() {
    this(null, null, null);
  }

  public AbstractColorCounterAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractColorCounterAdapter.getColorCounterOptions());
  }

  public static List<String> getColorCounterOptions() {
    String[] options = {"FREQTHRESHOLD", "DISTTHRESHOLD", "MINALPHA", "TIMEOUT", "LOGLEVEL"};

    return Arrays.asList(options);
  }
}
