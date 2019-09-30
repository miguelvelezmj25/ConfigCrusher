package edu.cmu.cs.mvelezce.adapter.adapters.sort;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSortAdapter extends BaseAdapter {

  public AbstractSortAdapter() {
    this(null, null, null);
  }

  public AbstractSortAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractSortAdapter.getSortOptions());
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
