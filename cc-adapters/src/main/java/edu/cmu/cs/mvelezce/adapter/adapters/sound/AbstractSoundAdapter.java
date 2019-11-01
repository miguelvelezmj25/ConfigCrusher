package edu.cmu.cs.mvelezce.adapter.adapters.sound;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractSoundAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "sound";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.analysis.Sound";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B"};

  public AbstractSoundAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractSoundAdapter.PROGRAM_NAME,
        AbstractSoundAdapter.MAIN_CLASS,
        AbstractSoundAdapter.getListOfOptions());
  }

  // TODO abstract method in base adapter?
  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractSoundAdapter.OPTIONS);
  }
}
