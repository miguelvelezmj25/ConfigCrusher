package edu.cmu.cs.mvelezce.adapter.adapters.colorCounter;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractColorCounterAdapter extends BaseAdapter {

  public AbstractColorCounterAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getColorCounterOptions() {
    String[] options = {"FREQTHRESHOLD", "DISTTHRESHOLD", "MINALPHA", "TIMEOUT", "LOGLEVEL"};

    return Arrays.asList(options);
  }
}
