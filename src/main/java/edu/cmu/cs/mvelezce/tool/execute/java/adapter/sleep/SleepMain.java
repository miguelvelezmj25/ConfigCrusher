package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.java.programs.*;
import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepMain {

    public static final String SLEEP_MAIN = SleepMain.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {
        String programName = args[0];
        String mainClass = args[1];
        String[] sleepArgs = Arrays.copyOfRange(args, 2, args.length);

        if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep1")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep1.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep2")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep2.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep3")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep3.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep4")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep4.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep5")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep5.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep6")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep6.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep7")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep7.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep8")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep8.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep9")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep9.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep10")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep10.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep11")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep11.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep12")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep12.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.equals("edu.cmu.cs.mvelezce.java.programs.Sleep13")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep13.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }

        Set<String> performanceConfiguration = SleepAdapter.adaptConfigurationToPerformanceMeasurement(sleepArgs);
        Executor.logExecutedRegions(programName, performanceConfiguration, Regions.getExecutedRegionsTrace());
    }
}
