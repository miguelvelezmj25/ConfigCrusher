package edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12;

import edu.cmu.cs.mvelezce.Regions12;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class Regions12Main extends BaseMain {

    public static final String REGIONS_12_MAIN = edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Main.class.getCanonicalName();

    public Regions12Main(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Main(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new Regions12Adapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        Executor executor = new ConfigCrusherExecutor(this.getProgramName());
        executor.writeToFile(this.getIteration(), configuration, Regions.getRegionsToProcessedPerformance());
    }

    @Override
    public void execute(String mainClass, String[] args) throws Exception {
        if(mainClass.contains("Regions12")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Regions12.main(args);
            Regions.exit(program.getRegionID());
        }
        else {
            throw new RuntimeException("Could not find the main class " + mainClass);
        }

        System.out.println("start count " + Regions.startCount);
        System.out.println("end count " + Regions.endCount);
    }
}
