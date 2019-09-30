package edu.cmu.cs.mvelezce.adapter.adapters.berkeley;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractBerkeleyAdapter extends BaseAdapter {

  public AbstractBerkeleyAdapter() {
    this(null, null, null);
  }

  public AbstractBerkeleyAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractBerkeleyAdapter.getBerkeleyOptions());
  }

  public static List<String> getBerkeleyOptions() {
    String[] options = {"RECORDS", "DATA", "DUPLICATES", "KEYSIZE", "SEQUENTIAL"};

    return Arrays.asList(options);
  }
}
