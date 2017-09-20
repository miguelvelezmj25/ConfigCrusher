package edu.cmu.cs.mvelezce.tool.execute.java.adapter.optimizer;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.DefaultExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseMain;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;
import optimizer.com.googlecode.pngtastic.Run;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class OptimizerMain extends BaseMain {

    public static final String OPTIMIZER_MAIN = OptimizerMain.class.getCanonicalName();

    public OptimizerMain(String programName, String iteration, String[] args) {
        super(programName, iteration, args);
    }

    public static void main(String[] args) throws Exception {
        String programName = args[0];
        String mainClass = args[1];
        String iteration = args[2];
        String[] sleepArgs = Arrays.copyOfRange(args, 3, args.length);

        Main main = new OptimizerMain(programName, iteration, sleepArgs);
        main.execute(mainClass, sleepArgs);
        main.logExecution();
    }

    @Override
    public void logExecution() throws IOException {
        Adapter adapter = new OptimizerAdapter();
        Set<String> configuration = adapter.configurationAsSet(this.getArgs());

        Executor executor = new DefaultExecutor(this.getProgramName());
        executor.writeToFile(this.getIteration(), configuration, Regions.getExecutedRegionsTrace());
    }

    @Override
    public void execute(String mainClass, String[] args) throws Exception {
        if(mainClass.contains("Run")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Run.main(args);
            Regions.exit(program.getRegionID());
            System.out.println(Regions.start);
            System.out.println(Regions.end);
        }
        else {
            throw new RuntimeException("Could not find the main class " + mainClass);
        }
    }
}
