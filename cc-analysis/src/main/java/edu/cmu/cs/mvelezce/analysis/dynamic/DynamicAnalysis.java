package edu.cmu.cs.mvelezce.analysis.dynamic;

import edu.cmu.cs.mvelezce.analysis.Analysis;

import java.io.IOException;

public interface DynamicAnalysis<T> extends Analysis<T> {

  T analyze() throws IOException, InterruptedException;
}
