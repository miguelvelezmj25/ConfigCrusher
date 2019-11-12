package edu.cmu.cs.mvelezce.adapters.find;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractFindAdapter extends BaseAdapter {

  public AbstractFindAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
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
