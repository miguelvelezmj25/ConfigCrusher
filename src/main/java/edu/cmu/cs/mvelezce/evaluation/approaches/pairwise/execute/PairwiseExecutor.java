package edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.execute;

import edu.cmu.cs.mvelezce.tool.execute.java.BaseExecutor;
import edu.cmu.cs.mvelezce.tool.performance.entry.DefaultPerformanceEntry;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PairwiseExecutor extends BaseExecutor {

    public PairwiseExecutor(String programName) {
        this(programName, null, null, null);
    }

    public PairwiseExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        super(programName, entryPoint, classDir, configurations);
    }

    @Override
    public Set<DefaultPerformanceEntry> execute(int iteration) throws IOException, InterruptedException {
        return null;
    }
}
