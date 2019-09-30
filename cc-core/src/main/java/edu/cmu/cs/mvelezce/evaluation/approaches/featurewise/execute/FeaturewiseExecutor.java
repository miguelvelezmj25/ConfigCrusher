package edu.cmu.cs.mvelezce.evaluation.approaches.featurewise.execute;

import edu.cmu.cs.mvelezce.evaluation.approaches.bruteforce.UsesBFExecutor;

import java.util.*;

public class FeaturewiseExecutor extends UsesBFExecutor {

    public FeaturewiseExecutor(String programName) {
        this(programName, null, null, null);
    }

    public FeaturewiseExecutor(String programName, String entryPoint, String classDir, Set<Set<String>> configurations) {
        super(programName, entryPoint, classDir, configurations);
    }

}
