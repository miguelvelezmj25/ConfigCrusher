package edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import java.io.IOException;
import java.util.Map;

public interface DynamicAnalysis<T> extends Analysis<T> {

  Map<JavaRegion, T> analyze() throws IOException, InterruptedException;

  Map<JavaRegion, T> analyze(String[] args) throws IOException, InterruptedException;

}
