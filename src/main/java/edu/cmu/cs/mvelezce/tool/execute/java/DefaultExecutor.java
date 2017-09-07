package edu.cmu.cs.mvelezce.tool.execute.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.analysis.region.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.BaseAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.elevator.ElevatorAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.gpl.GPLAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.pngtastic.PngtasticAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.runningexample.RunningExampleAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.sleep.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.zipme.ZipmeAdapter;
import edu.cmu.cs.mvelezce.tool.execute.java.serialize.Execution;
import edu.cmu.cs.mvelezce.tool.performancemodel.PerformanceEntry2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
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

    public DefaultExecutor(String programName, String entryPoint, String dir, Set<Set<String>> configurations) {
        super(programName, entryPoint, dir, configurations);
    }

    @Override
    public Set<PerformanceEntry2> execute(int iteration) throws IOException, InterruptedException {
        // TODO factory pattern or switch statement to create the right adapter
        BaseAdapter baseAdapter;

        if(this.getProgramName().contains("elevator")) {
            baseAdapter = new ElevatorAdapter(this.getProgramName(), this.getEntryPoint(), this.getDir());
        }
        else if(this.getProgramName().contains("gpl")) {
            baseAdapter = new GPLAdapter(this.getProgramName(), this.getEntryPoint(), this.getDir());
        }
        else if(this.getProgramName().contains("sleep")) {
            baseAdapter = new SleepAdapter(this.getProgramName(), this.getEntryPoint(), this.getDir());
        }
        else if(this.getProgramName().contains("zipme")) {
            baseAdapter = new ZipmeAdapter(this.getProgramName(), this.getEntryPoint(), this.getDir());
        }
        else if(this.getProgramName().contains("pngtastic")) {
            baseAdapter = new PngtasticAdapter(this.getProgramName(), this.getEntryPoint(), this.getDir());
        }
        else if(this.getProgramName().contains("running-example")) {
            baseAdapter = new RunningExampleAdapter(this.getProgramName(), this.getEntryPoint(), this.getDir());
        }
        else {
            throw new RuntimeException("Could not create an adapter for " + this.getProgramName());
        }

        for(Set<String> configuration : this.getConfigurations()) {
            baseAdapter.execute(configuration, iteration);
        }

        // TODO get all files from this directory
        String outputDir = BaseExecutor.DIRECTORY + "/" + this.getProgramName() + "/" + iteration;
        File outputFile = new File(outputDir);

        if(!outputFile.exists()) {
            throw new RuntimeException("The output file could not be found " + outputDir);
        }

        Collection<File> files = FileUtils.listFiles(outputFile, null, true);
        Set<PerformanceEntry2> entries = new HashSet<>();

        for(File file : files) {
            ObjectMapper mapper = new ObjectMapper();
            Execution execution = mapper.readValue(file, new TypeReference<Execution>() {
            });

            PerformanceEntry2 performanceEntry = new PerformanceEntry2(execution);
            entries.add(performanceEntry);
        }

        return entries;
    }

}
