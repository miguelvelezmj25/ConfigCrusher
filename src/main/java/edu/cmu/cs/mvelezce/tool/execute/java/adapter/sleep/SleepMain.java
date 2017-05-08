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

        if(mainClass.toLowerCase().equals(Sleep1.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep1.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep2.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep2.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep3.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep3.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep4.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep4.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep5.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep5.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep6.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep6.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep7.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep7.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep8.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep8.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep9.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep9.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep10.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep10.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep11.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep11.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }
        else if(mainClass.toLowerCase().equals(Sleep12.FILENAME.toLowerCase())) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            Sleep12.main(sleepArgs);
            Regions.exit(program.getRegionID());
        }

        Set<String> performanceConfiguration = SleepAdapter.adaptConfigurationToPerformanceMeasurement(sleepArgs);
        Executor.logExecutedRegions(programName, performanceConfiguration, Regions.getExecutedRegionsTrace());
    }
}
