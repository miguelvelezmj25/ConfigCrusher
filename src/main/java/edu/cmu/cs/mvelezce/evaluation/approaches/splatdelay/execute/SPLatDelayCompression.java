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
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleMain;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public class SPLatDelayCompression extends BaseCompression {

  public static final String DIRECTORY =
      Options.DIRECTORY + "/compression/java/programs/splatdelay";

  public SPLatDelayCompression(String programName) {
    super(programName, null);
  }

  @Override
  public Set<Set<String>> compressConfigurations(String[] args) throws IOException {
    Options.getCommandLine(args);

    String outputFile = this.getOutputDir() + "/" + this.getProgramName();
    File file = new File(outputFile);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      Collection<File> files = FileUtils.listFiles(file, null, true);

      for (File f : files) {
        if (f.getName().contains(this.getProgramName() + Options.DOT_JSON)) {
          return this.readFromFile(f);
        }
      }

      throw new RuntimeException("Could not find a file");
    }

    Set<Set<String>> configurationsToExecute = this.compressConfigurations();

    if (Options.checkIfSave()) {
      this.writeToFile(configurationsToExecute);
    }

    return configurationsToExecute;
  }

  @Override
  public Set<Set<String>> compressConfigurations() {
    SPLatDelayMain main;

    String programName = this.getProgramName();

    if (programName.contains(RunningExampleMain.PROGRAM_NAME)) {
      main = new SPLatDelayRunningExampleDelayMain(RunningExampleMain.PROGRAM_NAME);
    }
    else if (programName.contains("pngtasticColorCounter")) {
      main = new SPLatDelayCounterDelayMain("pngtasticColorCounter");
    }
    else if (programName.contains("pngtasticOptimizer")) {
      main = new SPLatDelayOptimizerDelayMain("pngtasticOptimizer");
    }
    else if (programName.contains("grep")) {
      main = new SPLatDelayGrepDelayMain("grep");
    }
    else if (programName.contains("kanzi")) {
      main = new SPLatDelayKanziDelayMain("kanzi");
    }
    else if (programName.contains("prevayler")) {
      main = new SPLatDelayPrevaylerDelayMain("prevayler");
    }
    else if (programName.contains("elevator")) {
      main = new SPLatDelayElevatorDelayMain("elevator");
    }
    else if (programName.contains("density")) {
      main = new SPLatDelayDensityDelayMain("density");
    }
    else if (programName.contains("sort")) {
      main = new SPLatDelaySortDelayMain("sort");
    }
    else {
      throw new RuntimeException("Could not create an adapter for " + programName);
    }

    Set<Set<String>> splatConfigurations;
    try {
      splatConfigurations = main.getSPLatConfigurations();
      main.writeToFileCoverage();
    } catch (InterruptedException | IOException e) {
      throw new RuntimeException("There was an error calculating the splat configurations");
    }

    return splatConfigurations;
  }

  @Override
  public String getOutputDir() {
    return SPLatDelayCompression.DIRECTORY;
  }
}
