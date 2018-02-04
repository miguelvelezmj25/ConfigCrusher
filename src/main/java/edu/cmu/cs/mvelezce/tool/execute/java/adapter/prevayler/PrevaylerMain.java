package edu.cmu.cs.mvelezce.tool.execute.java.adapter.prevayler;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.BaseRegionInstrumenter;
import edu.cmu.cs.mvelezce.tool.instrumentation.java.ConfigCrusherTimerRegionInstrumenter;
import org.prevayler.demos.demo1.PrimeNumbers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class PrevaylerMain extends BaseMain {

    public static final String PREVAYLER_MAIN = PrevaylerMain.class.getCanonicalName();

    public PrevaylerMain(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new PrevaylerMain(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new PrevaylerAdapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
        Map<String, Long> results = executor.getResults();
        executor.writeToFile(this.getIteration(), configuration, results);
    }

    @Override
    public void execute(String mainClass, String[] args) throws Exception {
        BaseRegionInstrumenter instrumenter = new ConfigCrusherTimerRegionInstrumenter("prevayler");
        instrumenter.instrument(args);
        Set<JavaRegion> regions = instrumenter.getRegionsToOptionSet().keySet();

        for(JavaRegion region : regions) {
            Regions.regionsToOverhead.put(region.getRegionID(),0L);
        }
        Regions.regionsToOverhead.put(Regions.PROGRAM_REGION_ID,0L);

        if(mainClass.contains("PrimeNumbers")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            PrimeNumbers.main(args);
            Regions.exit(program.getRegionID());
        }
        else {
            throw new RuntimeException("Could not find the main class " + mainClass);
        }
    }
}
