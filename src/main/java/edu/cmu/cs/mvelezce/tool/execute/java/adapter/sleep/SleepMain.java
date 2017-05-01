package edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep;

import edu.cmu.cs.mvelezce.java.programs.Sleep4;
import edu.cmu.cs.mvelezce.tool.analysis.Region;
import edu.cmu.cs.mvelezce.tool.analysis.Regions;

import java.util.Arrays;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class SleepMain {

    private static final String SLEEP4 = "sleep4";

    public static final String SLEEP_MAIN = SleepMain.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException {
        String sleepProgram = args[0];
        String[] sleepArgs = Arrays.copyOfRange(args, 1, args.length);

        if(sleepProgram.toLowerCase().contains(SleepMain.SLEEP4)) {
            Region program = new Region();
            Regions.addProgram(program);
            Regions.addExecutingRegion(program);

            program.startTime();
            Sleep4.main(sleepArgs);
            program.endTime();

            Regions.removeExecutingRegion(program);

            System.out.println(Regions.getRegions());
            System.out.println(Regions.getRegionsToAllPossibleInnerRegions());
        }

    }
}
