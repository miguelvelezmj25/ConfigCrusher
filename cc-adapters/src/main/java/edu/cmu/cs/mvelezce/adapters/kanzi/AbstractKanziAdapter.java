package edu.cmu.cs.mvelezce.adapters.kanzi;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractKanziAdapter extends BaseAdapter {

  public AbstractKanziAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getKanziOptions() {
    String[] options = {
      "VERBOSE", "FORCE", "BLOCKSIZE", "LEVEL", "ENTROPY", "TRANSFORM", "CHECKSUM"
    };

    return Arrays.asList(options);
  }
}
