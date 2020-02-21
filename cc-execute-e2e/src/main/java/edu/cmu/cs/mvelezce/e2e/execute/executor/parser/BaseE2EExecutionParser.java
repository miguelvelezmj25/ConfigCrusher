package edu.cmu.cs.mvelezce.e2e.execute.executor.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;
import edu.cmu.cs.mvelezce.region.RegionsManager;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseE2EExecutionParser
    extends BaseRawExecutionParser<ProcessedPerfExecution> {

  public BaseE2EExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  public void logExecution(Set<String> configuration, int iter) throws IOException {
    File dirFile = new File(".");
    Collection<File> serializedFiles = FileUtils.listFiles(dirFile, new String[] {"ser"}, false);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dirFile + " must have 1 file.");
    }

    long time = this.getExecutionTime(serializedFiles.iterator().next());
    this.logExecution(configuration, iter, time);
  }

  protected abstract long getExecutionTime(File file) throws IOException;

  private void logExecution(Set<String> configuration, int iter, long time) throws IOException {
    Map<String, Long> regionsToTimes = new HashMap<>();
    regionsToTimes.put(RegionsManager.PROGRAM_REGION_ID.toString(), time);

    ProcessedPerfExecution e2eResult = new ProcessedPerfExecution(configuration, regionsToTimes);

    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, e2eResult);
  }
}
