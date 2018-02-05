package edu.cmu.cs.mvelezce.evaluation.approaches.pairwise.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.UsesBFExecutor;

import java.util.*;

public class PairwiseExecutor extends UsesBFExecutor {

    public PairwiseExecutor(String programName) {
        this(programName, null, null, null);
    }

    public PairwiseExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        super(programName, entryPoint, classDir, configurations);
    }

}
