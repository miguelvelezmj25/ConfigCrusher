package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.execute;

import edu.cmu.cs.mvelezce.tool.execute.java.BaseExecutor;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FeaturewiseExecutor extends BaseExecutor {

    public FeaturewiseExecutor(String programName) {
        this(programName, null, null, null);
    }

    public FeaturewiseExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        super(programName, entryPoint, classDir, configurations);
    }

    @Override
    public Set<DefaultPerformanceEntry> execute(int iteration) throws IOException, InterruptedException {
        return null;
    }

    public static Set<Set<String>> getFeaturewiseConfigurations(Set<String> options) {
        Set<Set<String>> configurations = new HashSet<>();

        return configurations;
    }
}
