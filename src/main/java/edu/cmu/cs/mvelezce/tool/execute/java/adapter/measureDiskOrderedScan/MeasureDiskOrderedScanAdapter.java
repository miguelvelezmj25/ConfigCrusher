package edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Berkeley DB
 */
public class MeasureDiskOrderedScanAdapter extends BaseAdapter {

  public static final String PROGRAM_NAME = "measureDiskOrderedScan";
  public static final String MAIN_CLASS = "com.sleepycat.analysis.MeasureDiskOrderedScan";
  public static final String ORIGINAL_CLASS_PATH = "../performance-mapper-evaluation/original/berkeley-db/target/classes";
  public static final String INSTRUMENTED_CLASS_PATH = "../performance-mapper-evaluation/instrumented/berkeley-db/target/classes";

  private static final String[] OPTIONS = {"SEQUENTIAL"};

  public MeasureDiskOrderedScanAdapter() {
    // TODO check that we are passing empty string
    super(MeasureDiskOrderedScanAdapter.PROGRAM_NAME, MeasureDiskOrderedScanAdapter.MAIN_CLASS, "",
        MeasureDiskOrderedScanAdapter.getListOfOptions());
  }

  public static List<String> getListOfOptions() {
    return Arrays.asList(MeasureDiskOrderedScanAdapter.OPTIONS);
  }

  @Override
  public void execute(Set<String> configuration, int iteration)
      throws IOException, InterruptedException {
    String[] args = this.configurationAsMainArguments(configuration);
    String[] newArgs = new String[args.length + 1];

    newArgs[0] = iteration + "";
    System.arraycopy(args, 0, newArgs, 1, args.length);

    this.execute(MeasureDiskOrderedScanMain.MEASURE_DISK_ORDERED_SCAN_MAIN, newArgs);
  }

//  public BerkeleyAdapter() {
//    this(null, null, null);
//  }
//
//  public BerkeleyAdapter(String programName, String entryPoint, String dir) {
//    super(programName, entryPoint, dir, BerkeleyAdapter.getBerkeleyOptions());
//  }
//
//  public static List<String> getBerkeleyOptions() {
//    String[] options = {"RECORDS", "DATA", "DUPLICATES", "KEYSIZE", "SEQUENTIAL"};
//
//    return Arrays.asList(options);
//  }
//
//  @Override
//  public void execute(Set<String> configuration, int iteration)
//      throws IOException, InterruptedException {
//    String[] args = this.configurationAsMainArguments(configuration);
//    String[] newArgs = new String[args.length + 1];
//
//    newArgs[0] = iteration + "";
//    System.arraycopy(args, 0, newArgs, 1, args.length);
//
//    this.execute(BerkeleyMain.BERKELEY_MAIN, newArgs);
//  }
}
