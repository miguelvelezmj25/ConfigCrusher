package edu.cmu.cs.mvelezce.e2e.execute.instrument.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.e2e.execute.executor.parser.BaseE2EExecutionParser;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.region.RegionsManager;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class E2EInstrumentExecutionParser extends BaseE2EExecutionParser {

  public E2EInstrumentExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  public void logExecution(Set<String> configuration, int iter) throws IOException {
    long time = this.getExecutionTime();
    Map<String, Long> regionsToTimes = new HashMap<>();
    regionsToTimes.put(RegionsManager.PROGRAM_REGION_ID.toString(), time);

    ProcessedPerfExecution e2eResult = new ProcessedPerfExecution(configuration, regionsToTimes);

    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, e2eResult);
  }

  private long getExecutionTime() throws IOException {
    File dirFile = new File(".");
    Collection<File> serializedFiles = FileUtils.listFiles(dirFile, new String[] {"ser"}, false);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dirFile + " must have 1 file.");
    }

    return this.deserialize(serializedFiles.iterator().next());
  }

  private long deserialize(File file) throws IOException {
    String time = "";

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = reader.readLine()) != null) {
      time = line;
    }

    reader.close();

    return Long.parseLong(time);
  }

  @Override
  protected ProcessedPerfExecution readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<ProcessedPerfExecution>() {});
  }
}
