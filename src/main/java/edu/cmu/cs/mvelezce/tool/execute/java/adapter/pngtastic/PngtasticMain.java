package edu.cmu.cs.mvelezce.tool.execute.java.adapter.pngtastic;

import edu.cmu.cs.mvelezce.tool.analysis.region.Region;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.ConfigCrusherExecutor;
import edu.cmu.cs.mvelezce.tool.execute.java.Executor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class PngtasticMain {

    public static final String PNGTASTIC_MAIN = PngtasticMain.class.getCanonicalName();

    public static void main(String[] args) throws InterruptedException, IOException {
        String programName = args[0];
        String mainClass = args[1];
        String[] pngtasticArgs = Arrays.copyOfRange(args, 2, args.length);

        if(mainClass.equals("com.googlecode.pngtastic.PngtasticColorCounter")) {
            Region program = new Region.Builder(Regions.PROGRAM_REGION_ID).build();
            Regions.enter(program.getRegionID());
//            PngtasticColorCounter.main(pngtasticArgs);
            Regions.exit(program.getRegionID());
        }

        Set<String> performanceConfiguration = PngtasticAdapter.adaptConfigurationToPerformanceMeasurement(pngtasticArgs);
        Executor executor = new ConfigCrusherExecutor();
//        executor.writeToFile(programName, performanceConfiguration, Regions.getRegionsToProcessedPerformance());
        throw new RuntimeException("Check this main");
    }
}
