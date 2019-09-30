package edu.cmu.cs.mvelezce.adapter.adapters.guessANumber;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractGuessANumberAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "GuessANumber";
  public static final String MAIN_CLASS = "edu.cmu.cs.mvelezce.GuessANumber";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/phosphor-examples/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/phosphor-examples/target/classes";

  private static final String[] OPTIONS = {"A", "B", "C"};

  public AbstractGuessANumberAdapter() {
    // TODO check that we are passing empty string
    super(
        AbstractGuessANumberAdapter.PROGRAM_NAME,
        AbstractGuessANumberAdapter.MAIN_CLASS,
        "",
        AbstractGuessANumberAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(AbstractGuessANumberAdapter.OPTIONS);
  }
}
