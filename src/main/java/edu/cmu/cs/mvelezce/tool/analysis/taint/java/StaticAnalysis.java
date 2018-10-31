package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.Analysis;
import java.io.IOException;
import java.util.Map;

// TODO use generics for java region
public interface StaticAnalysis<T> extends Analysis<T> {

  Map<JavaRegion, T> analyze() throws IOException;

  Map<JavaRegion, T> analyze(String[] args) throws IOException;

}
