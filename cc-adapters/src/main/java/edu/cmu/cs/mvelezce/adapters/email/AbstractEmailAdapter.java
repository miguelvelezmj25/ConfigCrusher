package edu.cmu.cs.mvelezce.adapters.email;

import edu.cmu.cs.mvelezce.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/** Created by miguelvelez on 4/30/17. */
public abstract class AbstractEmailAdapter extends BaseAdapter {

  public AbstractEmailAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getEmailOptions() {
    String[] options = {
      "BASE",
      "KEYS",
      "ENCRYPT",
      "AUTORESPONDER",
      "ADDRESSBOOK",
      "SIGN",
      "FORWARD",
      "VERIFY",
      "DECRYPT"
    };

    return Arrays.asList(options);
  }
}
