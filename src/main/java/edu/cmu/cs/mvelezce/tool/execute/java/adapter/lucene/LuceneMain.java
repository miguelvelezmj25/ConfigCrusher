package edu.cmu.cs.mvelezce.tool.execute.java.adapter.lucene;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.ConfigCrusherTimerRegionInstrumenter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.demo.IndexFiles;

public class LuceneMain extends BaseMain {

  public static final String LUCENE_MAIN = LuceneMain.class.getCanonicalName();
  public static final String PROGRAM_NAME = "lucene";

  public LuceneMain(String programName, String iteration, String[] args) {
    super(programName, iteration, args);
  }

  public static void main(String[] args) throws IOException {
    String programName = args[0];
    String mainClass = args[1];
    String iteration = args[2];
    String[] luceneArgs = Arrays.copyOfRange(args, 3, args.length);

    Main main = new LuceneMain(programName, iteration, luceneArgs);
    main.execute(mainClass, luceneArgs);
    main.logExecution();
  }

  @Override
  public void logExecution() throws IOException {
    Adapter adapter = new LuceneAdapter();
    Set<String> configuration = adapter.configurationAsSet(this.getArgs());

    ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
    Map<String, Long> results = executor.getResults();
    executor.writeToFile(this.getIteration(), configuration, results);
  }

  @Override
  public void execute(String mainClass, String[] args) {
    if (!mainClass.contains("IndexFiles")) {
      throw new RuntimeException(
          "Could not find the main class " + mainClass + " to execute with lucene");
    }

    this.addRegionsForTracking(args);
    args = addWorkloadArgs(args);

    Region program = new Region.Builder(Regions.PROGRAM_REGION_ID).build();

    try {
      Regions.enter(program.getRegionID());
      IndexFiles.main(args);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      Regions.exit(program.getRegionID());
    }
  }

  private String[] addWorkloadArgs(String[] args) {
    List<String> argsList = new ArrayList<>(Arrays.asList(args));
    argsList.add("-docs");
    argsList.add("../performance-mapper-evaluation/original/lucene/lucene-src/");

    args = new String[argsList.size()];
    return argsList.toArray(args);
  }

  private void addRegionsForTracking(String[] args) {
    try {
      BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter(PROGRAM_NAME);
      instrumenter.instrument(args);
      Set<JavaRegion> regions = instrumenter.getRegionsToOptionSet().keySet();

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
  }
}
