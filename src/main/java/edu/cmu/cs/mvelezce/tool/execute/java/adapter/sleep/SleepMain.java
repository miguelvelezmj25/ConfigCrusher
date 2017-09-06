package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.*;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.DefaultExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepMain implements Main {

    public static final String SLEEP_MAIN = SleepMain.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException, IOException {
        String programName = args[0];
        String mainClass = args[1];
        String[] sleepArgs = Arrays.copyOfRange(args, 2, args.length);

        if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep1")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep1.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep2")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep2.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep3")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep3.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep4")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep4.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep5")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep5.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep6")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep6.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep7")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep7.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep8")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep8.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep9")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep9.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep10")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep10.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep11")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep11.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep12")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep12.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep13")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep13.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep14")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep14.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep15")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep15.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep16")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep16.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep17")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep17.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep18")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep18.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep19")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep19.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep20")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep20.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep21")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep21.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep22")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep22.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep23")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep23.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep24")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep24.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep25")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep25.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep26")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep26.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep27")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep27.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep28")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep28.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep29")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep29.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep30")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep30.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.Sleep31")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep31.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else {
            throw new RuntimeException("Could not find the main class " + mainClass);
        }

        Main main = new SleepMain();
        main.logExecution(programName, sleepArgs);
    }

    @Override
    public void logExecution(String programName, String[] args) throws IOException {
        Adapter adapter = new SleepAdapter();
        Set<String> configuration = adapter.configurationAsSet(args);

        Executor executor = new DefaultExecutor();
        executor.writeToFile(programName, configuration, Regions.getExecutedRegionsTrace());
    }

}
