package edu.cmu.cs.mvelezce.adapter.adapters.email;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/** Created by miguelvelez on 4/30/17. */
public abstract class AbstractEmailAdapter extends BaseAdapter {

  public AbstractEmailAdapter() {
    this(null, null, null);
  }

  public AbstractEmailAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractEmailAdapter.getEmailOptions());
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
