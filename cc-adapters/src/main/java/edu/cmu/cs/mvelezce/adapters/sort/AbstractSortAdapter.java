package edu.cmu.cs.mvelezce.adapters.sort;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSortAdapter extends BaseAdapter {

  public AbstractSortAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getSortOptions() {
    String[] options = {
      "CHECK",
      "MERGE",
      "UNIQUE",
      "IGNORELEADINGBLANKS",
      "DICTIONARYORDER",
      "IGNORECASE",
      "NUMBERICSORT",
      "GENERALNUMRICSORT",
      "HUMANNUMERICSORT",
      "MONTHSORT",
      "VERSIONSORT",
      "REVERSE"
    };

    return Arrays.asList(options);
  }
}
