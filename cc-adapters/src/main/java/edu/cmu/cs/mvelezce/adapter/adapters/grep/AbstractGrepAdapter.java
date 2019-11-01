package edu.cmu.cs.mvelezce.adapter.adapters.grep;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractGrepAdapter extends BaseAdapter {

  public AbstractGrepAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
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
