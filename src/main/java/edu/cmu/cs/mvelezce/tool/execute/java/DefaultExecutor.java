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
        this(null, null, null, null);
    }

    public DefaultExecutor(String programName, String mainClass, String dir, Set<Set<String>> configurations) {
        this(programName, mainClass, dir, configurations, 1);
    }

    public DefaultExecutor(String programName, String mainClass, String dir, Set<Set<String>> configurations, int repetitions) {
        super(programName, mainClass, dir, configurations, repetitions);
    }

    @Override
    public Set<PerformanceEntry> execute(String programName) throws IOException {
        // TODO factory pattern or switch statement to create the right adapter
        BaseAdapter baseAdapter;

        if(programName.contains("elevator")) {
            baseAdapter = new ElevatorAdapter(programName, this.getMainClass(), this.getDir());
        }
        else if(programName.contains("gpl")) {
            baseAdapter = new GPLAdapter(programName, this.getMainClass(), this.getDir());
        }
        else if(programName.contains("sleep")) {
            baseAdapter = new SleepAdapter(programName, this.getMainClass(), this.getDir());
        }
        else if(programName.contains("zipme")) {
            baseAdapter = new ZipmeAdapter(programName, this.getMainClass(), this.getDir());
        }
        else if(programName.contains("pngtastic")) {
            baseAdapter = new PngtasticAdapter(programName, this.getMainClass(), this.getDir());
        }
        else {
            throw new RuntimeException("Could not create an adapter for " + programName);
        }

        for(Set<String> configuration : this.getConfigurations()) {
            baseAdapter.execute(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }
        }

        String outputFile = BaseExecutor.DIRECTORY + "/" + programName + Options.DOT_JSON;
        File file = new File(outputFile);

//        Execution result = this.readFromFile(file);

        // TODO
        return null;
    }

}
