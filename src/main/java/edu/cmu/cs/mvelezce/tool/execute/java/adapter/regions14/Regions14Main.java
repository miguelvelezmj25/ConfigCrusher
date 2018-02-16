package edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions14;

import edu.cmu.cs.mvelezce.Regions14;
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
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class Regions14Main extends BaseMain {

    public static final String REGIONS_14_MAIN = Regions14Main.class.getCanonicalName();

    public Regions14Main(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws IOException {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new Regions14Main(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new Regions14Adapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
        Map<String, Long> results = executor.getResults();
        executor.writeToFile(this.getIteration(), configuration, results);
    }

    @Override
    public void execute(String mainClass, String[] args) {
        try {
            BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter("regions14");
            instrumenter.instrument(args);
            Set<JavaRegion> regions = instrumenter.getRegionsToOptionSet().keySet();

            for(JavaRegion region : regions) {
                Regions.regionsToOverhead.put(region.getRegionID(), 0L);
            }
            Regions.regionsToOverhead.put(Regions.PROGRAM_REGION_ID, 0L);
        }
        catch(InvocationTargetException | NoSuchMethodException | IOException | IllegalAccessException | InterruptedException e) {
            throw new RuntimeException("Could not add regions to the Regions class");
        }

        if(mainClass.contains("Regions14")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);

            try {
                Regions.enter(program.getRegionID());
                Regions14.main(args);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                Regions.exit(program.getRegionID());
            }
        }
        else {
            throw new RuntimeException("Could not find the main class " + mainClass);
        }
    }
}
