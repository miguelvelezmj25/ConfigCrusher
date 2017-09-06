package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gpl.GPLAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.pngtastic.PngtasticAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.zipme.ZipmeAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class DefaultExecutor extends BaseExecutor {

    public DefaultExecutor() {
        this(null);
    }

    public DefaultExecutor(String programName) {
        this(programName, null, null, null);
    }

    public DefaultExecutor(String programName, String mainClass, String dir, Set<Set<String>> configurations) {
        super(programName, mainClass, dir, configurations);
    }

    @Override
    public Set<PerformanceEntry> execute(int iteration) throws IOException {
        // TODO factory pattern or switch statement to create the right adapter
        BaseAdapter baseAdapter;

        if(this.getProgramName().contains("elevator")) {
            baseAdapter = new ElevatorAdapter(this.getProgramName(), this.getMainClass(), this.getDir());
        }
        else if(this.getProgramName().contains("gpl")) {
            baseAdapter = new GPLAdapter(this.getProgramName(), this.getMainClass(), this.getDir());
        }
        else if(this.getProgramName().contains("sleep")) {
            baseAdapter = new SleepAdapter(this.getProgramName(), this.getMainClass(), this.getDir());
        }
        else if(this.getProgramName().contains("zipme")) {
            baseAdapter = new ZipmeAdapter(this.getProgramName(), this.getMainClass(), this.getDir());
        }
        else if(this.getProgramName().contains("pngtastic")) {
            baseAdapter = new PngtasticAdapter(this.getProgramName(), this.getMainClass(), this.getDir());
        }
        else {
            throw new RuntimeException("Could not create an adapter for " + this.getProgramName());
        }

        for(Set<String> configuration : this.getConfigurations()) {
            baseAdapter.execute(configuration, iteration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }
        }

        // TODO get all files from this directory
        String outputFile = BaseExecutor.DIRECTORY + "/" + this.getProgramName() + Options.DOT_JSON;
        File file = new File(outputFile);

//        Execution result = this.readFromFile(file);

        // TODO
        return null;
    }

}
