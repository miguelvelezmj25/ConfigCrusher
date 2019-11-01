package edu.cmu.cs.mvelezce.adapter.adapters.elevator;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/** Created by miguelvelez on 4/30/17. */
public abstract class AbstractElevatorAdapter extends BaseAdapter {

  public AbstractElevatorAdapter(String programName, String directory, List<String> options) {
    super(programName, directory, options);
  }

  public static List<String> getElevatorOptions() {
    String[] options = {"BASE", "OVERLOADED", "WEIGHT", "EMPTY", "TWOTHIRDSFULL", "EXECUTIVEFLOOR"};

    return Arrays.asList(options);
  }
}
