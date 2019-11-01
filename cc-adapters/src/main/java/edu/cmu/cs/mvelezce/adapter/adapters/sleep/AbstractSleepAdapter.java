package edu.cmu.cs.mvelezce.adapter.adapters.sleep;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/** Created by miguelvelez on 4/30/17. */
public abstract class AbstractSleepAdapter extends BaseAdapter {

  public AbstractSleepAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  private static List<String> getSleepOptions() {
    String[] options = {"A", "B", "C", "D", "IA", "DA"};

    return Arrays.asList(options);
  }
}
