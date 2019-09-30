package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import java.io.IOException;

public interface DynamicAnalysis<T> extends Analysis<T> {

  T analyze() throws IOException, InterruptedException;

  T analyze(String[] args) throws IOException, InterruptedException;

}
