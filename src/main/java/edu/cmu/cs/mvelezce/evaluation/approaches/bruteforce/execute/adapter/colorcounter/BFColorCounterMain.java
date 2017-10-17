package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.colorcounter;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.colorCounter.ColorCounterAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.colorCounter.ColorCounterMain;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class BFColorCounterMain extends ColorCounterMain {

    public static final String BF_COLOR_COUNTER_MAIN = BFColorCounterMain.class.getCanonicalName();

    public BFColorCounterMain(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] counterArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new BFColorCounterMain(programName, iteration, counterArgs);
        main.execute(mainClass, counterArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new ColorCounterAdapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        BruteForceExecutor executor = new BruteForceExecutor(this.getProgramName());
        Map<String, Long> results = executor.getResults();
        executor.writeToFile(this.getIteration(), configuration, results);
    }

}
