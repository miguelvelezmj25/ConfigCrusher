package edu.cmu.cs.mvelezce.tool.analysis.taint.java;

import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface StaticAnalysis {

    public Map<JavaRegion, Set<Set<String>>> analyze() throws IOException;

    public Map<JavaRegion, Set<Set<String>>> analyze(String[] args) throws IOException;

    public void writeToFile(Map<JavaRegion, Set<Set<String>>> relevantRegionsToOptions) throws IOException;

    public Map<JavaRegion, Set<Set<String>>> readFromFile(File file) throws IOException;
}
