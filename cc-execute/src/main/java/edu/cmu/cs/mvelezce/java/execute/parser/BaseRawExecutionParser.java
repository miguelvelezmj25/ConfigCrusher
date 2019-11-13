package edu.cmu.cs.mvelezce.java.execute.parser;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public abstract class BaseRawExecutionParser<T> {

  private final String programName;
  private final String outputDir;

  public BaseRawExecutionParser(String programName, String outputDir) {
    this.programName = programName;
    this.outputDir = outputDir;
  }

  public abstract void logExecution(Set<String> configuration, int iter) throws IOException;

  public abstract Map<Integer, Set<T>> readResults() throws IOException;

  public String getProgramName() {
    return programName;
  }

  public String getOutputDir() {
    return outputDir;
  }
}
