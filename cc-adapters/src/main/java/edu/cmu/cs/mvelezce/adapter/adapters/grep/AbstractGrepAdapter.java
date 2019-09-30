package edu.cmu.cs.mvelezce.adapter.adapters.grep;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractGrepAdapter extends BaseAdapter {

  public AbstractGrepAdapter() {
    this(null, null, null);
  }

  public AbstractGrepAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractGrepAdapter.getGrepOptions());
  }

  public static List<String> getGrepOptions() {
    String[] options = {
      "IGNORECASE",
      "INVERTMATCH",
      "FIXEDSTRINGS",
      "LINENUMBER",
      "COUNT",
      "MATCHINGFILES",
      "WHOLELINE"
    };

    return Arrays.asList(options);
  }
}
