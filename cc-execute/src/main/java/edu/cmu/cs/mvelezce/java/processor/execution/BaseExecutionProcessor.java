package edu.cmu.cs.mvelezce.java.processor.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseExecutionProcessor<T>
    implements Analysis<Map<Integer, Set<PerfExecution>>> {

  public static final String TRUE_REGION = "True";

  private final Map<Integer, Set<T>> itersToRawPerfExecutions;
  private final String outputDir;

  public BaseExecutionProcessor(
      String programName, Map<Integer, Set<T>> itersToRawPerfExecutions, String measuredTime) {
    this.itersToRawPerfExecutions = itersToRawPerfExecutions;

    this.outputDir =
        this.outputDir() + "/" + measuredTime + "/" + programName + "/execution/processed";
  }

  @Override
  public Map<Integer, Set<PerfExecution>> analyze(String[] args) throws IOException {
    Options.getCommandLine(args);

    File file = new File(this.outputDir);

    Options.checkIfDeleteResult(file);

    if (file.exists()) {
      return this.readFromFile(file);
    }

    Map<Integer, Set<PerfExecution>> results = this.analyze();

    if (Options.checkIfSave()) {
      this.writeToFile(results);
    }

    return results;
  }

  @Override
  public Map<Integer, Set<PerfExecution>> analyze() {
    Map<Integer, Set<PerfExecution>> itersToProcessedPerf = new HashMap<>();

    for (Map.Entry<Integer, Set<T>> entry : this.getItersToRawPerfExecutions().entrySet()) {
      Set<PerfExecution> processedPerfExecs = new HashSet<>();

      for (T rawPerfExecution : entry.getValue()) {
        PerfExecution perfExecution = this.getProcessedPerfExec(rawPerfExecution);
        processedPerfExecs.add(perfExecution);
      }

      itersToProcessedPerf.put(entry.getKey(), processedPerfExecs);
    }

    return itersToProcessedPerf;
  }

  protected abstract PerfExecution getProcessedPerfExec(T rawPerfExecution);

  @Override
  public void writeToFile(Map<Integer, Set<PerfExecution>> results) throws IOException {
    for (Map.Entry<Integer, Set<PerfExecution>> entry : results.entrySet()) {
      int iter = entry.getKey();

      for (PerfExecution perfExecution : entry.getValue()) {
        String outputFile =
            this.getOutputDir() + "/" + iter + "/" + UUID.randomUUID() + Options.DOT_JSON;
        File file = new File(outputFile);
        file.getParentFile().mkdirs();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, perfExecution);
      }
    }
  }

  @Override
  public Map<Integer, Set<PerfExecution>> readFromFile(File file) throws IOException {
    File[] dirs = file.listFiles();

    if (dirs == null) {
      throw new RuntimeException("Could not find any directories in " + file);
    }

    Map<Integer, Set<PerfExecution>> itersToProcessedPerfExecutions = new HashMap<>();

    for (File dir : dirs) {
      Set<PerfExecution> perfExecutions = new HashSet<>();

      Collection<File> files = FileUtils.listFiles(dir, new String[] {"json"}, false);

      for (File dataFile : files) {
        ObjectMapper mapper = new ObjectMapper();
        PerfExecution perfExecution =
            mapper.readValue(dataFile, new TypeReference<PerfExecution>() {});
        perfExecutions.add(perfExecution);
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
