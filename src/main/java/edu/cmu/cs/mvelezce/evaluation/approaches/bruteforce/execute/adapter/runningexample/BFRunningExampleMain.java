package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.runningexample;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleMain;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class BFRunningExampleMain extends RunningExampleMain {

    public static final String BF_RUNNING_EXAMPLE_MAIN = BFRunningExampleMain.class.getCanonicalName();

    public BFRunningExampleMain(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new BFRunningExampleMain(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new RunningExampleAdapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        Executor executor = new BruteForceExecutor(this.getProgramName());
        executor.writeToFile(this.getIteration(), configuration, Regions.getRegionsToProcessedPerformance());
    }

}
