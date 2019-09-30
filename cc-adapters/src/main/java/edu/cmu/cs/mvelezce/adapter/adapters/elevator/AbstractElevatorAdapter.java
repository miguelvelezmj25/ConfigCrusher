package edu.cmu.cs.mvelezce.adapter.adapters.elevator;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/** Created by miguelvelez on 4/30/17. */
public abstract class AbstractElevatorAdapter extends BaseAdapter {

  public AbstractElevatorAdapter() {
    this(null, null, null);
  }

  public AbstractElevatorAdapter(String programName, String entryPoint, String dir) {
    super(programName, entryPoint, dir, AbstractElevatorAdapter.getElevatorOptions());
  }

  public static List<String> getElevatorOptions() {
    String[] options = {"BASE", "OVERLOADED", "WEIGHT", "EMPTY", "TWOTHIRDSFULL", "EXECUTIVEFLOOR"};

    return Arrays.asList(options);
  }
}
