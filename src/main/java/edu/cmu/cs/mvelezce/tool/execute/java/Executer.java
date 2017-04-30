package edu.cmu.cs.mvelezce.tool.execute.java;

import edu.cmu.cs.mvelezce.tool.analysis.Regions;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.Adapter;
import edu.cmu.cs.mvelezce.tool.execute.java.adapter.SleepAdapter;
import edu.cmu.cs.mvelezce.tool.performance.PerformanceEntry;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by miguelvelez on 4/30/17.
 */
public class Executer {

    public static Set<PerformanceEntry> measureConfigurationPerformance(String mainClass, String directory, Set<Set<String>> configurationsToExecute) {
        Regions.reset();
        // TODO add program region
        Set<PerformanceEntry> configurationsToPerformance = new HashSet<>();

        for(Set<String> configuration : configurationsToExecute) {
            Regions.resetRegions();

            // TODO factory pattern or switch statement to create the right adapter
            Adapter adapter = new SleepAdapter(mainClass, directory);
            adapter.execute(configuration);

            if(!Regions.getExecutingRegions().isEmpty()) {
                throw new RuntimeException("There program finished executing, but there are methods in the execution stack that did not finish");
            }

            configurationsToPerformance.add(new PerformanceEntry(configuration, Regions.getRegions(), Regions.getProgram()));
        }

        return configurationsToPerformance;
    }
}
