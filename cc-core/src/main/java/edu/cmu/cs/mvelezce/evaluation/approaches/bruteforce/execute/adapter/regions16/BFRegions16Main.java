package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.regions16;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions16.Regions16Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions16.Regions16Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class BFRegions16Main extends Regions16Main {

    public static final String BF_REGIONS_16_MAIN = BFRegions16Main.class.getCanonicalName();

    public BFRegions16Main(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws IOException {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new BFRegions16Main(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new Regions16Adapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        BruteForceExecutor executor = new BruteForceExecutor(this.getProgramName());
        Map<String, Long> results = executor.getResults();
        executor.writeToFile(this.getIteration(), configuration, results);
    }

}
