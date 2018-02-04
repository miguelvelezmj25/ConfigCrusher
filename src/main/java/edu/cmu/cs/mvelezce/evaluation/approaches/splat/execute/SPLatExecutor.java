package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.SPLatMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.counter.SPLatCounterMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.grep.SPLatGrepMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.kanzi.SPLatKanziMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.optimizer.SPLatOptimizerMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.prevalyer.SPLatPrevaylerMain;
import edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter.runningexample.SPLatRunningExampleMain;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.compression.BaseCompression;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class SPLatExecutor extends BaseCompression {

    public static final String DIRECTORY = Options.DIRECTORY + "/compression/java/programs/splat";

    public SPLatExecutor(String programName) {
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
        else if(this.getProgramName().contains("kanzi")) {
            main = new SPLatKanziMain("kanzi");
        }
        else if(this.getProgramName().contains("prevayler")) {
            main = new SPLatPrevaylerMain("prevayler");
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
