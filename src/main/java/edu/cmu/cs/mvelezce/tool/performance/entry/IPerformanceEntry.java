package edu.cmu.cs.mvelezce.tool.performance.entry;

import java.io.File;
import java.io.IOException;

public interface IPerformanceEntry {

    public void writeToFile(IPerformanceEntry performanceEntry) throws IOException;

    public IPerformanceEntry readFromFile(File file) throws IOException;

}
