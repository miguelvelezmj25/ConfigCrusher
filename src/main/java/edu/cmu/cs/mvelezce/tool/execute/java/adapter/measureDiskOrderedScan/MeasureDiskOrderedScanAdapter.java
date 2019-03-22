package edu.cmu.cs.mvelezce.tool.execute.java.adapter.measureDiskOrderedScan;

import edu.cmu.cs.mvelezce.tool.Helper;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import java.io.IOException;
import java.util.ArrayList;
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

  private static final String HOME_DIR = "tmp";
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

  public void preProcess() {
    try {
      this.removeDir();
      this.makeDir();
      this.sync();
      this.clearCache();
    }
    catch (IOException | InterruptedException e) {
      throw new RuntimeException("Could not create the tmp folder to run " + PROGRAM_NAME);
    }
  }

  private void clearCache() {
    List<String> commandList = new ArrayList<>();
    commandList.add("echo");
    commandList.add("3");
    commandList.add(">");
    commandList.add("/proc/sys/vm/drop_caches");

    try {
      runCommand(commandList);
    }
    catch (IOException | InterruptedException e) {
      System.err.println("Could not clear the cache before running " + PROGRAM_NAME);
    }
  }

  private void sync() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("sync");

    runCommand(commandList);
  }

  private void makeDir() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("mkdir");
    commandList.add(HOME_DIR);

    runCommand(commandList);
  }

  private void removeDir() throws IOException, InterruptedException {
    List<String> commandList = new ArrayList<>();
    commandList.add("rm");
    commandList.add("-rf");
    commandList.add(HOME_DIR);

    runCommand(commandList);
  }

  private void runCommand(List<String> commandList) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(commandList);

    Process process = builder.start();

    Helper.processOutput(process);
    Helper.processError(process);

    process.waitFor();
  }

}
