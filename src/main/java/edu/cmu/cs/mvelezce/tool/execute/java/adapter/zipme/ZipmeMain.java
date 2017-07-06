package edu.cmu.cs.mvelezce.tool.execute.java.adapter.zipme;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;
import edu.cmu.cs.mvelezce.zip.ZipMain;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class ZipmeMain {

    public static final String ZIPME_MAIN = ZipmeMain.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {
        String programName = args[0];
        String mainClass = args[1];
        String[] zipmeArgs = Arrays.copyOfRange(args, 2, args.length);

        if(mainClass.equals("edu.cmu.cs.mvelezce.ZipMain")) {
            Region program = new Region(Regions.PROGRAM_REGION_ID);
            Regions.enter(program.getRegionID());
            ZipMain.main(zipmeArgs);
            Regions.exit(program.getRegionID());
        }

        Set<String> performanceConfiguration = ZipmeAdapter.adaptConfigurationToPerformanceMeasurement(zipmeArgs);
        Executor.logExecutedRegions(programName, performanceConfiguration, Regions.getExecutedRegionsTrace());
    }
}
