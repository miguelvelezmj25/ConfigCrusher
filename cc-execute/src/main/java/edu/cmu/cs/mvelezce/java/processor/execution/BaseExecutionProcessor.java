package edu.cmu.cs.mvelezce.java.processor.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseExecutionProcessor<T>
    implements Analysis<Map<Integer, Set<ProcessedPerfExecution>>> {

  private final Map<Integer, Set<T>> itersToRawPerfExecutions;
  private final String outputDir;

  public BaseExecutionProcessor(String programName, Map<Integer, Set<T>> itersToRawPerfExecutions) {
    this.itersToRawPerfExecutions = itersToRawPerfExecutions;

    this.outputDir = this.outputDir() + "/" + programName + "/execution/processed";
  }

  @Override
  public Map<Integer, Set<ProcessedPerfExecution>> analyze(String[] args)
      throws IOException, InterruptedException {
    Options.getCommandLine(args);

    File file = new File(this.outputDir);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return this.readFromFile(file);
    }

    Map<Integer, Set<ProcessedPerfExecution>> results = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(results);
    }

    return results;
  }

  @Override
  public Map<Integer, Set<ProcessedPerfExecution>> analyze() {
    Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerf = new HashMap<>();

    for (Map.Entry<Integer, Set<T>> entry : this.getItersToRawPerfExecutions().entrySet()) {
      Set<ProcessedPerfExecution> processedPerfExecs = new HashSet<>();

      for (T rawPerfExecution : entry.getValue()) {
        ProcessedPerfExecution processedPerfExecution = this.getProcessedPerfExec(rawPerfExecution);
        processedPerfExecs.add(processedPerfExecution);
      }

      itersToProcessedPerf.put(entry.getKey(), processedPerfExecs);
    }

    return itersToProcessedPerf;
  }

  protected abstract ProcessedPerfExecution getProcessedPerfExec(T rawPerfExecution);

  @Override
  public void writeToFile(Map<Integer, Set<ProcessedPerfExecution>> results) throws IOException {
    for (Map.Entry<Integer, Set<ProcessedPerfExecution>> entry : results.entrySet()) {
      int iter = entry.getKey();

      for (ProcessedPerfExecution processedPerfExecution : entry.getValue()) {
        String outputFile =
            this.getOutputDir() + "/" + iter + "/" + UUID.randomUUID() + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, processedPerfExecution);
      }
    }
  }

  @Override
  public Map<Integer, Set<ProcessedPerfExecution>> readFromFile(File file) throws IOException {
    File[] dirs = file.listFiles();

    if (dirs == null) {
      throw new RuntimeException("Could not find any directories in " + file);
    }

    Map<Integer, Set<ProcessedPerfExecution>> itersToProcessedPerfExecutions = new HashMap<>();

    for (File dir : dirs) {
      Set<ProcessedPerfExecution> perfExecutions = new HashSet<>();

      Collection<File> files = FileUtils.listFiles(dir, new String[] {"json"}, false);

      for (File dataFile : files) {
        ObjectMapper mapper = new ObjectMapper();
        ProcessedPerfExecution processedPerfExecution =
            mapper.readValue(dataFile, new TypeReference<ProcessedPerfExecution>() {});
        perfExecutions.add(processedPerfExecution);
      }

      itersToProcessedPerfExecutions.put(Integer.parseInt(dir.getName()), perfExecutions);
    }

    return itersToProcessedPerfExecutions;
  }

  public Map<Integer, Set<T>> getItersToRawPerfExecutions() {
    return itersToRawPerfExecutions;
  }

  public String getOutputDir() {
    return outputDir;
  }
}
