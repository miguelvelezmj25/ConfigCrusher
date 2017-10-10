package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions12;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class BFRegions12Main extends Regions12Main {

    public static final String BF_REGIONS_12_MAIN = BFRegions12Main.class.getCanonicalName();

    public BFRegions12Main(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new BFRegions12Main(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new Regions12Adapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        Executor executor = new BruteForceExecutor(this.getProgramName());
        executor.writeToFile(this.getIteration(), configuration, Regions.getExecutedRegionsTrace());
    }

}
