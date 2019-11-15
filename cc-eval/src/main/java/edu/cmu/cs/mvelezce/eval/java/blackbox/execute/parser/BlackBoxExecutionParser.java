package edu.cmu.cs.mvelezce.eval.java.blackbox.execute.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.region.RegionsManager;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BlackBoxExecutionParser extends BaseRawExecutionParser<ProcessedPerfExecution> {

  public BlackBoxExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  public void logExecution(Set<String> configuration, int iter) throws IOException {
    long time = this.getExecutionTime();
    Map<String, Long> regionsToTimes = new HashMap<>();
    regionsToTimes.put(RegionsManager.PROGRAM_REGION_ID.toString(), time);

    ProcessedPerfExecution blackBoxResult =
        new ProcessedPerfExecution(configuration, regionsToTimes);

    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, blackBoxResult);
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
