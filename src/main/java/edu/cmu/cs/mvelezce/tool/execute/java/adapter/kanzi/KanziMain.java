package edu.cmu.cs.mvelezce.tool.execute.java.adapter.kanzi;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import kanzi.Run;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class KanziMain extends BaseMain {

    public static final String KANZI_MAIN = KanziMain.class.getCanonicalName();

    public KanziMain(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new KanziMain(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new KanziAdapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        ConfigCrusherExecutor executor = new ConfigCrusherExecutor(this.getProgramName());
        Map<String, Long> results = executor.getResults();
        executor.writeToFile(this.getIteration(), configuration, results);
    }

    @Override
    public void execute(String mainClass, String[] args) throws Exception {
        if(mainClass.contains("Run")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Run.main(args);
            Regions.exit(program.getRegionID());
        }
        else {
            throw new RuntimeException("Could not find the main class " + mainClass);
        }

    }
}
