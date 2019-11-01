package edu.cmu.cs.mvelezce.adapter.adapters.prevayler;

import edu.cmu.cs.mvelezce.adapter.adapters.BaseAdapter;

import java.util.Arrays;
import java.util.List;

import static edu.cmu.cs.mvelezce.utils.execute.Executor.PATH_SEPARATOR;
import static edu.cmu.cs.mvelezce.utils.execute.Executor.USER_HOME;

public abstract class AbstractPrevaylerAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "prevayler";
  public static final String MAIN_CLASS = "org.prevayler.demos.demo1.PrimeNumbers";
  public static final String ORIGINAL_CLASS_PATH =
      "../performance-mapper-evaluation/original/prevayler/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH =
      "../performance-mapper-evaluation/instrumented/prevayler/target/classes";
  public static final String CLASS_PATH =
      USER_HOME
          + "/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar"
          + PATH_SEPARATOR
          + USER_HOME
          + "/.m2/repository/log4j/log4j/1.2.15/log4j-1.2.15.jar"
          + PATH_SEPARATOR
          + USER_HOME
          + "/.m2/repository/javax/mail/mail/1.4/mail-1.4.jar"
          + PATH_SEPARATOR
          + USER_HOME
          + "/.m2/repository/javax/activation/activation/1.1/activation-1.1.jar"
          + PATH_SEPARATOR
          + USER_HOME
          + "/.m2/repository/com/thoughtworks/xstream/xstream/1.4.5/xstream-1.4.5.jar"
          + PATH_SEPARATOR
          + USER_HOME
          + "/.m2/repository/xmlpull/xmlpull/1.1.3.1/xmlpull-1.1.3.1.jar"
          + PATH_SEPARATOR
          + USER_HOME
          + "/.m2/repository/xpp3/xpp3_min/1.1.4c/xpp3_min-1.1.4c.jar";

  private static final String[] OPTIONS = {
    "TRANSIENTMODE", "CLOCK", "DEEPCOPY", "DISKSYNC"
  }; // , "FILESIZETHRESHOLD", "FILEAGETHRESHOLD", "MONITOR", "JOURNALSERIALIZER",
  // "SNAPSHOTSERIALIZER"};

  //  public PrevaylerAdapter() {
  //    this(null, null, null);
  //  }

  public AbstractPrevaylerAdapter() {
    // TODO check that we are passing empty string
    super(PROGRAM_NAME, MAIN_CLASS, getListOfOptions());
  }

  public static List<String> getPrevaylerOptions() {
    String[] options = {
      "TRANSIENTMODE", "CLOCK", "DEEPCOPY", "DISKSYNC"
    }; // , "FILESIZETHRESHOLD", "FILEAGETHRESHOLD",
    //                "MONITOR", "JOURNALSERIALIZER", "SNAPSHOTSERIALIZER"};

    return Arrays.asList(options);
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(OPTIONS);
  }
}
