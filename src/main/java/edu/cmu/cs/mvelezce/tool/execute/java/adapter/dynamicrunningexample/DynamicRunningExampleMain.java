package edu.cmu.cs.mvelezce.tool.execute.java.adapter.dynamicrunningexample;

import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class DynamicRunningExampleMain extends BaseMain {

  static final String DYNAMIC_RUNNING_EXAMPLE_MAIN = DynamicRunningExampleMain.class
      .getCanonicalName();

  private DynamicRunningExampleMain(String programName, String iteration, String[] args) {
    super(programName, iteration, args);
  }

  public static void main(String[] args) throws IOException {
    String programName = args[0];
    String mainClass = args[1];
    String iteration = args[2];
    String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

    Main main = new DynamicRunningExampleMain(programName, iteration, sleepArgs);
    main.execute(mainClass, sleepArgs);
    main.logExecution();
  }

  @Override
  public void logExecution() throws IOException {
    Adapter adapter = new DynamicRunningExampleAdapter();
    Set<String> configuration = adapter.configurationAsSet(this.getArgs());

    ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
    Map<String, Long> results = executor.getResults();
    executor.writeToFile(this.getIteration(), configuration, results);
  }

  @Override
  public void execute(String mainClass, String[] args) {
    throw new UnsupportedOperationException("Implement this logic");

//    try {
//      BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(PROGRAM_NAME);
//      instrumenter.instrument(args);
//      Set<JavaRegion> regions = instrumenter.getRegionsToOptionSet().keySet();
//
//      for (JavaRegion region : regions) {
//        Regions.regionsToOverhead.put(region.getRegionID(), 0L);
//      }
//
//      Regions.regionsToOverhead.put(Regions.PROGRAM_REGION_ID, 0L);
//    } catch (InvocationTargetException
//        | NoSuchMethodException
//        | IOException
//        | IllegalAccessException
//        | InterruptedException e) {
//      throw new RuntimeException("Could not add regions to the Regions class");
//    }
//
//    if (mainClass.contains("Example")) {
//      Region program = new Region(Regions.PROGRAM_REGION_ID);
//
//      try {
//        Regions.enter(program.getRegionID());
//        Example.main(args);
//      } catch (Exception e) {
//        e.printStackTrace();
//      } finally {
//        Regions.exit(program.getRegionID());
//      }
//    } else {
//      throw new RuntimeException("Could not find the main class " + mainClass);
//    }
  }
}
