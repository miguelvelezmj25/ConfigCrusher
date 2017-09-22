package edu.cmu.cs.mvelezce.tool.performance.entry;

import java.io.File;
import java.io.IOException;

public interface PerformanceEntry {

    public void writeToFile(PerformanceEntry performanceEntry) throws IOException;

    public PerformanceEntry readFromFile(File file) throws IOException;

}
