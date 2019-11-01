package edu.cmu.cs.mvelezce.adapter.adapters.pngtastic;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

public class BasePngtasticAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "counter";
  public static final String MAIN_CLASS = "counter.com.googlecode.pngtastic.Run";
  public static final String ROOT_PACKAGE = "counter.com.googlecode.pngtastic";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/pngtastic-counter/target/classes";
  public static final String INSTRUMENTED_DIR_PATH =
      "../performance-mapper-evaluation/instrumented/pngtastic-counter";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/pngtastic-counter/target/classes";

  private static final String[] OPTIONS = {
    "FREQTHRESHOLD", "DISTTHRESHOLD", "MINALPHA", "TIMEOUT", "LOGLEVEL"
  };

  public BasePngtasticAdapter() {
    // TODO check why are we passing empty string. Empty string represents the directory of the
    // class files to execute.
    super(
        BasePngtasticAdapter.PROGRAM_NAME,
        BasePngtasticAdapter.MAIN_CLASS,
        BasePngtasticAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(BasePngtasticAdapter.OPTIONS);
  }
}
