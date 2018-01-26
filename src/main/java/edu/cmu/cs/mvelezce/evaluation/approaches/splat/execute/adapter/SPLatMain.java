package edu.cmu.cs.mvelezce.evaluation.approaches.splat.execute.adapter;

import java.util.Set;

public interface SPLatMain {

    Set<Set<String>> getSPLatConfigurations(String programName) throws InterruptedException;

}
