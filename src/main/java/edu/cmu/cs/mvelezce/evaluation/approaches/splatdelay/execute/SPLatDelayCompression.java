package edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.SPLatDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.counter.SPLatDelayCounterDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.density.SPLatDelayDensityDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.elevator.SPLatDelayElevatorDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.grep.SPLatDelayGrepDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.kanzi.SPLatDelayKanziDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.optimizer.SPLatDelayOptimizerDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.prevalyer.SPLatDelayPrevaylerDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.runningexample.SPLatDelayRunningExampleDelayMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splatdelay.execute.adapter.sort.SPLatDelaySortDelayMain;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.BaseCompression;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class SPLatDelayCompression extends BaseCompression {

    public static final String DIRECTORY = Options.DIRECTORY + "/compression/java/programs/splatdelay";

    public SPLatDelayCompression(String programName) {
        super(programName, null);
    }

    @Override
    public Set<Set<String>> compressConfigurations(String[] args) throws IOException {
        Options.getCommandLine(args);

        String outputFile = this.getOutputDir() + "/" + this.getProgramName();
        File file = new File(outputFile);

        Options.checkIfDeleteResult(file);

        if(file.exists()) {
            Collection<File> files = FileUtils.listFiles(file, null, true);

            for(File f : files) {
                if(f.getName().contains(this.getProgramName() + Options.DOT_JSON)) {
                    return this.readFromFile(f);
                }
            }

            throw new RuntimeException("Could not find a file");
        }

        Set<Set<String>> configurationsToExecute = this.compressConfigurations();

        if(Options.checkIfSave()) {
            this.writeToFile(configurationsToExecute);
        }

        return configurationsToExecute;
    }

    @Override
    public Set<Set<String>> compressConfigurations() {
        SPLatDelayMain main;

        if(this.getProgramName().contains("running-example")) {
            main = new SPLatDelayRunningExampleDelayMain("running-example");
        }
        else if(this.getProgramName().contains("pngtasticColorCounter")) {
            main = new SPLatDelayCounterDelayMain("pngtasticColorCounter");
        }
        else if(this.getProgramName().contains("pngtasticOptimizer")) {
            main = new SPLatDelayOptimizerDelayMain("pngtasticOptimizer");
        }
        else if(this.getProgramName().contains("grep")) {
            main = new SPLatDelayGrepDelayMain("grep");
        }
        else if(this.getProgramName().contains("kanzi")) {
            main = new SPLatDelayKanziDelayMain("kanzi");
        }
        else if(this.getProgramName().contains("prevayler")) {
            main = new SPLatDelayPrevaylerDelayMain("prevayler");
        }
        else if(this.getProgramName().contains("elevator")) {
            main = new SPLatDelayElevatorDelayMain("elevator");
        }
        else if(this.getProgramName().contains("density")) {
            main = new SPLatDelayDensityDelayMain("density");
        }
        else if(this.getProgramName().contains("sort")) {
            main = new SPLatDelaySortDelayMain("sort");
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
        return SPLatDelayCompression.DIRECTORY;
    }
}
