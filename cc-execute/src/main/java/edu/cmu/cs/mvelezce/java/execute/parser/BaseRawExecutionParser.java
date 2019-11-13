package edu.cmu.cs.mvelezce.java.execute.parser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseRawExecutionParser<T> {

  private final String programName;
  private final String outputDir;

  public BaseRawExecutionParser(String programName, String outputDir) {
    this.programName = programName;
    this.outputDir = outputDir;
  }

  public abstract void logExecution(Set<String> configuration, int iter)
      throws IOException, InterruptedException;

  public Map<Integer, Set<T>> readResults() throws IOException {
    Map<Integer, Set<T>> itersToPerfExecutions = new HashMap<>();

    int iter = 0;
    File file = new File(this.getRawOutputDir(iter));

    while (file.exists()) {
      Set<T> perfExecs = new HashSet<>();
      Collection<File> files = FileUtils.listFiles(file, new String[] {"json"}, true);

      for (File perfFile : files) {
        T perfExec = this.readFromFile(perfFile);
        perfExecs.add(perfExec);
      }

      itersToPerfExecutions.put(iter, perfExecs);

      iter++;
      file = new File(this.getRawOutputDir(iter));
    }

    return itersToPerfExecutions;
  }

  protected abstract T readFromFile(File perfFile) throws IOException;

  public String getRawOutputDir(int iter) {
    return this.getOutputDir() + "/" + this.getProgramName() + "/execution/raw/" + iter;
  }

  public String getProgramName() {
    return programName;
  }

  public String getOutputDir() {
    return outputDir;
  }
}
