package edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.adapter.find;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.execute.BruteForceExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.find.FindAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.find.FindMain;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class BFFindMain extends FindMain {

    public static final String BF_FIND_MAIN = BFFindMain.class.getCanonicalName();

    public BFFindMain(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] counterArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new BFFindMain(programName, iteration, counterArgs);
        main.execute(mainClass, counterArgs);
        main.logExecution();

    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new FindAdapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        BruteForceExecutor executor = new BruteForceExecutor(this.getProgramName());
        Map<String, Long> results = executor.getResults();
        executor.writeToFile(this.getIteration(), configuration, results);
    }
}
