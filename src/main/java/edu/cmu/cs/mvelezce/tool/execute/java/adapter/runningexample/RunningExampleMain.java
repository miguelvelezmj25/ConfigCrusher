package edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample;

import edu.cmu.cs.mvelezce.Example;
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
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class RunningExampleMain extends BaseMain {

    public static final String RUNNING_EXAMPLE_MAIN = RunningExampleMain.class.getCanonicalName();

    public RunningExampleMain(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new RunningExampleMain(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new RunningExampleAdapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
        Map<String, Long> results = executor.getResults();
        executor.writeToFile(this.getIteration(), configuration, results);
    }

    @Override
    public void execute(String mainClass, String[] args) throws Exception {
        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter("running-example");
        instrumenter.instrument(args);
        Set<JavaRegion> regions = instrumenter.getRegionsToOptionSet().keySet();

        for(JavaRegion region : regions) {
            Regions.regionsToOverhead.put(region.getRegionID(),0L);
        }
            Regions.regionsToOverhead.put(Regions.PROGRAM_REGION_ID,0L);

        if(mainClass.contains("Example")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Example.main(args);
            Regions.exit(program.getRegionID());
        }
        else {
            throw new RuntimeException("Could not find the main class " + mainClass);
        }
    }
}
