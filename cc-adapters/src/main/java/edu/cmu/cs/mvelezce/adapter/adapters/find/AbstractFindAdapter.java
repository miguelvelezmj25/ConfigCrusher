package edu.cmu.cs.mvelezce.adapter.adapters.find;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractFindAdapter extends BaseAdapter {

  public AbstractFindAdapter() {
    this(null, null, null);
  }

  public AbstractFindAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractFindAdapter.getFindOptions());
  }

  public static List<String> getFindOptions() {
    String[] options = {
      "ISTYPEDIRECTORY",
      "ISTYPEFILE",
      "ISTYPESYMLINK",
      "ISTYPEOTHER",
      "ISREGEX",
      "ISIGNORECASE",
      "ISTIMENEWER",
      "ISTIMEOLDER",
      "ISTIMECREATE",
      "ISTIMEACCESS",
      "ISTIMEMODIFIED",
      "ISPRINT0"
    };

    return Arrays.asList(options);
  }
}
