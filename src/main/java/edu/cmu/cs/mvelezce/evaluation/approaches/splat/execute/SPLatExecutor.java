package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.SPLatMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.counter.SPLatCounterMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.grep.SPLatGrepMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.optimizer.SPLatOptimizerMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.runningexample.SPLatRunningExampleMain;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.BaseCompression;

import java.io.IOException;
import java.util.Set;

public class SPLatExecutor extends BaseCompression {

    public static final String DIRECTORY = Options.DIRECTORY + "/compression/java/programs/splat";

    public SPLatExecutor(String programName) {
        super(programName, null);
    }

    @Override
    public Set<Set<String>> compressConfigurations() {
        SPLatMain main;

        if(this.getProgramName().contains("running-example")) {
            main = new SPLatRunningExampleMain("running-example");
        }
        else if(this.getProgramName().contains("pngtasticColorCounter")) {
            main = new SPLatCounterMain("pngtasticColorCounter");
        }
        else if(this.getProgramName().contains("pngtasticOptimizer")) {
            main = new SPLatOptimizerMain("pngtasticOptimizer");
        }
        else if(this.getProgramName().contains("grep")) {
            main = new SPLatGrepMain("grep");
        }
        else {
            throw new RuntimeException("Could not create an adapter for " + this.getProgramName());
        }

        Set<Set<String>> splatConfigurations;
        try {
            splatConfigurations = main.getSPLatConfigurations();
            main.writeToFileCoverage();
        } catch(InterruptedException | IOException e) {
            throw new RuntimeException("There was an error calculating the splat configurations");
        }

        return splatConfigurations;
    }

    @Override
    public String getOutputDir() {
        return SPLatExecutor.DIRECTORY;
    }
}
