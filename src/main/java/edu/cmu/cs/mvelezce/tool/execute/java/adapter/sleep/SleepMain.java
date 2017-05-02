package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.java.programs.Sleep4;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.Executer;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepMain {

    private static final String SLEEP4 = "sleep4";

    public static final String SLEEP_MAIN = SleepMain.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {
        String programName = args[0];
        String mainClass = args[1];
        String[] sleepArgs = Arrays.copyOfRange(args, 2, args.length);

        if(mainClass.toLowerCase().contains(SleepMain.SLEEP4)) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep4.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);

        }

        Set<String> performanceConfiguration = SleepAdapter.adaptConfigurationToPerformanceMeasurement(sleepArgs);
        Executer.logExecutedRegions(programName, performanceConfiguration, Regions.getExecutedRegionsTrace());
    }
}
