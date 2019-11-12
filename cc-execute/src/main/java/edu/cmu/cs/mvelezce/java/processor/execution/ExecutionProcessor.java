package edu.cmu.cs.mvelezce.java.processor.execution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.Analysis;
import edu.cmu.cs.mvelezce.java.execute.instrumentation.region.RegionsManager;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.java.results.raw.RawPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class ExecutionProcessor
    implements Analysis<Map<Integer, Set<ProcessedPerfExecution>>> {

  private final Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions;
  private final String outputDir;

  public ExecutionProcessor(
      String programName, Map<Integer, Set<RawPerfExecution>> itersToRawPerfExecutions) {
    this.itersToRawPerfExecutions = itersToRawPerfExecutions;

    this.outputDir = this.outputDir() + "/" + programName + "/execution/processed";
  }

  @Override
  public Map<Integer, Set<ProcessedPerfExecution>> analyze(String[] args) throws IOException {
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

    for (Map.Entry<Integer, Set<RawPerfExecution>> entry :
        this.itersToRawPerfExecutions.entrySet()) {
      Set<ProcessedPerfExecution> processedPerfExecs = new HashSet<>();

      for (RawPerfExecution rawPerfExecution : entry.getValue()) {
        Map<String, Long> regionToPerf = this.process(rawPerfExecution);
        Set<String> config = rawPerfExecution.getConfiguration();

        ProcessedPerfExecution processedPerfExecution =
            new ProcessedPerfExecution(config, regionToPerf);
        processedPerfExecs.add(processedPerfExecution);
      }

      itersToProcessedPerf.put(entry.getKey(), processedPerfExecs);
    }

    return itersToProcessedPerf;
  }

  private Map<String, Long> process(RawPerfExecution rawPerfExecution) {
    List<String> trace = rawPerfExecution.getTrace();
    Map<String, Long> regionsToPerf = this.addRegions(trace);
    this.addPerfs(regionsToPerf, trace);

    return regionsToPerf;
  }

  private void addPerfs(Map<String, Long> regionsToPerf, List<String> trace) {
    Deque<String> executingRegions = new ArrayDeque<>();
    Deque<Long> executionRegionsStart = new ArrayDeque<>();
    Deque<Long> nestedRegionsExecTime = new ArrayDeque<>();

    for (int i = 0; i < trace.size(); i++) {
      String entry = trace.get(i);
      String[] items = entry.split(RegionsManager.COMMA);
      String action = items[0];
      String region = items[1];
      long time = Long.parseLong(items[2]);

      if (action.equals(RegionsManager.START)) {
        executingRegions.push(region);
        executionRegionsStart.push(time);
        nestedRegionsExecTime.push(0L);
      } else {
        if (!region.equals(executingRegions.peek())) {
          throw new RuntimeException("Exiting a region that we did not expected");
        }

        executingRegions.pop();

        long regionStart = executionRegionsStart.pop();
        long nestedRegionTime = nestedRegionsExecTime.pop();
        long regionExecTime = time - regionStart - nestedRegionTime;

        long currentTime = regionsToPerf.get(region);
        currentTime += regionExecTime;
        regionsToPerf.put(region, currentTime);

        if (executingRegions.isEmpty()) {
          continue;
        }

        Deque<Long> tmp = new ArrayDeque<>(nestedRegionsExecTime.size());

        while (!nestedRegionsExecTime.isEmpty()) {
          long nestedRegionExecTime = nestedRegionsExecTime.pop();
          tmp.push(nestedRegionExecTime + regionExecTime);
        }

        while (!tmp.isEmpty()) {
          long exec = tmp.pop();
          nestedRegionsExecTime.push(exec);
        }
      }
    }
  }

  private Map<String, Long> addRegions(List<String> trace) {
    Map<String, Long> regionsToPerf = new HashMap<>();

    for (String entry : trace) {
      String[] items = entry.split(RegionsManager.COMMA);
      regionsToPerf.put(items[1].trim(), 0L);
    }

    return regionsToPerf;
  }

  @Override
  public void writeToFile(Map<Integer, Set<ProcessedPerfExecution>> results) throws IOException {
    for (Map.Entry<Integer, Set<ProcessedPerfExecution>> entry : results.entrySet()) {
      int iter = entry.getKey();

      for (ProcessedPerfExecution processedPerfExecution : entry.getValue()) {
        String outputFile =
            this.outputDir + "/" + iter + "/" + UUID.randomUUID() + Options.DOT_JSON;
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
}
