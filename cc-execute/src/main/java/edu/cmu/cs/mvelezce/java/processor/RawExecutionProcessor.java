package edu.cmu.cs.mvelezce.java.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.java.results.raw.RawPerfExecution;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class RawExecutionProcessor {

  private final String programName;
  private final String outputDir;

  public RawExecutionProcessor(String programName, String outputDir) {
    this.programName = programName;
    this.outputDir = outputDir;
  }

  public Object process(File rawOutputDir) throws IOException {
    Collection<File> files = FileUtils.listFiles(rawOutputDir, new String[] {"json"}, true);
    //    Set<PerformanceEntry> performanceEntries = new HashSet<>();

    for (File file : files) {
      RawPerfExecution rawPerfExecution = this.readFromFile(file);
      Object object = this.process(rawPerfExecution);
      //      performanceEntries.add(result);
    }

    //    return performanceEntries;
    throw new RuntimeException("Implement");
  }

  private Object process(RawPerfExecution rawPerfExecution) {
    List<String> trace = rawPerfExecution.getTrace();

    Map<String, Long> regionsToPerf = this.addRegions(trace);

    Deque<UUID> stack = new ArrayDeque<>();

    for (String entry : trace) {
      String[] item = entry.split(",");
      System.out.println();
    }

    throw new RuntimeException("Implement");
  }

  private Map<String, Long> addRegions(List<String> trace) {
    Map<String, Long> regionsToPerf = new HashMap<>();

    for (String entry : trace) {
      String[] items = entry.split(",");
      regionsToPerf.put(items[1].trim(), 0L);
    }

    return regionsToPerf;
  }

  private RawPerfExecution readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(file, new TypeReference<RawPerfExecution>() {});
  }
}
