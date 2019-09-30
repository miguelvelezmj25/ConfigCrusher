package edu.cmu.cs.mvelezce.tool.execute.java.adapter.nesting;

import edu.cmu.cs.mvelezce.analysis.Nesting;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.region.timer.DynamicConfigCrusherTimerRegionInstrumenter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class NestingMain extends BaseMain {

  static final String NESTING_MAIN = NestingMain.class.getCanonicalName();

  private NestingMain(String programName, String iteration, String[] args) {
    super(programName, iteration, args);
  }

  public static void main(String[] args) throws IOException {
    String programName = args[0];
    String mainClass = args[1];
    String iteration = args[2];
    String[] nestringArgs = Arrays.copyOfRange(args, 3, args.length);

    Main main = new NestingMain(programName, iteration, nestringArgs);
    main.execute(mainClass, nestringArgs);
    main.logExecution();
  }

  @Override
  public void logExecution() throws IOException {
    Adapter adapter = new NestingAdapter();
    Set<String> configuration = adapter.configurationAsSet(this.getArgs());

    ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
    Map<String, Long> results = executor.getResults();
    executor.writeToFile(this.getIteration(), configuration, results);
  }

  @Override
  public void execute(String mainClass, String[] args) {
    try {
      BaseRegionInstrumenter instrumenter =
          new DynamicConfigCrusherTimerRegionInstrumenter(NestingAdapter.PROGRAM_NAME);
      instrumenter.instrument(args);
      Set<JavaRegion> regions = instrumenter.getRegionsToData().keySet();

      for (JavaRegion region : regions) {
        Regions.regionsToOverhead.put(region.getRegionID(), 0L);
      }

      Regions.regionsToOverhead.put(Regions.PROGRAM_REGION_ID, 0L);
    } catch (InvocationTargetException
        | NoSuchMethodException
        | IOException
        | IllegalAccessException
        | InterruptedException e) {
      throw new RuntimeException("Could not add regions to the Regions class");
    }

    if (!mainClass.equals(NestingAdapter.MAIN_CLASS)) {
      throw new RuntimeException("Could not find the main class " + mainClass);
    }

    Region program = new Region.Builder(Regions.PROGRAM_REGION_ID).build();

    try {
      Regions.enter(program.getRegionID());
      Nesting.main(args);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      Regions.exit(program.getRegionID());
    }
  }
}
