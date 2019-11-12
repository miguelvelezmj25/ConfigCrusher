package edu.cmu.cs.mvelezce.adapters.berkeley;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractBerkeleyAdapter extends BaseAdapter {

  public AbstractBerkeleyAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getBerkeleyOptions() {
    String[] options = {"RECORDS", "DATA", "DUPLICATES", "KEYSIZE", "SEQUENTIAL"};

    return Arrays.asList(options);
  }
}
