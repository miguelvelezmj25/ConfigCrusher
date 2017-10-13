package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.colorCounter.ColorCounterAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gpl.GPLAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.optimizer.OptimizerAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions12.Regions12Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions13.Regions13Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.regions14.Regions14Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.zipme.ZipmeAdapter;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class ConfigCrusherExecutor extends BaseExecutor {

    static {
        DIRECTORY = BaseExecutor.DIRECTORY + "/configcrusher/programs";
    }

    public ConfigCrusherExecutor() {
        this(null);
    }

    public ConfigCrusherExecutor(String programName) {
        this(programName, null, null, null);
    }

    public ConfigCrusherExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        super(programName, entryPoint, classDir, configurations);
    }

    @Override
    public Set<DefaultPerformanceEntry> execute(int iteration) throws IOException, InterruptedException {
        // TODO factory pattern or switch statement to create the right adapter
        Adapter adapter;

        if(this.getProgramName().contains("elevator")) {
            adapter = new ElevatorAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("gpl")) {
            adapter = new GPLAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("sleep")) {
            adapter = new SleepAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("zipme")) {
            adapter = new ZipmeAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
//        else if(this.getProgramName().contains("pngtastic")) {
//            adapter = new PngtasticAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
//        }
        else if(this.getProgramName().contains("pngtasticColorCounter")) {
            adapter = new ColorCounterAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("pngtasticOptimizer")) {
            adapter = new OptimizerAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("running-example")) {
            adapter = new RunningExampleAdapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("regions12")) {
            adapter = new Regions12Adapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("regions13")) {
            adapter = new Regions13Adapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else if(this.getProgramName().contains("regions14")) {
            adapter = new Regions14Adapter(this.getProgramName(), this.getEntryPoint(), this.getClassDir());
        }
        else {
            throw new RuntimeException("Could not create an adapter for " + this.getProgramName());
        }

        for(Set<String> configuration : this.getConfigurations()) {
            adapter.execute(configuration, iteration);
        }

        String outputDir = BaseExecutor.DIRECTORY + "/" + this.getProgramName() + "/" + iteration;
        File outputFile = new File(outputDir);

        if(!outputFile.exists()) {
            throw new RuntimeException("The output file could not be found " + outputDir);
        }

        Set<DefaultPerformanceEntry> performanceEntries = this.aggregateExecutions(outputFile);
        return performanceEntries;
    }

}
